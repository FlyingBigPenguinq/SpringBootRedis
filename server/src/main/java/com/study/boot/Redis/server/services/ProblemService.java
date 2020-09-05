package com.study.boot.Redis.server.services;

import com.google.common.collect.Sets;
import com.study.boot.Redis.model.entity.Problem;
import com.study.boot.Redis.model.mapper.ProblemMapper;
import com.study.boot.Redis.server.enums.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @ClassName ProblemService
 * @Description: TODO: 在项目成功启动后的时机加载到Set中
 * @Author lxl
 * @Date 2020/9/3
 * @Version V1.0
 **/
@Service
public class ProblemService implements CommandLineRunner {


    private static final Logger log = LoggerFactory.getLogger(ProblemService.class);

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    /***
     * @MethodName: init
     * @Description: TODO: 在项目启动过程中的时机发生
     * @Param: []
     * @Return: void
     * @Author: lxl
     * @Date: 下午10:21
    **/
    /*@PostConstruct
    public void init(){

    }*/


    @Override
    public void run(String... args) throws Exception {
        log.info("项目成功启动之后，开始执行的过程　　---　ProblemService");
        this.initProblemToCache();
    }

    /**
     * @MethodName: initProblemToCache
     * @Description: TODO:将DB中的问题加入到Redis当中
     * @Param: []
     * @Return: void
     * @Author: lxl
     * @Date: 下午10:26
    **/
    public void initProblemToCache(){
        try {
            //防止当机重启的时候　重复添加
            redisTemplate.delete(Constant.RedisSetKey);
            Set<Problem> problems = problemMapper.getAll();

            if (problems != null && !problems.isEmpty()){
                SetOperations setOperations = redisTemplate.opsForSet();
                problems.forEach(problem -> setOperations.add(Constant.RedisSetKey,problem));
            }
        }catch (Exception e){
            log.info("初始化添加Problem 出现异常　　　----ProblemService");
        }
    }

    /**
     * @MethodName:
     * @Description: TODO:获取total个随机的Problem
     * @Param:
     * @Return:
     * @Author: lxl
     * @Date: 下午10:41
    **/
    public Set<Problem> getRandomProblem(final Integer total){
        Set<Problem> problems = Sets.newHashSet();
        try{
            SetOperations setOperations = redisTemplate.opsForSet();
            problems = setOperations.distinctRandomMembers(Constant.RedisSetKey,total);
        }catch (Exception e){

        }
        return problems;
    }
}
