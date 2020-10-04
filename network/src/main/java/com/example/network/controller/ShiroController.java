package com.example.network.controller;

import com.example.network.service.ShiroService;
import com.example.network.service.StudentService;
import com.example.network.vo.ConstantField;
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

import java.util.Map;

@Controller
@Api(value = "学生操作",tags = "学生操作")
public class ShiroController {

    @Autowired
    StudentService studentService;

    @Autowired
    ShiroService shiroService;

    @ResponseBody
    @GetMapping("/test")
    @ApiOperation("测试权限")
    public JsonResponse test(){
        boolean falgLog = shiroService.checkAuthentication();
        boolean flagAdmin = shiroService.checkAdminPermission();
        JsonResponse success = JsonResponse.success();
        success.put("登陆状态",falgLog);
        success.put("管理员权限",flagAdmin);
        return success;
    }
    @ResponseBody
    @PostMapping("/login")
    @ApiOperation("登录")
    public JsonResponse login(@RequestBody Map<String, String> params){
        String id = params.get(ConstantField.ID);
        String password = params.get(ConstantField.PASSWORD);
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
    @GetMapping("/logout")
    @ApiOperation("注销")
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
        //role为空默认为学生角色
        if (StringUtils.isEmpty(user.getRole())){
            user.setRole(ConstantField.ROLE_STUDENT);
        }
        //创建管理员账号需要管理员权限
        if (ConstantField.ROLE_ADMIN.equals(user.getRole())){
            if (!shiroService.checkAdminPermission()){
                return JsonResponse.noAuthority();
            }
        }
        if (!ConstantField.ROLE_ADMIN.equals(user.getRole()) && !ConstantField.ROLE_STUDENT.equals(user.getRole())){
            return JsonResponse.invalidParam("不接受你这种角色！");
        }
        try{
            return shiroService.register(user);
        }catch (Exception e){
            return JsonResponse.unknownError();
        }
    }

    @ResponseBody
    @PostMapping("/changePassword")
    @ApiOperation("修改密码")
    public JsonResponse changePassowrd(@RequestBody Map<String, String> params){
        if (!shiroService.checkAuthentication()){
            return JsonResponse.noLogError();
        }
        String oldPassword = params.get(ConstantField.OLD_PASSWORD);
        String password = params.get(ConstantField.PASSWORD);

        if (StringUtils.isEmpty(oldPassword)){
            return JsonResponse.invalidParam("旧密码呢？");
        }
        if (StringUtils.isEmpty(password)){
            return JsonResponse.invalidParam("新密码呢？");
        }

        try{
            return shiroService.changePassowrd(oldPassword,password);
        }catch (Exception e){
            return JsonResponse.unknownError();
        }
    }

    @ResponseBody
    @GetMapping("/getRole")
    @ApiOperation("获取当前用户角色")
    public JsonResponse getRole(){
        try{
            String role = shiroService.getRole();
            JsonResponse success = JsonResponse.success();
            success.put("role",role);
            return success;
        }catch (Exception e){
            return JsonResponse.unknownError();
        }

    }
}
