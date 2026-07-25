package com.iy.api.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_audit_log")
public class SysAuditLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String account;

    private String module;

    private String operationType;

    private String description;

    private String requestMethod;

    private String requestUri;

    private String requestIp;

    private String requestParams;

    private String responseResult;

    private Integer success;

    private String errorMessage;

    private Long duration;

    private Date createTime;
}
