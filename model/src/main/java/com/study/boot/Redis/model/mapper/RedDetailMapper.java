package com.study.boot.Redis.model.mapper;

import com.study.boot.Redis.model.entity.RedDetail;

public interface RedDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RedDetail record);

    int insertSelective(RedDetail record);

    RedDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RedDetail record);

    int updateByPrimaryKey(RedDetail record);
}