package com.study.boot.Redis.server.scheduler;

import com.study.boot.Redis.model.entity.Notice;
import com.study.boot.Redis.model.entity.User;
import com.study.boot.Redis.model.mapper.UserMapper;
import com.study.boot.Redis.server.enums.Constant;
import com.study.boot.Redis.server.services.MailServer;
import com.study.boot.Redis.server.thread.NoticeThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ListListenerScheduler
 * @Description: TODO
 * @Author lxl
 * @Date 2020/9/2
 * @Version V1.0
 **/
@Component
public class ListListenerScheduler {

    private static final Logger log = LoggerFactory.getLogger(ListListenerScheduler.class);
    @Autowired
    private RedisTemplate redisTemplate;

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailServer mailServer;

    @Scheduled(cron = "0 0/2 * * * ? ")
    public void listenNotice(){
        log.info("----开启实时监听list中的消息队列-----");
        ListOperations<String, Notice> listOperations = redisTemplate.opsForList();
        Notice notice = listOperations.rightPop(Constant.RedisListNoticeKey);
        while (notice != null){
            this.sendNoticeToUser(notice);
            notice = listOperations.rightPop(Constant.RedisListNoticeKey);
        }
    }

    public void sendNoticeToUser(Notice notice){
        if (threadPoolTaskExecutor == null){
            threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
            threadPoolTaskExecutor.setCorePoolSize(5);
            threadPoolTaskExecutor.setMaxPoolSize(10);
            threadPoolTaskExecutor.setQueueCapacity(6);
        }
        try {
            List<User> users = userMapper.selectList();
            if (users != null && !users.isEmpty()) {
                users.forEach(user -> threadPoolTaskExecutor.submit(new NoticeThread(notice, user, mailServer)));
            }
        }catch (Exception e){
            log.info("提交用户消息邮件时发生异常");
        }
    }
}
