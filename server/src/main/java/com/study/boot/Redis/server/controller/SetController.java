package com.study.boot.Redis.server.controller;

import cn.hutool.core.util.StrUtil;
import com.study.boot.Redis.api.response.BaseResponse;
import com.study.boot.Redis.api.response.StatusCode;
import com.study.boot.Redis.model.entity.Notice;
import com.study.boot.Redis.model.entity.Problem;
import com.study.boot.Redis.model.mapper.ProblemMapper;
import com.study.boot.Redis.server.services.ProblemService;
import com.study.boot.Redis.server.services.SetService;
import com.study.boot.Redis.server.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @ClassName SetController
 * @Description: TODO
 * @Author lxl
 * @Date 2020/9/3
 * @Version V1.0
 **/
@Controller
@RequestMapping("set")
public class SetController extends AbstractController{

    @Autowired
    private SetService setService;

    @Autowired
    private ProblemService problemService;
    //获取列表详情
    @RequestMapping(value = "random",method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse get(final Integer total){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            Set<Problem> problems = problemService.getRandomProblem(total);
            response.setData(problems);
        }catch (Exception e){
            log.error("--List实战-商户商品-获取列表-发生异常：",e.fillInStackTrace());
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


    //平台管理员添加通知公告信息并发送通知给到各位商户
    @RequestMapping(value = "notice/put",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BaseResponse putNotice(@RequestBody @Validated Notice notice, BindingResult result){
        String checkRes= ValidatorUtil.checkResult(result);
        if (StrUtil.isNotBlank(checkRes)){
            return new BaseResponse(StatusCode.Fail.getCode(),checkRes);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("--平台发送通知给到各位商户：{}",notice);


        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}
