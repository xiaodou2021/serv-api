package com.iy.api.common;

import com.iy.api.common.constants.SystemConstants;

public class ResultHelper {

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<T>(SystemConstants.SUCCESS_CODE,"success",data);
    }

    public static <T> ResultVO<T> error(String code, String message) {
        return new ResultVO<T>(code,message,null);
    }
}
