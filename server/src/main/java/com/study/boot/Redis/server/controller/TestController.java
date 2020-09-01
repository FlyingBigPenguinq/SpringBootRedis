package com.study.boot.Redis.server.controller;

import cn.hutool.core.date.DateTime;
import com.study.boot.Redis.api.response.BaseResponse;
import com.study.boot.Redis.api.response.StatusCode;
import com.study.boot.Redis.model.entity.Item;
import com.study.boot.Redis.model.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName TestController
 * @Description: TODO
 * @Author lxl
 * @Date 2020/8/31
 * @Version V1.0
 **/
@Controller
@RequestMapping("test")
public class TestController {
    @Autowired
    private ItemMapper itemMapper;

    @RequestMapping("test")
    public BaseResponse addtest(){
        Item item = new Item();
        item.setName("测试");
        item.setCode("xeshi");
        item.setCreateTime(DateTime.now());
        itemMapper.insertSelective(item);
        return new BaseResponse(StatusCode.Success);
    }
}
