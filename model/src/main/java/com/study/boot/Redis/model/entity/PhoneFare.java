package com.study.boot.Redis.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class PhoneFare implements Serializable {
    private Integer id;

    @NotBlank(message = "手机号码不能为空！")
    private String phone;

    @NotNull(message = "充值金额不能为空！")
    private BigDecimal fare;

    private Byte isActive = 1;

    public PhoneFare(String phone, BigDecimal fare) {
        this.phone = phone;
        this.fare = fare;
    }
}