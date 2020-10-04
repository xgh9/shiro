package com.example.network.controller;

import com.example.network.service.ShiroService;
import com.example.network.service.StudentService;
import com.example.network.vo.JsonResponse;
import com.example.network.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
import java.util.Map;

@Controller
@Api(value = "学生操作",tags = "学生操作")
public class ShiroController {

    @Autowired
    StudentService studentService;

    @Autowired
    ShiroService shiroService;

    @ResponseBody
    @PostMapping("/test")
    @ApiOperation("测试权限")
    public JsonResponse test(@RequestBody int id){
        if (id == 1){
            System.out.println(shiroService.checkAuthentication());
        }else {
            System.out.println(shiroService.checkAuthorization());
        }
        JsonResponse success = JsonResponse.success();
        return success;
    }
    @ResponseBody
    @PostMapping("/login")
    @ApiOperation("登录")
    public JsonResponse login(@RequestBody Map<String, String> params){
        String id = params.get("id");
        String password = params.get("password");
        if (StringUtils.isEmpty(id)){
            return JsonResponse.invalidParam("学号呢？");
        }
        if (StringUtils.isEmpty(password)){
            return JsonResponse.invalidParam("密码呢？");
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
            return JsonResponse.invalidParam("学号呢？");
        }
        if (shiroService.checkExist(user.getId()) > 0){
            return JsonResponse.invalidParam("学号" + user.getId() + "已经被别人抢先一步使用了，如果这是你的学号，快到助教这来找回账户！");
        }
        if (StringUtils.isEmpty(user.getPassword())){
            return JsonResponse.invalidParam("密码呢？");
        }
        if (!"admin".equals(user.getRole()) && !"student".equals(user.getRole())){
            return JsonResponse.invalidParam("不接受你这种角色！");
        }
        try{
            return shiroService.register(user);
        }catch (Exception e){
            return JsonResponse.unknownError();
        }
    }
}
