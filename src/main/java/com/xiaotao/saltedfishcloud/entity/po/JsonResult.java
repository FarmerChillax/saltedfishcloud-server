package com.xiaotao.saltedfishcloud.entity.po;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.xiaotao.saltedfishcloud.entity.CommonPageInfo;
import org.springframework.data.domain.Page;

import java.util.LinkedHashMap;

/**
 * @TODO 修改为可迭代的接口或抽象类，并编写一个默认的实现类和一个空数据的只读单例实现类
 */
public class JsonResult extends LinkedHashMap<String, Object>{
    private static final long serialVersionUID = 1537580038140716422L;

    public JsonResult() {
        this(200, null, "OK");
    }

    public JsonResult(int code, Object data, String msg) {
        this.put("code", code);
        this.put("data", data);
        this.put("msg", msg);
    }

    @Override
    public JsonResult put(String key, Object obj) {
        super.put(key, obj);
        return this;
    }

    public static JsonResult getInstance(int code, Object data, String msg) {
        return new JsonResult(code, data, msg);
    }
    public static JsonResult getInstance(Object data) {
        return getInstance(200, data, "OK");
    }
    public static JsonResult getInstance() {
        return getInstance(200, null, "OK");
    }

    public static JsonResult getInstanceWithPage(Page<?> page) {
        return JsonResult.getInstance(CommonPageInfo.of(page));
    }

    public static JsonResult getInstanceWithPage(PageInfo<?> page) {
        return JsonResult.getInstance(CommonPageInfo.of(page));
    }

    /**
     * 获取一个数据Map实例
     */
    public static LinkedHashMap<String, Object> getDataMap() {
        return new LinkedHashMap<>();
    }

    public int getCode() {
        return (int)this.get("code");
    }

    public JsonResult setCode(int code) {
        this.put("code", code);
        return this;
    }

    public Object getData() {
        return this.get("data");
    }

    public JsonResult setData(Object data) {
        this.put("data", data);
        return this;
    }

    public String getMsg() {
        return (String)this.get("msg");
    }

    public JsonResult setMsg(String msg) {
        this.put("msg", msg);
        return this;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
