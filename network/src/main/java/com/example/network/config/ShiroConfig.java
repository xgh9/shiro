package com.example.network.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //其实可以把下面两个方法的东西放到这个方法中，不用交给spring
    //关系  ShiroFilterFactoryBean<-DefaultWebSecurityManager<-MyRealm<-HashedCredentialsMatcher
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        // setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
        shiroFilterFactoryBean.setLoginUrl("/notLogin");
        // 设置无权限时跳转的 url;
        shiroFilterFactoryBean.setUnauthorizedUrl("/notAdmin");

        // 设置拦截器
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //游客，开发权限
//        filterChainDefinitionMap.put("/guest/**", "anon");
        //用户，需要角色权限 “user”
//        filterChainDefinitionMap.put("/user/**", "roles[user]");
        //管理员，需要角色权限 “admin”
//        filterChainDefinitionMap.put("/admin/**", "roles[admin]");
//        filterChainDefinitionMap.put("/getRole", "roles[admin]");
        //开放登陆接口
//        filterChainDefinitionMap.put("/login", "anon");
        //其余接口一律拦截
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
//        filterChainDefinitionMap.put("/**", "authc");

//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 将Web安全管理器注册到spring容器中
     * @param myRealm 自定义验证和授权的realm
     * @param matcher 自定义密码匹配器
     * @return
     */
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(MyRealm myRealm, HashedCredentialsMatcher matcher){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        defaultWebSecurityManager.setRealm(myRealm);

        return defaultWebSecurityManager;
    }



    @Bean
    public MyRealm getRealm(HashedCredentialsMatcher matcher){
        MyRealm realm = new MyRealm();

        //开启缓存管理
        realm.setCacheManager(new EhCacheManager());
        //开启全局缓存
        realm.setCachingEnabled(true);
        //开启认证缓存
        realm.setAuthenticationCachingEnabled(true);
        realm.setAuthenticationCacheName("authenticationCache");
        //开启授权缓存
        realm.setAuthorizationCachingEnabled(true);
        realm.setAuthorizationCacheName("authorizationCache");

        //给realm设置密码匹配器
        realm.setCredentialsMatcher(matcher);
        return realm;
    }

    //将密码匹配器注册到spring容器中
    @Bean
    public HashedCredentialsMatcher getHashedCredentialsMatcher(){
        //设置密码匹配器
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //设置加密算法
        matcher.setHashAlgorithmName("MD5");
        //设置散列次数
        matcher.setHashIterations(10);

        return matcher;
    }
}
