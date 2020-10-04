package com.example.network.service;

import com.example.network.dao.UserMapper;
import com.example.network.vo.JsonResponse;
import com.example.network.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShiroServiceImpl implements ShiroService{

    @Resource
    UserMapper userMapper;

    @Override
    public JsonResponse login(String id, String password) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()){
            try{
                subject.login(new UsernamePasswordToken(id,password));
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

    @Override
    public JsonResponse logout() {
        return null;
    }

    @Override
    public JsonResponse register(User user) {
        Md5Hash md5Hash = new Md5Hash(user.getPassword(),user.getId(),10);
        user.setPassword(md5Hash.toHex());
        System.out.println(user.toString());
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
}
