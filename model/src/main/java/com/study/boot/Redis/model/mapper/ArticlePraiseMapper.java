package com.study.boot.Redis.model.mapper;

import com.study.boot.Redis.model.entity.ArticlePraise;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticlePraiseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ArticlePraise record);

    int insertSelective(ArticlePraise record);

    ArticlePraise selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArticlePraise record);

    int updateByPrimaryKey(ArticlePraise record);

    List<ArticlePraise> selectAll();

    int cancelPraise(@Param("articleId") Integer articleId, @Param("userId") Integer userId);
}