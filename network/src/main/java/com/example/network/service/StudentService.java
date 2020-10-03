package com.example.network.service;

import com.example.network.vo.JsonResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;

@Service
public class StudentService {

    public JsonResponse login(String username, String password){
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()){
            try{
                subject.login(new UsernamePasswordToken(username,password));
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
}
