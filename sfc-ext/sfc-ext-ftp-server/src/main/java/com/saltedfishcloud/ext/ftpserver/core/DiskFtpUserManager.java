package com.saltedfishcloud.ext.ftpserver.core;

import com.xiaotao.saltedfishcloud.dao.mybatis.UserDao;
import com.xiaotao.saltedfishcloud.utils.SecureUtils;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.AnonymousAuthentication;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;


public class DiskFtpUserManager implements UserManager {
    @Autowired
    private UserDao userDao;

    @Override
    public BaseUser getUserByName(String username) {
        BaseUser ftpUser = new BaseUser();
        List<Authority> authorities = new LinkedList<>();

        authorities.add(new ConcurrentLoginPermission(0, 0));
        authorities.add(new TransferRatePermission(0, 0));
        ftpUser.setAuthorities(authorities);
        ftpUser.setName(username);
        ftpUser.setHomeDirectory("/");
        return ftpUser;
    }

    @Override
    public String[] getAllUserNames() throws FtpException {
        String[] users = (String[]) userDao.getUserList()
                .stream()
                .map(com.xiaotao.saltedfishcloud.model.po.User::getUsername)
                .toArray();
        return users;
    }

    @Override
    public void delete(String username) throws FtpException {

    }

    @Override
    public void save(User user) throws FtpException {

    }

    @Override
    public boolean doesExist(String username) throws FtpException {
        return userDao.getUserByUser(username) != null;
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        if (authentication instanceof AnonymousAuthentication) {
            return getUserByName(com.xiaotao.saltedfishcloud.model.po.User.SYS_NAME_PUBLIC);
        }
        if (authentication instanceof UsernamePasswordAuthentication) {
            UsernamePasswordAuthentication auth = (UsernamePasswordAuthentication) authentication;
            com.xiaotao.saltedfishcloud.model.po.User user = userDao.getUserByUser(auth.getUsername());
            if (user == null) return null;
            if (user.getPassword().equals(SecureUtils.getPassswd(auth.getPassword()))) {
                return getUserByName(auth.getUsername());
            }
        }
        return null;
    }

    @Override
    public String getAdminName() throws FtpException {
        return null;
    }

    @Override
    public boolean isAdmin(String username) throws FtpException {
        return userDao.getUserByUser(username).getType() == com.xiaotao.saltedfishcloud.model.po.User.TYPE_ADMIN;
    }
}

