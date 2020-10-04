package com.example.network.service;

import com.example.network.vo.JsonResponse;
import com.example.network.vo.User;

public interface ShiroService {

    //登录
    JsonResponse login(String id, String password);

    //注册
    JsonResponse register(User user);

    //学号验重
    int checkExist(String id);

    //根据学号获取user
    User getUserById(String id);

    //检查是否登录
    boolean checkAuthentication();

    //检查是否有管理员权限
    boolean checkAuthorization();
}
