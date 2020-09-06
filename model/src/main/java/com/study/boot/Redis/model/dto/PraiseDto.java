package com.study.boot.Redis.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @ClassName PraiseDto
 * @Description: TODO
 * @Author lxl
 * @Date 2020/9/6
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
public class PraiseDto {

    //当前用户id
    @NotNull(message = "当前用户id不能为为空！")
    private Integer userId;

    //文章id
    @NotNull(message = "当前文章id不能为为空！")
    private Integer articleId;

    //文章标题 ~ 开发技巧 ~ 服务于排行榜(如微博的热搜，只显示其标题，而不需要再根据id查询db获取标题...)
    @NotNull(message = "当前文章标题不能为空！")
    private String title;

    public PraiseDto(Integer currUserId, Integer valueOf, String value) {
        this.userId = currUserId;
        this.articleId = valueOf;
        this.title = value;
    }
}
