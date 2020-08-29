package com.study.boot.Redis.model.mapper;

import com.study.boot.Redis.model.entity.UserOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserOrder record);

    int insertSelective(UserOrder record);

    UserOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOrder record);

    int updateByPrimaryKey(UserOrder record);

    List<UserOrder> selectUnPayOrders();

    int unActiveOrder(@Param("id") Integer id);
}