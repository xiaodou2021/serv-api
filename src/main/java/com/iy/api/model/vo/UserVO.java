package com.iy.api.model.vo;

import com.iy.api.common.annotation.Sensitive;
import com.iy.api.common.enums.SensitiveType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String account;

    private String nickname;

    private String avatar;

    @Sensitive(type = SensitiveType.EMAIL)
    private String email;

    @Sensitive(type = SensitiveType.PHONE)
    private String phone;

    private Integer lockFlag;

    private Date lastLoginTime;

}