package com.study.boot.Redis.server.controller;

import com.study.boot.Redis.api.response.BaseResponse;
import com.study.boot.Redis.api.response.StatusCode;
import com.study.boot.Redis.model.entity.Item;
import com.study.boot.Redis.server.services.StringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName StringController
 * @Description: TODO: 商品对象信息管理　-　缓存
 * @Author lxl
 * @Date 2020/8/30
 * @Version V1.0
 **/
@RequestMapping("string")
@Controller
public class StringController extends AbstractController{


    @Autowired
    private StringService stringService;

    @ResponseBody
    @RequestMapping(value = "add",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public BaseResponse add(@Validated @RequestBody Item item, BindingResult result){
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        log.info(item.toString());
        if (result.hasErrors()){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        try{
            stringService.addItem(item);
        }catch (Exception e){
            log.info("----新增缓存异常----");
            baseResponse = new BaseResponse(StatusCode.InvalidParams);
        }
        return baseResponse;
    }

    /**
     * @MethodName: get
     * @Description: TODO
     * @Param: []
     * @Return: com.study.boot.Redis.model.entity.Item
     * @Author: lxl
     * @Date: 下午3:23
    **/
    @ResponseBody
    @RequestMapping("getItem/{id}")
    public BaseResponse get(@PathVariable("id") Integer id){
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        try{
            Item item = stringService.getItem(id);
            baseResponse.setData(item);
        }catch (Exception e){
            log.info("----获取缓存异常----");
            baseResponse = new BaseResponse(StatusCode.InvalidParams);
        }
        return baseResponse;
    }


    @ResponseBody
    @RequestMapping(value = "updateItem", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse update(@RequestBody @Validated Item item, BindingResult result){
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        log.info(item.toString());
        if (result.hasErrors()){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        try{
            stringService.updateItem(item);
        }catch (Exception e){
            log.info("----更新缓存异常----");
            baseResponse = new BaseResponse(StatusCode.InvalidParams);
        }
        return baseResponse;
    }

    @ResponseBody
    @RequestMapping("deleteItem/{id}")
    public BaseResponse delete(@PathVariable("id") Integer id){
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        try{
            Integer res = stringService.deleteItem(id);
            baseResponse.setData(res);
        }catch (Exception e){
            log.info("----删除缓存异常----");
            baseResponse = new BaseResponse(StatusCode.InvalidParams);
        }
        return baseResponse;
    }
}
