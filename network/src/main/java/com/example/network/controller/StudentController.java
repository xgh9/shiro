package com.example.network.controller;

import com.example.network.vo.JsonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;

@Controller
@Api(value = "学生操作",tags = "学生操作")
public class StudentController {

    @ResponseBody
    @GetMapping("/testLog")
    @ApiOperation("测试是否登录")
    @RolesAllowed("123")
    public JsonResponse test(){

        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            return JsonResponse.success();
        }else {
            return JsonResponse.error("未登录");
        }
    }
    @ResponseBody
    @PostMapping("/login")
    @ApiOperation("登录")
    public JsonResponse login(String username, String password){
        Subject subject = SecurityUtils.getSubject();
        System.out.println(username + " " + password);
        try{
            subject.login(new UsernamePasswordToken(username,password));
        }catch (UnknownAccountException e){
            return JsonResponse.error("用户名错误");
        }catch (IncorrectCredentialsException e){
            return JsonResponse.error("密码错误");
        }
        return JsonResponse.success();
    }
}
