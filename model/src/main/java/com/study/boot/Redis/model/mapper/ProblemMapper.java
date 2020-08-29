package com.study.boot.Redis.model.mapper;



import com.study.boot.Redis.model.entity.Problem;

import java.util.Set;

public interface ProblemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Problem record);

    int insertSelective(Problem record);

    Problem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Problem record);

    int updateByPrimaryKey(Problem record);

    Set<Problem> getAll();
}