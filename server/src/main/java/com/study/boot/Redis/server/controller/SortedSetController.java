package com.study.boot.Redis.server.controller;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.study.boot.Redis.api.response.BaseResponse;
import com.study.boot.Redis.api.response.StatusCode;
import com.study.boot.Redis.model.entity.PhoneFare;
import com.study.boot.Redis.server.services.SortedSetService;
import com.study.boot.Redis.server.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 数据类型为SortedSet - list + set的结合体 - 可用于实现不重复的、有序的元素存储
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2020/3/10 15:10
 **/
@RestController
@RequestMapping("sorted/set")
public class SortedSetController extends AbstractController {

    @Autowired
    private SortedSetService sortedSetService;

    //充值
    @RequestMapping(value = "put",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse put(@RequestBody @Validated PhoneFare fare, BindingResult result){
        String checkRes= ValidatorUtil.checkResult(result);
        if (StrUtil.isNotBlank(checkRes)){
            return new BaseResponse(StatusCode.Fail.getCode(),checkRes);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            response.setData(sortedSetService.addRecord(fare));

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //获取排行榜
    @RequestMapping(value = "rank",method = RequestMethod.GET)
    public BaseResponse get(@RequestParam String phone){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap=Maps.newHashMap();
        try {
            resMap.put("RankInfo-正序",sortedSetService.getSortFares());
            resMap.put("RankInfo-倒序",sortedSetService.getSortFaresV2());
            resMap.put("PhoneFare-获取具体的充值金额",sortedSetService.getFareByPhone(phone));


        }catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

}






















