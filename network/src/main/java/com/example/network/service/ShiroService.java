package com.example.network.service;

import com.example.network.vo.JsonResponse;
import com.example.network.vo.User;

public interface ShiroService {

    JsonResponse login(String id, String password);

    JsonResponse logout();

    JsonResponse register(User user);

    int checkExist(String id);

    User getUserById(String id);
}
