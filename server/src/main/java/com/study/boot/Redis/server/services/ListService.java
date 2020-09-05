package com.study.boot.Redis.server.services;

import com.study.boot.Redis.model.entity.Notice;
import com.study.boot.Redis.model.entity.Product;
import com.study.boot.Redis.model.mapper.NoticeMapper;
import com.study.boot.Redis.model.mapper.ProductMapper;
import com.study.boot.Redis.server.enums.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName ListService
 * @Description: TODO
 * @Author lxl
 * @Date 2020/9/1
 * @Version V1.0
 **/
@Service
public class ListService {
    private static final Logger log = LoggerFactory.getLogger(ListService.class);
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    public Integer addProduct(Product product){
        product.setId(null);
        Integer res = productMapper.insertSelective(product);
        if (res > 0){
            ListOperations listOperations = redisTemplate.opsForList();
            listOperations.leftPush(Constant.RedisListPrefix + product.getUserId(), product);
        }
        return 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Product> getHistoryProducts(final Integer userId){
        final String key = Constant.RedisListPrefix + userId;
        ListOperations listOperations = redisTemplate.opsForList();
        List<Product> resList;
        resList = listOperations.range(key,0L, listOperations.size(key));
        return resList;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer pushNotice(Notice notice){
        notice.setId(null);
        Integer res = noticeMapper.insertSelective(notice);
        if (res > 0){
            ListOperations<String, Notice> listOperations = redisTemplate.opsForList();
            //向缓存内的消息队列添加消息
            listOperations.leftPush(Constant.RedisListNoticeKey, notice);
        }
        return 1;
    }
}
