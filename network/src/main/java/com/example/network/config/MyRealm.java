package com.example.network.config;

import com.example.network.dao.UserMapper;
import com.example.network.service.ShiroService;
import com.example.network.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    ShiroService shiroService;

    @Resource
    UserMapper userMapper;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String principal = (String) token.getPrincipal();
        User user = shiroService.getUserById(principal);

        if (!ObjectUtils.isEmpty(user)){
            System.out.println(user.toString());
            return new SimpleAuthenticationInfo(user.getId(),user.getPassword(), ByteSource.Util.bytes(user.getId()),this.getName());
        }
        return null;
    }
}
