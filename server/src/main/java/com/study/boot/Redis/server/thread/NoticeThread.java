package com.study.boot.Redis.server.thread;

import com.study.boot.Redis.model.entity.Notice;
import com.study.boot.Redis.model.entity.User;
import com.study.boot.Redis.server.services.MailServer;
import org.springframework.mail.MailSender;

import java.util.concurrent.Callable;

/**
 * @ClassName NoticeThread
 * @Description: TODO
 * @Author lxl
 * @Date 2020/9/3
 * @Version V1.0
 **/
public class NoticeThread implements Callable {

    private Notice notice;

    private User user;

    private MailServer mailServer;

    public NoticeThread(Notice notice, User user, MailServer mailServer) {
        this.notice = notice;
        this.user = user;
        this.mailServer = mailServer;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Boolean call() throws Exception {
        mailServer.sendMail(notice, user);
        return true;
    }
}
