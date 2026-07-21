package com.iy.api.common.enums;

import lombok.Getter;

@Getter
public enum YesOrNoEnum {
    YES(1, "是"),
    NO(0, "否");

    private Integer code;
    private String desc;

    YesOrNoEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
