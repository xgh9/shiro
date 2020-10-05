package com.example.network.util;

import com.example.network.vo.ConstantField;
import com.example.network.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.Random;

public class ShiroUtils {
    static char[] chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPLKJHGFDSAZXCVBNM0123456789!@#$%^&*()".toCharArray();
    public static String getSalt(int k){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            char c = chars[new Random().nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static boolean checkAuthentication(){
        Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated();
    }

    public static boolean checkAdminPermission(){
        Subject subject = SecurityUtils.getSubject();
        return subject.hasRole(ConstantField.ROLE_ADMIN);
    }
}
