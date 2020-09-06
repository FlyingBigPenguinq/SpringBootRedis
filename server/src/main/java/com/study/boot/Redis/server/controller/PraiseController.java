package com.study.boot.Redis.server.controller;

import com.study.boot.Redis.api.response.BaseResponse;
import com.study.boot.Redis.api.response.StatusCode;
import com.study.boot.Redis.model.dto.PraiseDto;
import com.study.boot.Redis.server.services.PraiseService;
import com.study.boot.Redis.server.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 点赞模块
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2020/3/11 9:49
 **/
@RestController
@RequestMapping("praise/article")
public class PraiseController extends AbstractController {

    @Autowired
    private PraiseService praiseService;


    //获取文章列表
    @GetMapping("list")
    public BaseResponse articleList(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            response.setData(praiseService.getAll());

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


    //点赞文章
    @RequestMapping(value = "on",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse praiseOn(@RequestBody @Validated PraiseDto dto, BindingResult result){
        String checkRes= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(checkRes)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkRes);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            response.setData(praiseService.praiseOn(dto));

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


    //取消点赞文章
    @RequestMapping(value = "cancel",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse praiseCancel(@RequestBody @Validated PraiseDto dto, BindingResult result){
        String checkRes= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(checkRes)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkRes);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            response.setData(praiseService.praiseCancel(dto));

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //获取文章详情~排行榜
    @RequestMapping(value = "info",method = RequestMethod.GET)
    public BaseResponse articleInfo(@RequestParam Integer articleId, Integer currUserId){
        if (articleId==null || articleId<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            response.setData(praiseService.getArticleInfo(articleId,currUserId));

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //获取用户点赞过的历史文章-用户详情
    @RequestMapping(value = "user/articles",method = RequestMethod.GET)
    public BaseResponse userArticles(@RequestParam Integer currUserId){
        if (currUserId==null || currUserId<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            response.setData(praiseService.getUserArticles(currUserId));

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


}





























