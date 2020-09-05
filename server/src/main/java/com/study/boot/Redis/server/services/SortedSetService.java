package com.study.boot.Redis.server.services;

import com.study.boot.Redis.model.entity.PhoneFare;
import com.study.boot.Redis.model.mapper.PhoneFareMapper;
import com.study.boot.Redis.server.enums.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testng.collections.Lists;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @ClassName SortedSetService
 * @Description: TODO
 * @Author lxl
 * @Date 2020/9/4
 * @Version V1.0
 **/
@Service
public class SortedSetService {
    private static final Logger log= LoggerFactory.getLogger(SortedSetService.class);

    @Autowired
    private PhoneFareMapper fareMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    //TODO:新增 手机话费充值记录
    @Transactional(rollbackFor = Exception.class)
    public Integer addRecord(PhoneFare fare) throws Exception{
        log.info("----sorted set话费充值记录新增：{} ",fare);

        /*fare.setId(null);
        //先插db
        int res=fareMapper.insertSelective(fare);
        if (res>0){
            //再插cache
            ZSetOperations<String,String> zSetOperations=redisTemplate.opsForZSet();

            //TODO:往sortedSet添加一条充值记录：手机号为value;充值金额为score
            zSetOperations.add(Constant.RedisSortedSetKey2,fare.getPhone(),fare.getFare().doubleValue());
        }
        return fare.getId();*/



        fare.setId(null);
        //先插db
        int res=fareMapper.insertSelective(fare);
        if (res>0){
            //再插cache
            ZSetOperations<String,String> zSetOperations=redisTemplate.opsForZSet();

            //TODO:插入cache之前，需要判断一下cache里面是否有该手机号的充值记录，如果有，则叠加；如果没有，则直接插入(第一次)
            Double currFare=zSetOperations.score(Constant.RedisSortedSetKey2,fare.getPhone());
            if (currFare!=null){
                zSetOperations.incrementScore(Constant.RedisSortedSetKey2,fare.getPhone(),fare.getFare().doubleValue());
            }else{
                zSetOperations.add(Constant.RedisSortedSetKey2,fare.getPhone(),fare.getFare().doubleValue());
            }
        }
        return fare.getId();


    }

    //TODO:获取充值排行榜-正序
    public List<PhoneFare> getSortFares(){
        List<PhoneFare> list= Lists.newLinkedList();

        ZSetOperations<String,String> zSetOperations=redisTemplate.opsForZSet();
        Long total=zSetOperations.size(Constant.RedisSortedSetKey2);
        //得到了排好序的、带分数的成员
        Set<ZSetOperations.TypedTuple<String>> set=zSetOperations.rangeWithScores(Constant.RedisSortedSetKey2,0L,total);
        if (set!=null && !set.isEmpty()){
            set.forEach(tuple -> list.add(new PhoneFare(tuple.getValue(),BigDecimal.valueOf(tuple.getScore()))));
        }

        return list;
    }


    //TODO:获取充值排行榜-倒序
    public List<PhoneFare> getSortFaresV2(){
        List<PhoneFare> list= Lists.newLinkedList();

        ZSetOperations<String,String> zSetOperations=redisTemplate.opsForZSet();
        Long total=zSetOperations.size(Constant.RedisSortedSetKey2);
        //得到了排好序的、带分数的成员
        Set<ZSetOperations.TypedTuple<String>> set=zSetOperations.reverseRangeWithScores(Constant.RedisSortedSetKey2,0L,total);
        if (set!=null && !set.isEmpty()){
            set.forEach(tuple -> list.add(new PhoneFare(tuple.getValue(), BigDecimal.valueOf(tuple.getScore()))));
        }

        return list;
    }

    //TODO:获取指定手机号的充值金额
    public Double getFareByPhone(final String phone) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.score(Constant.RedisSortedSetKey2, phone);
    }
}
