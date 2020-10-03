package com.example.network.controller;

import com.example.network.vo.JsonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(value = "学生操作",tags = "学生操作")
public class StudentController {

    @ResponseBody
    @GetMapping("/test")
    @ApiOperation("测试")
    public JsonResponse test(){
        JsonResponse success = JsonResponse.success();
        success.put("1","asfasdf");
        return success;
    }
}
