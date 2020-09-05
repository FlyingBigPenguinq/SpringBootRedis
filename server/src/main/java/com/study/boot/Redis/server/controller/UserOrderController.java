package com.study.boot.Redis.server.controller;

import cn.hutool.core.util.StrUtil;
import com.study.boot.Redis.api.response.BaseResponse;
import com.study.boot.Redis.api.response.StatusCode;
import com.study.boot.Redis.model.entity.UserOrder;
import com.study.boot.Redis.server.services.UserOrderService;
import com.study.boot.Redis.server.utils.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户下单controller
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2020/3/10 20:58
 **/
@RestController
@RequestMapping("user/order")
public class UserOrderController {

    private static final Logger log= LoggerFactory.getLogger(UserOrderController.class);

    @Autowired
    private UserOrderService userOrderService;


    //下单
    @RequestMapping(value = "put",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse put(@RequestBody @Validated UserOrder userOrder, BindingResult result){
        String checkRes= ValidatorUtil.checkResult(result);
        if (StrUtil.isNotBlank(checkRes)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkRes);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("--用户下单：{}",userOrder);

            String res=userOrderService.putOrder(userOrder);
            response.setData(res);
        }catch (Exception e){
            log.error("--用户下单-发生异常：",e.fillInStackTrace());
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

}

















































