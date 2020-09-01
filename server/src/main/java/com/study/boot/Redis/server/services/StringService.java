package com.study.boot.Redis.server.services;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.boot.Redis.model.entity.Item;
import com.study.boot.Redis.model.mapper.ItemMapper;
import com.study.boot.Redis.server.enums.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @ClassName StringService
 * @Description: TODO
 * @Author lxl
 * @Date 2020/8/30
 * @Version V1.0
 **/
@Service
public class StringService {

    private static final Logger log = LoggerFactory.getLogger(StringService.class);

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional(rollbackFor = Exception.class)
    public Integer addItem(Item item) {
        item.setCreateTime(DateTime.now());
        Integer res = itemMapper.insertSelective(item);
        //TODO:如果写入成功则往　缓存中也写入一份
        if (res > 0){
            try{
                redisTemplate.opsForValue().set(Constant.RedisStringPrefix + item.getId(), objectMapper.writeValueAsString(item));
            }catch (Exception e){
                log.info(e.toString());
            }
        }
        return item.getId();
    }

    public Item getItem(final Integer id) throws Exception {
        final String key = Constant.RedisStringPrefix + id;
        Item res = null;
        if (redisTemplate.hasKey(Constant.RedisStringPrefix + id)){
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj != null && StringUtils.isNotBlank(obj.toString())){
                res = objectMapper.readValue(obj.toString(),Item.class);
                return res;
            }
        }else {
            res = itemMapper.selectByPrimaryKey(id);
            if (res != null){
                redisTemplate.opsForValue().set(key,objectMapper.writeValueAsString(res));
            }else {
                redisTemplate.opsForValue().set(key,"");   //TODO:防止缓存穿透
            }
        }
        return res;
    }

    /**
     * @MethodName: updateItem
     * @Description: TODO: 更新缓存
     * @Param: [item]
     * @Return: java.lang.Integer
     * @Author: lxl
     * @Date: 上午7:33
    **/
    @Transactional(rollbackFor = Exception.class)
    public Integer updateItem(Item item) throws JsonProcessingException {
        int res = itemMapper.updateByPrimaryKeySelective(item);

        if (res > 0){
            redisTemplate.opsForValue().set(Constant.RedisStringPrefix + item.getId(), objectMapper.writeValueAsString(item));
        }
        return item.getId();
    }

    /**
     * @MethodName: updateItem
     * @Description: TODO: 删除一个缓存
     * @Param: [item]
     * @Return: java.lang.Integer
     * @Author: lxl
     * @Date: 下午2:25
    **/
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteItem(final Integer id){
        int res = itemMapper.deleteByPrimaryKey(id);
        if (res > 0){
            redisTemplate.delete(Constant.RedisStringPrefix + id);
        }
        return 1;
    }
}
