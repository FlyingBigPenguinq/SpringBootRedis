package com.study.boot.Redis.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName ArticlePraiseDto
 * @Description: TODO
 * @Author lxl
 * @Date 2020/9/7
 * @Version V1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ArticlePraiseRankDto implements Serializable {

    //文章id
    private String articleId;

    //文章标题
    private String title;

    //点赞总数
    private String total;

    private Double score;
}
