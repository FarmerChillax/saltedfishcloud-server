package com.xiaotao.saltedfishcloud.controller.admin;

import com.xiaotao.saltedfishcloud.annotations.ReadOnlyBlock;
import com.xiaotao.saltedfishcloud.config.DiskConfig;
import com.xiaotao.saltedfishcloud.config.StoreType;
import com.xiaotao.saltedfishcloud.dao.mybatis.ConfigDao;
import com.xiaotao.saltedfishcloud.dao.mybatis.ProxyDao;
import com.xiaotao.saltedfishcloud.entity.po.ConfigInfo;
import com.xiaotao.saltedfishcloud.entity.po.JsonResult;
import com.xiaotao.saltedfishcloud.entity.po.ProxyInfo;
import com.xiaotao.saltedfishcloud.service.config.ConfigName;
import com.xiaotao.saltedfishcloud.service.config.ConfigServiceImpl;
import com.xiaotao.saltedfishcloud.service.mail.MailProperties;
import com.xiaotao.saltedfishcloud.service.manager.AdminService;
import com.xiaotao.saltedfishcloud.exception.JsonException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping(SysController.prefix)
@RolesAllowed({"ADMIN"})
@Validated
public class SysController {
    public static final String prefix = "/api/admin/sys/";
    @Resource
    private ConfigServiceImpl configService;
    @Resource
    private ConfigDao configDao;
    @Resource
    private AdminService adminService;
    @Resource
    private ProxyDao proxyDao;

    @PutMapping("mailConfig")
    public JsonResult setMailProperties(@RequestBody MailProperties mailProperties) {
        configService.setMailProperties(mailProperties);
        return JsonResult.getInstance();
    }

    @GetMapping("overview")
    public JsonResult getOverview() {
        LinkedHashMap<String, Object> res = JsonResult.getDataMap();
        res.put("store", adminService.getStoreState());
        res.put("invite_reg_code", DiskConfig.REG_CODE);
        return JsonResult.getInstance(res);
    }


    @GetMapping({"settings", "config"})
    public JsonResult getSysSettings() {
        List<ConfigInfo> res = configDao.getAllConfig();
        LinkedHashMap<String, Object> data = JsonResult.getDataMap();
        res.forEach(e -> data.put(e.getKey().toString(), e.getValue()));
        return JsonResult.getInstance(data);
    }

    @GetMapping("configKeys")
    public JsonResult getConfigKeys() {
        return JsonResult.getInstance(ConfigName.values());
    }

    @GetMapping("config/{key}")
    public JsonResult getConfig(@PathVariable String key) {
        String res = configService.getConfig(ConfigName.valueOf(key));
        return JsonResult.getInstance(res);
    }


    /**
     * 设置存储类型
     * @param type      存储类型
     */
    @PutMapping("config/STORE_TYPE/{type}")
    @ReadOnlyBlock
    public JsonResult setStoreType(@PathVariable("type") String type) throws IOException {
        try {
            StoreType storeType = StoreType.valueOf(type.toUpperCase());
            if (configService.setStoreType(storeType)) {
                return JsonResult.getInstance();
            } else {
                return JsonResult.getInstance(202, DiskConfig.STORE_TYPE.toString(), "请求被忽略，模式无变化");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的类型，可选RAW或UNIQUE");
        }
    }

    /**
     * 设置配置项参数值
     * @param key       配置项名
     * @param value     值
     */
    @PutMapping("config/{key}/{value}")
    public JsonResult setConfig(@PathVariable String key, @PathVariable String  value) throws IOException {
        configService.setConfig(key, value);
        return JsonResult.getInstance();
    }

    @PostMapping("proxy")
    public JsonResult addProxy(@Validated ProxyInfo info) {
        try {
            proxyDao.addProxy(info);
        } catch (DuplicateKeyException e) {
            throw new JsonException(400, "名称已存在");
        }
        return JsonResult.getInstance();
    }

    @GetMapping("proxy")
    public JsonResult getAllProxy() {
        return JsonResult.getInstance(proxyDao.getAllProxy());
    }

    @PutMapping("proxy")
    public JsonResult modifyProxy(@Valid ProxyInfo info, String proxyName) {
        if (proxyDao.modifyProxy(proxyName, info) == 0) {
            throw new JsonException(400, "代理" + proxyName + "不存在");
        }
        return JsonResult.getInstance();
    }

    @DeleteMapping("proxy")
    public JsonResult deleteProxy(@RequestParam String proxyName) {
        if (proxyDao.removeProxy(proxyName) == 0) {
            throw new JsonException(400, "代理" + proxyName + "不存在");
        }
        return JsonResult.getInstance();
    }


}
