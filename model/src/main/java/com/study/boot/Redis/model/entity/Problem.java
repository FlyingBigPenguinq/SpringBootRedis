package com.study.boot.Redis.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class Problem implements Serializable{
    private Integer id;

    @NotBlank(message = "问题的标题不能为空！")
    private String title;

    private String choiceA;

    private String choiceB;

    private Byte isActive=1;

    private Byte orderBy=0;
}