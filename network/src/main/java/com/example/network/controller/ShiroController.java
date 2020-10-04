package com.example.network.controller;

import com.example.network.service.ShiroService;
import com.example.network.service.StudentService;
import com.example.network.vo.JsonResponse;
import com.example.network.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Controller
@Api(value = "学生操作",tags = "学生操作")
public class ShiroController {

    @Autowired
    StudentService studentService;

    @Autowired
    ShiroService shiroService;

    @ResponseBody
    @PostMapping("/testLog")
    @ApiOperation("测试是否登录")
    @RolesAllowed("123")
    public JsonResponse test(@RequestBody String id,@RequestBody String password){
        System.out.println(id + " " + password);
        JsonResponse success = JsonResponse.success();
        success.put("data",id + " " + password);
        return success;
    }
    @ResponseBody
    @PostMapping("/login")
    @ApiOperation("登录")
    public JsonResponse login(@RequestBody String id, @RequestBody String password){
        if (StringUtils.isEmpty(id)){
            return JsonResponse.invalidParam("学号为空！");
        }
        if (StringUtils.isEmpty(password)){
            return JsonResponse.invalidParam("密码为空！");
        }
        try{
            return shiroService.login(id,password);
        }catch (Exception e){
            return JsonResponse.unknownError();
        }
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

    @ResponseBody
    @PostMapping("/register")
    @ApiOperation("注册")
    public JsonResponse register(User user){
        if (StringUtils.isEmpty(user.getId())){
            return JsonResponse.invalidParam("学号为空！");
        }
        if (shiroService.checkExist(user.getId()) > 0){
            return JsonResponse.invalidParam("学号" + user.getId() + "已存在！");
        }
        if (StringUtils.isEmpty(user.getPassword())){
            return JsonResponse.invalidParam("密码为空！");
        }
        if (!"admin".equals(user.getRole()) && !"student".equals(user.getRole())){
            return JsonResponse.invalidParam("角色参数错误！");
        }
        try{
            return shiroService.register(user);
        }catch (Exception e){
            return JsonResponse.unknownError();
        }
    }
}
