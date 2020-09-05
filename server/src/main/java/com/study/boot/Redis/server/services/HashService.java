package com.study.boot.Redis.server.services;

import com.study.boot.Redis.model.entity.SysConfig;
import com.study.boot.Redis.model.mapper.SysConfigMapper;
import com.study.boot.Redis.server.redis.HashRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**hash数据类型-service
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2020/3/10 21:07
 **/
@Service
public class HashService {

    private static final Logger log= LoggerFactory.getLogger(HashService.class);

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private HashRedisService redisService;

    //TODO:添加数据字典及其对应的选项(code-value)
    @Transactional(rollbackFor = Exception.class)
    public Integer addSysConfig(SysConfig config) throws Exception{
        config.setId(null);
        int res=sysConfigMapper.insertSelective(config);
        if (res>0){
            //插入db成功之后，实时触发数据字典的hash存储
            redisService.cacheSysConfigs();
        }
        return config.getId();
    }

    //TODO:取出缓存中所有的数据字典列表
    public Map<String,List<SysConfig>> getAll() throws Exception{
        return redisService.getAllSysConfigs();
    }

    //TODO:取出缓存中特定的数据字典列表
    public List<SysConfig> getByType(final String type) throws Exception{
        return redisService.getSysConfigsByType(type);
    }
}




























