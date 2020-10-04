package com.example.network.dao;

import com.example.network.vo.User;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    Integer insert(User user);

    List<User> getAllStudent();

    List<User> getAllAdmin();

    List<User> getAllUser();

    Integer checkExist(String id);

    User getUserById(String id);
}
