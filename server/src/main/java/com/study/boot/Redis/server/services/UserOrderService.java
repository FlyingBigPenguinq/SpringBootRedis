package com.study.boot.Redis.server.services;

import cn.hutool.core.lang.Snowflake;
import com.study.boot.Redis.model.entity.UserOrder;
import com.study.boot.Redis.model.mapper.UserOrderMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**用户下单service
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2020/1/3 13:30
 **/
@EnableScheduling
@Service
public class UserOrderService {

    private static final Logger log= LoggerFactory.getLogger(UserOrderService.class);


    //雪花算法工具-用于分布式环境（高并发）生成全局唯一ID的工具
    private static final Snowflake SNOWFLAKE=new Snowflake(3,2);

    //存储至缓存的用户订单编号的前缀
    private static final String RedisUserOrderPrefix="SpringBootRedis:UserOrder:";

    //用户订单失效的时间配置 - 30s
    private static final Long UserOrderTimeOut=30L;


    @Autowired
    private UserOrderMapper userOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    //下单服务
    @Transactional(rollbackFor = Exception.class)
    public String putOrder(UserOrder entity) throws Exception{
        //用户下单-入库
        String orderNo=SNOWFLAKE.nextIdStr();
        entity.setOrderNo(orderNo);
        entity.setOrderTime(new Date());
        int res=userOrderMapper.insertSelective(entity);
        if (res>0){
            //TODO:插入db之后，将订单编号塞入缓存中，同时设置好ttl
            redisTemplate.opsForValue().set(RedisUserOrderPrefix+orderNo,entity.getId(),UserOrderTimeOut,TimeUnit.SECONDS);
        }
        return orderNo;
    }


    //TODO：定时任务调度-拉取出 有效 + 未支付 的订单列表，前往缓存查询订单是否已失效
    @Scheduled(cron = "0/30 * * * * ?")
    @Async("threadPoolTaskExecutor")
    public void schedulerCheckOrder(){
        try {
            List<UserOrder> orders=userOrderMapper.selectUnPayOrders();
            if (orders!=null && !orders.isEmpty()){
                orders.forEach(entity -> {
                    String key=RedisUserOrderPrefix+entity.getOrderNo();
                    if (!redisTemplate.hasKey(key)){
                        //TODO:表示缓存中该订单编号已经过期失效了，我们需要前往数据库失效该订单...
                        userOrderMapper.unActiveOrder(entity.getId());

                        log.info("----缓存中当前订单超过了TTL未支付，故而失效该表中的对应记录：orderNo={}",entity.getOrderNo());
                    }
                });
            }
        }catch (Exception e){
            log.error("定时任务调度-拉取出 有效 + 未支付 的订单列表，前往缓存查询订单是否已失效-发生异常：",e);
        }
    }


}