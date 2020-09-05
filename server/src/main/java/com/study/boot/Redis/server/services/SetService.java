package com.study.boot.Redis.server.services;

import com.study.boot.Redis.model.mapper.ProblemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName SetService
 * @Description: TODO
 * @Author lxl
 * @Date 2020/9/3
 * @Version V1.0
 **/
@Service
public class SetService {

    @Autowired
    private ProblemMapper problemMapper;


}
