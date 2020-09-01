package com.study.boot.Redis.server.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.function.Consumer;

/**
 * @ClassName Validated
 * @Description: TODO
 * @Author lxl
 * @Date 2020/9/1
 * @Version V1.0
 **/
public class ValidatorUtil {

    public static String checkResult(BindingResult result){
        List<ObjectError> errors = result.getAllErrors();
        StringBuilder stringBuilder = new StringBuilder("");
        for (ObjectError error: errors) {
            stringBuilder.append(error.getDefaultMessage());
        }
        return stringBuilder.toString();
    }
}
