package com.example.network.controller;

import com.example.network.service.ShiroService;
import com.example.network.util.ShiroUtils;
import com.example.network.vo.ConstantField;
import com.example.network.vo.JsonResponse;
import com.example.network.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 不需要任何权限，即游客身份可以访问的页面
 */
@RestController
@Api(tags = "游客操作")
public class GuestController {

    @Autowired
    ShiroService shiroService;

    @ApiOperation("后端处理未登录异常用的")
    @GetMapping(value = "/notLogin")
    public JsonResponse notLogin() {
        return JsonResponse.noLogError();
    }

    @ApiOperation("后端处理权限异常用的")
    @GetMapping(value = "/notAdmin")
    public JsonResponse notAdmin() {
        return JsonResponse.noAuthority();
    }

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
        return shiroService.login(id,password);
    }

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
        //数据库没有管理员账号时提供注册管理员账号的方法
        if (ConstantField.ROLE_ADMIN.equals(user.getRole())){
            if (shiroService.checkAdmin() == 0){
                return shiroService.register(user);
            }else {
                return JsonResponse.invalidParam("数据库中已有管理员账号，若丢失密码可以重新导入sql文件或手动将MD5盐值加密并hash10次的密码写入数据库！");
            }
        }
        user.setRole(ConstantField.ROLE_STUDENT);
        return shiroService.register(user);
    }

    @GetMapping("/test")
    @ApiOperation("测试权限")
    public JsonResponse test(){
        boolean falgLog = ShiroUtils.checkAuthentication();
        boolean flagAdmin = ShiroUtils.checkAdminPermission();
        JsonResponse success = JsonResponse.success();
        success.put("登陆状态",falgLog);
        success.put("管理员权限",flagAdmin);
        Subject subject = SecurityUtils.getSubject();
        boolean remembered = subject.isRemembered();
        success.put("记住我",remembered);
        return success;
    }




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
