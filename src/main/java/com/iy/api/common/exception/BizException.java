package com.iy.api.common.exception;

import com.iy.api.common.enums.BizCodeEnum;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

    private final String code;

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(BizCodeEnum bizCode) {
        super(bizCode.getMessage());
        this.code = bizCode.getCode();
    }

    public BizException(BizCodeEnum bizCode, String detail) {
        super(bizCode.getMessage() + ": " + detail);
        this.code = bizCode.getCode();
    }
}
