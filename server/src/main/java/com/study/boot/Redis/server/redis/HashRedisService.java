package com.study.boot.Redis.server.redis;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.study.boot.Redis.model.entity.SysConfig;
import com.study.boot.Redis.model.mapper.SysConfigMapper;
import com.study.boot.Redis.server.enums.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2020/3/9 22:27
 **/
@Service
public class HashRedisService {

    private static final Logger log= LoggerFactory.getLogger(HashRedisService.class);

    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    //实时触发db中数据字典的缓存存储
    public void cacheSysConfigs(){
        try {

            //从db获取出所有
            List<SysConfig> list=configMapper.selectActiveConfigs();
            if (list!=null && !list.isEmpty()){
                //大类：type;小类: field-value (code-value) 对
                Map<String,List<SysConfig>> typeMap= Maps.newHashMap();

                list.forEach(config -> {
                    List<SysConfig> configs=typeMap.get(config.getType());
                    if (configs==null || configs.isEmpty()){
                        configs= Lists.newLinkedList();
                    }
                    configs.add(config);

                    typeMap.put(config.getType(),configs);
                });

                //TODO:存储至hash中
                HashOperations<String,String,List<SysConfig>> hashOperations=redisTemplate.opsForHash();
                hashOperations.putAll(Constant.RedisHashKeyConfig,typeMap);
            }
        }catch (Exception e){
            log.error("实时触发db中数据字典的缓存存储-发生异常：",e);
        }
    }

    //获取所有的数据类型
    public Map<String,List<SysConfig>> getAllSysConfigs(){
        Map<String,List<SysConfig>> resMap=Maps.newHashMap();
        try {
            HashOperations<String,String,List<SysConfig>> hashOperations=redisTemplate.opsForHash();
            resMap=hashOperations.entries(Constant.RedisHashKeyConfig);

        }catch (Exception e){
            log.error("获取所有的数据类型-发生异常：",e);
        }
        return resMap;
    }

    //根据数据类型编码获取特定的选项编码列表
    public List<SysConfig> getSysConfigsByType(final String type){
        List<SysConfig> list=Lists.newLinkedList();
        try {
            HashOperations<String,String,List<SysConfig>> hashOperations=redisTemplate.opsForHash();
            list=hashOperations.get(Constant.RedisHashKeyConfig,type);

        }catch (Exception e){
            log.error("根据数据类型编码获取特定的选项编码列表-发生异常：",e);
        }
        return list;
    }
}




































