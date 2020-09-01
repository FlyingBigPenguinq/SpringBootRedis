package com.study.boot.Redis.server.services;

import cn.hutool.core.date.DateTime;
import com.study.boot.Redis.model.entity.Item;
import com.study.boot.Redis.model.mapper.ItemMapper;
import com.study.boot.Redis.server.MainApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class StringServiceTest {

    @Autowired
    private ItemMapper itemMapper;
    @Test
    public void addItem() {
        Item item = new Item();
        item.setCreateTime(DateTime.now());
        item.setCode("ceshi");
        item.setName("测试");
        itemMapper.insertSelective(item);
    }
}