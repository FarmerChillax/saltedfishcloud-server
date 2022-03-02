package com.xiaotao.saltedfishcloud.orm.config.utils;

import com.xiaotao.saltedfishcloud.orm.config.annotation.ConfigEntity;
import com.xiaotao.saltedfishcloud.orm.config.entity.MethodInst;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import sun.reflect.misc.FieldUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigUtils {
    @Data
    @AllArgsConstructor
    public static class ConfigBinding {
        private String key;
        private Object inst;
        private Method method;
    }
    /**
     * 通过setter方法名获取对应的小驼峰命名法的字段名
     * @param name  setter方法名
     * @return      小驼峰命名法的字段名
     */
    public static String getFieldNameByMethodName(String name) {
        if (!name.startsWith("set")) {
            throw new IllegalStateException(name + " 不合法");
        }
        return ((char)(name.charAt(3) + 32) + name.substring(4));
    }

    /**
     * 通过小驼峰命名法的字段名获取对应的setter方法名
     * @param name  小驼峰命名法的字段名
     * @return      setter方法名
     */
    public static String getMethodNameByFieldName(String name) {
        return "set" + ((char)(name.charAt(0) - 32) + name.substring(1));
    }

    /**
     * 获取对象中指定配置节点属性的方法实例
     * @TODO 实现通过配置节点读取值
     * @TODO 出现错误时候抛异常
     * @param key   配置节点
     * @param obj   配置对象
     * @return      key为配置节点，value为setter方
     */
    public static MethodInst getMethodInst(String key, Object obj) {
        final String[] nodes = key.split("\\.");
        if (nodes.length < 2) {
            throw new IllegalArgumentException("无效的配置节点：" + key);
        }
        Object curObj = obj;
        for (int i = 1; i < nodes.length; i++) {
            String node = nodes[i];

            if (i == nodes.length - 1) {
                try {
                    return new MethodInst(
                            curObj.getClass().getDeclaredMethod(getMethodNameByFieldName(node), curObj.getClass().getDeclaredField(node).getType()),
                            curObj
                    );
                } catch (NoSuchMethodException | NoSuchFieldException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            try {
                final Field targetField = curObj.getClass().getDeclaredField(node);
                targetField.setAccessible(true);
                curObj = targetField.get(curObj);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
                return null;
            }

            final ConfigEntity configEntity = curObj.getClass().getDeclaredAnnotation(ConfigEntity.class);
            if (configEntity == null || !node.equals(configEntity.value())) {
                throw new IllegalArgumentException("不存在的节点：" + key);
            }
        }
        return null;
    }

    /**
     * 获取对象中声明的所有配置节点
     * @param clazz   类
     * @return  配置节点名集合
     */
    public static List<String> getAllConfigKey(Class<?> clazz) {
        final ConfigEntity annotation = AnnotationUtils.findAnnotation(clazz, ConfigEntity.class);
        List<String> res = new ArrayList<>();

        // 先获取所有的字段
        final Map<String, Field> fieldMap = Arrays.stream(clazz.getDeclaredFields()).
                collect(
                        Collectors.toMap(
                                Field::getName,
                                e -> e,
                                (v1, v2) -> v1)

                );

        // 再获取所有的setter方法
        for (Method method : ReflectionUtils.getAllDeclaredMethods(clazz)) {
            if (!method.getName().startsWith("set") || method.getParameters().length != 1) {
                continue;
            }
            assert annotation != null;
            final String fieldName = ConfigUtils.getFieldNameByMethodName(method.getName());
            final Field field = fieldMap.get(fieldName);


            // setter方法没有对应的字段的跳过
            if (field == null) {
                continue;
            }

            final String key = annotation.value() + "." + fieldName;
            res.add(key);

            final Class<?> fieldType = field.getType();
            final ConfigEntity fieldAnnotation = fieldType.getDeclaredAnnotation(ConfigEntity.class);
            if (fieldAnnotation != null) {
                res.addAll(
                        getAllConfigKey(fieldType)
                                .stream()
                                .map(e -> annotation.value() + "." + e)
                                .collect(Collectors.toSet())
                );
            }
        }
        return res;
    }
}
