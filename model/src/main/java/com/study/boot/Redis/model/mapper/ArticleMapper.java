package com.study.boot.Redis.model.mapper;

import com.study.boot.Redis.model.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<Article> selectAll();

    int updatePraiseTotal(@Param("articleId") Integer articleId, @Param("flag") Integer flag);

    Article selectByPK(@Param("articleId") Integer articleId);
}