package com.example.network.vo;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class JsonResponse extends HashMap<String,Object>{
    public static Map<Integer,String> error = Maps.newHashMap();

    public static final String ERROR_MSG = "errorMsg";

    public static final String ERROR_CODE = "errorCode";


    public static JsonResponse error(int code, String msg){
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.put(ERROR_CODE,code);
        jsonResponse.put(ERROR_MSG,msg);
        return jsonResponse;
    }
    public static JsonResponse success(){
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.put(ERROR_CODE,0);
        jsonResponse.put(ERROR_MSG,"操作成功");
        return jsonResponse;
    }

    public static JsonResponse noLogError(){
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.put(ERROR_CODE,1);
        jsonResponse.put(ERROR_MSG,"未登录");
        return jsonResponse;
    }

    public static JsonResponse noAuthority(){
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.put(ERROR_CODE,2);
        jsonResponse.put(ERROR_MSG,"没有权限");
        return jsonResponse;
    }

    public static JsonResponse invalidParam(){
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.put(ERROR_CODE,3);
        jsonResponse.put(ERROR_MSG,"参数错误");
        return jsonResponse;
    }

    public static JsonResponse invalidParam(String msg){
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.put(ERROR_CODE,3);
        jsonResponse.put(ERROR_MSG,msg);
        return jsonResponse;
    }

    public static JsonResponse repeatLogin(String username){
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.put(ERROR_CODE,4);
        jsonResponse.put(ERROR_MSG,"用户"+ username + "已登录，若要切换用户请先退出");
        return jsonResponse;
    }

    public static JsonResponse unknownError(){
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.put(ERROR_CODE,10);
        jsonResponse.put(ERROR_MSG,"未知错误");
        return jsonResponse;
    }
}
