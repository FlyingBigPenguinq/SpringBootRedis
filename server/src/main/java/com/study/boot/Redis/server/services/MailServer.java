package com.study.boot.Redis.server.services;

import com.study.boot.Redis.model.entity.Notice;
import com.study.boot.Redis.model.entity.User;
import org.springframework.stereotype.Service;

/**
 * @ClassName MailServer
 * @Description: TODO
 * @Author lxl
 * @Date 2020/9/3
 * @Version V1.0
 **/
@Service
public class MailServer<T> {
    public void sendMail(T... t) throws InterruptedException {
        Thread.sleep(100);
        System.out.println("发送邮件");
    }
}
