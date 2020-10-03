package com.example.network.vo;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class JsonResponse extends HashMap<String,Object>{
    public static Map<Integer,String> error = Maps.newHashMap();

    public static final String ERROR_MSG = "erroMsg";

    public static final String SUCCESS = "操作成功";

    public static final String  NOT_LOG= "未登录";

    public static final String  NO_AUTHORITY= "没有权限";

    public static final String  INVALID_PARAM = "参数错误";

    public static final String  UNKNOWN_ERROR = "未知错误";

    public static JsonResponse error(String msg){
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.put(ERROR_MSG,msg);
        return jsonResponse;
    }
    public static JsonResponse success(){
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.put(ERROR_MSG,SUCCESS);
        return jsonResponse;
    }
}
