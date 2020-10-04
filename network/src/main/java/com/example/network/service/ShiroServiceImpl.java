package com.example.network.service;

import com.example.network.config.MyRealm;
import com.example.network.dao.UserMapper;
import com.example.network.vo.ConstantField;
import com.example.network.vo.JsonResponse;
import com.example.network.vo.User;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;

@Service
public class ShiroServiceImpl implements ShiroService{

    @Resource
    UserMapper userMapper;

    @Autowired
    MyRealm myRealm;

    @Override
    public JsonResponse login(String id, String password) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()){
            try{
                UsernamePasswordToken token = new UsernamePasswordToken(id, password);
//                token.setRememberMe(true);
                subject.login(token);
            }catch (UnknownAccountException e){
                return JsonResponse.invalidParam("用户名错误");
            }catch (IncorrectCredentialsException e){
                return JsonResponse.invalidParam("密码错误");
            }
            return JsonResponse.success();
        }else {
            return JsonResponse.repeatLogin((String)subject.getPrincipal());
        }
    }


    //注册成功后不能自动登录，因为只有管理员才能创建管理员账户，自动登录会导致重复登陆
    @Transactional
    @Override
    public JsonResponse register(User user){
        String password = user.getPassword();
        Md5Hash md5Hash = new Md5Hash(user.getPassword(),user.getId(),10);
        user.setPassword(md5Hash.toHex());
        int res = userMapper.insert(user);
        if (res == 0){
            return JsonResponse.unknownError();
        }

        return JsonResponse.success();
    }

    /**
     * 检查学号是否已经存在
     * @param id 学号
     * @return 0 表示不存在
     */
    @Override
    public int checkExist(String id) {
        return userMapper.checkExist(id);
    }

    @Override
    public User getUserById(String id) {
        return userMapper.getUserById(id);
    }

    @Override
    public boolean checkAuthentication(){
        Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated();
    }

    @Override
    public boolean checkAdminPermission(){
        Subject subject = SecurityUtils.getSubject();
        return subject.hasRole(ConstantField.ROLE_ADMIN);
    }

    //修改改密码后在另一个浏览器还可以用旧密码登录
    //因为修改密码后缓存中会保存原密码，只有注销当前账户才会清空旧密码的缓存，因此手动清空缓存
    @Transactional
    @Override
    public JsonResponse changePassowrd(String oldPassword, String password){
        Subject subject = SecurityUtils.getSubject();
        String id = (String) subject.getPrincipal();
        Md5Hash md5Hash = new Md5Hash(oldPassword, id,10);
        User user = getUserById(id);

        if (!user.getPassword().equals(md5Hash.toHex())){
            return JsonResponse.invalidParam("旧密码不对劲！");
        }
        Md5Hash newMd5Hash = new Md5Hash(password, id, 10);
        user.setPassword(newMd5Hash.toHex());

        //手动清空缓存  如果有更好的获取缓存管理器的方法请告诉我
        Cache<Object, AuthenticationInfo> authenticationCache = myRealm.getAuthenticationCache();
        authenticationCache.remove(id);

        userMapper.update(user);

        return JsonResponse.success();
    }

    /**
     * 好多好多坑
     * 1，未登录时 AuthorizationInfo 和 subject.getPrincipal()都会报空指针异常，要提前判断登陆状态
     * 2，权限缓存Cache<Object, AuthorizationInfo> authorizationCache的key 是 subject.getPrincipals()
     *      subject.getPrincipal() 和  （String）subject.getPrincipal()都获取不到值
     * 3，bean被静态工具类注入时为null ,解决办法见JsonResponse
     * @return
     */
    @Override
    public String getRole() {
        Subject subject = SecurityUtils.getSubject();

        //未登录时返回""
        if (!subject.isAuthenticated()){
            return "";
        }

        Cache<Object, AuthorizationInfo> authorizationCache = myRealm.getAuthorizationCache();
        //第一次调用的时候缓存中没有info
        AuthorizationInfo info = authorizationCache.get(subject.getPrincipals());
        if (info != null){
            Collection<String> roles = info.getRoles();
            Iterator<String> iterator = roles.iterator();
            if (roles.size() != 0){
                String role = roles.iterator().next();
                //系统中用户都是单角色
                return role;
            }
        }

        //如果缓存中没有就去数据库中取
        return getUserById((String)subject.getPrincipal()).getRole();
    }


}
