package com.study.boot.Redis.model.mapper;

import com.study.boot.Redis.model.entity.Item;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Item record);

    int insertSelective(Item record);

    Item selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);
}