package com.xiaotao.saltedfishcloud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaotao.saltedfishcloud.config.DiskConfig;
import com.xiaotao.saltedfishcloud.config.security.AllowAnonymous;
import com.xiaotao.saltedfishcloud.dao.mybatis.UserDao;
import com.xiaotao.saltedfishcloud.dao.redis.TokenDao;
import com.xiaotao.saltedfishcloud.entity.po.JsonResult;
import com.xiaotao.saltedfishcloud.entity.po.QuotaInfo;
import com.xiaotao.saltedfishcloud.entity.po.User;
import com.xiaotao.saltedfishcloud.service.http.ResponseService;
import com.xiaotao.saltedfishcloud.service.user.UserService;
import com.xiaotao.saltedfishcloud.utils.JwtUtils;
import com.xiaotao.saltedfishcloud.utils.SecureUtils;
import com.xiaotao.saltedfishcloud.exception.JsonException;
import com.xiaotao.saltedfishcloud.exception.UserNoExistException;
import com.xiaotao.saltedfishcloud.validator.annotations.UID;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(UserController.PREFIX)
@ResponseBody
@Validated
@RequiredArgsConstructor
public class UserController {
    public static final String PREFIX = "/api/user";

    private final UserService userService;
    private final ResponseService responseService;
    private final UserDao userDao;
    private final TokenDao tokenDao;
    /**
     * 获取用户基本信息，并刷新token有效期
     */
    @GetMapping
    public JsonResult getUserInfo(HttpServletRequest request) throws UserNoExistException {
        var user =  SecureUtils.getSpringSecurityUser();
        if (user == null) {
            throw new JsonException(401, "未登录");
        }
        tokenDao.setToken(user.getUsername(), request.getHeader(JwtUtils.AUTHORIZATION));
        return JsonResult.getInstance(user);
    }

    /**
     * 注册新账号，管理员可直接添加无需邀请码
     * @param user  用户名
     * @param rawPassword   原始密码（即密码原文）
     * @param regCode   注册邀请码
     */
    @PostMapping
    @AllowAnonymous
    public JsonResult regUser(@RequestParam("user") String user,
                              @RequestParam("passwd") String rawPassword,
                              @RequestParam(value = "regcode", defaultValue = "") String regCode,
                              @RequestParam(value = "type", defaultValue = "0") int type
                              ) throws JsonException {
        if (SecureUtils.getSpringSecurityUser() != null && SecureUtils.getSpringSecurityUser().getType() == User.TYPE_ADMIN) {
            userService.addUser(user, rawPassword, type == User.TYPE_ADMIN ? User.TYPE_ADMIN : User.TYPE_COMMON);
        } else if (!regCode.equals(DiskConfig.REG_CODE)) {
            throw new JsonException(400, "注册码不正确");
        } else {
            userService.addUser(user, rawPassword, User.TYPE_COMMON);
        }
        return JsonResult.getInstance();
    }

    /**
     * 上传用户头像
     * @param file  头像文件
     */
    @PostMapping("avatar")
    public JsonResult uploadAvatar(@RequestParam("file") MultipartFile file) {
        userService.setAvatar(SecureUtils.getSpringSecurityUser().getUsername(), file);
        return JsonResult.getInstance();
    }

    /**
     * 获取用户头像信息，直接响应头像文件
     * @param username  用户名
     */
    @GetMapping({
            "avatar/{username}",
            "avatar"
    })
    @AllowAnonymous
    public ResponseEntity<org.springframework.core.io.Resource>
                getAvatar(HttpServletResponse response, @PathVariable(required = false) String username) throws IOException {
        try {
            String profilePath = username == null ? DiskConfig.getLoginUserProfileRoot() : DiskConfig.getUserProfileRoot(username);
            File[] avatars = new File(profilePath).listFiles(pathname -> pathname.getName().contains("avatar"));

            // 数组越界，空指针操作均视为头像不存在
            return responseService.sendFile(avatars[0].getPath());
        } catch (Exception e) {
            response.sendRedirect("/api/static/static/defaultAvatar.png");
            return null;
        }
    }

    /**
     * 获取用户空间配额使用情况
     */
    @GetMapping("quota")
    public JsonResult getQuotaUsed() {
        QuotaInfo used = userDao.getUserQuotaUsed(SecureUtils.getSpringSecurityUser().getId());
        return JsonResult.getInstance(used);
    }

    /**
     * @param oldPasswd 旧密码
     * @param newPasswd 新密码
     */
    @PostMapping("{uid}/passwd")
    public JsonResult modifyPassword(@RequestParam("old") String oldPasswd,
                                     @RequestParam("new") String newPasswd,
                                     @PathVariable("uid") @UID int uid,
                                     @RequestParam(value = "force", defaultValue = "false") boolean force) throws AccessDeniedException {
        User user = SecureUtils.getSpringSecurityUser();
        if (force) {
            if ( user.getType() != User.TYPE_ADMIN) {
                throw new AccessDeniedException("非管理员不允许使用force参数");
            } else {
                userDao.modifyPassword(uid, SecureUtils.getPassswd(newPasswd));
                tokenDao.cleanUserToken(user.getUsername());
                return JsonResult.getInstance(200, null, "force reset");
            }
        } else {
            tokenDao.cleanUserToken(user.getUsername());
            int i = userService.modifyPasswd(uid, oldPasswd, newPasswd);
            return JsonResult.getInstance(200, i, "ok");
        }
    }

    /**
     * 授予或撤销用户权限
     * @param uid  被操纵的用户ID
     * @param type 要设置的权限类型
     */
    @PutMapping("{uid}/type/{typeCode}")
    @RolesAllowed({"ADMIN"})
    public JsonResult grant(@PathVariable("uid") int uid,
                            @PathVariable("typeCode") int type) {
        User user = userDao.getUserById(uid);
        if (user == null) {
            throw new UserNoExistException();
        }
        userService.grant(uid, type);
        return JsonResult.getInstance();
    }

    /**
     * 取用户列表
     * @param page 页数
     * @param size 每页大小
     */
    @GetMapping("list")
    @RolesAllowed({"ADMIN"})
    public JsonResult getUserList(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "size", defaultValue = "10") @Max(50) @Min(5) @Valid int size) {
        PageHelper.startPage(page, 10);
        List<User> userList = userDao.getUserList();
        userList.forEach(e -> e.setPwd(null));
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        return JsonResult.getInstance(pageInfo);
    }

}
