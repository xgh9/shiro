package com.example.network.vo;

import lombok.Data;

@Data
public class User {
    private String id;

    private String name;

    private String password;

    private String role;

    private String salt;
}
