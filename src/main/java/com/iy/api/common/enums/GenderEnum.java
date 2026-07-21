package com.iy.api.common.enums;

import lombok.Getter;

@Getter
public enum GenderEnum {
    MALE(1, "男"),
    FEMALE(0, "女");

    private Integer code;
    private String desc;

    GenderEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
