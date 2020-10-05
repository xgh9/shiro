package com.example.network.exception;

import com.example.network.vo.JsonResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@ResponseBody
public class MyExceptionHandler {

    @ExceptionHandler(value = {org.apache.shiro.authz.UnauthenticatedException.class})
    public JsonResponse notLoginError(Exception e){
        return JsonResponse.noLogError();
    }

    @ExceptionHandler(value = {AuthorizationException.class})
    public JsonResponse notAdminError(Exception e){
        return JsonResponse.noAuthority();
    }

    @ExceptionHandler(value = {Exception.class})
    public JsonResponse error(Exception e){
        return JsonResponse.unknownError();
    }
}
