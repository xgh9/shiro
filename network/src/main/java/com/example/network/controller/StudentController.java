package com.example.network.controller;

import com.example.network.service.StudentService;
import com.example.network.vo.JsonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Controller
@Api(value = "学生操作",tags = "学生操作")
public class StudentController {

    @Autowired
    StudentService studentService;

    @ResponseBody
    @GetMapping("/testLog")
    @ApiOperation("测试是否登录")
    @RolesAllowed("123")
    public JsonResponse test(HttpSession session){{
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()){
            System.out.println(session.getAttribute(attributeNames.nextElement()));
        }
    }
        return JsonResponse.success();
    }
    @ResponseBody
    @PostMapping("/login")
    @ApiOperation("登录")
    public JsonResponse login(String username, String password){

        return studentService.login(username,password);
    }

    @ResponseBody
    @PostMapping("/logout")
    @ApiOperation("退出")
    public JsonResponse logout(){

        Subject subject = SecurityUtils.getSubject();
        try{
            subject.logout();
            return JsonResponse.success();
        }catch (Exception e){
            return JsonResponse.unknownError();
        }
    }
}
