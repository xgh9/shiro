package com.example.network.controller;

import com.example.network.service.ShiroService;
import com.example.network.util.ShiroUtils;
import com.example.network.vo.ConstantField;
import com.example.network.vo.JsonResponse;
import com.example.network.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 需要登录后才能操作的方法，admin和student都可以
 */
@RestController
@RequiresAuthentication
@Api(tags = "用户操作")
public class UserController {

    @Autowired
    ShiroService shiroService;

    @GetMapping("/logout")
    @ApiOperation("注销")
    public JsonResponse logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return JsonResponse.success();
    }

    @PostMapping("/changePassword")
    @ApiOperation("修改密码")
    public JsonResponse changePassowrd(@RequestBody Map<String, String> params){
        String oldPassword = params.get(ConstantField.OLD_PASSWORD);
        String password = params.get(ConstantField.PASSWORD);

        if (StringUtils.isEmpty(oldPassword)){
            return JsonResponse.invalidParam("旧密码呢？");
        }
        if (StringUtils.isEmpty(password)){
            return JsonResponse.invalidParam("新密码呢？");
        }
        return shiroService.changePassowrd(oldPassword,password);
    }
}
