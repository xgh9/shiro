package com.example.network.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.network.config.JWTToken;
import com.example.network.service.ShiroService;
import com.example.network.service.ShiroServiceImpl;
import com.example.network.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Random;

@Component
public class JWTUtils {

    private static int expireTime;

    @Value("${shiro.expireTime}")
    public void setExpireTime(int expireTime){
        JWTUtils.expireTime = expireTime;
    }

    static char[] chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPLKJHGFDSAZXCVBNM0123456789!@#$%^&*()".toCharArray();

    /**
     * 生成token
     * @param user
     * @return
     */
    public static String sign(User user){
        try{
            Calendar date = Calendar.getInstance();
            date.add(Calendar.MINUTE,expireTime);
            String token = JWT.create()
                    .withClaim("id",user.getId())
                    .withClaim("name",user.getName())
                    .withClaim("role",user.getRole())
                    .withExpiresAt(date.getTime())
                    .sign(Algorithm.HMAC256(user.getSalt()));
            return token;
        }catch (UnsupportedEncodingException e){
            return null;
        }
    }

    /**
     * 验证token
     */
    public static boolean verify(String token, String salt){
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static User getClaim(String token){
        try {
            User user = new User();
            DecodedJWT decode = JWT.decode(token);
            user.setId(decode.getClaim("id").asString());
            user.setName(decode.getClaim("name").asString());
            user.setRole(decode.getClaim("role").asString());
            return user;
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    //生成随机盐
    public static String getSalt(int k){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            char c = chars[new Random().nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
    }
}
