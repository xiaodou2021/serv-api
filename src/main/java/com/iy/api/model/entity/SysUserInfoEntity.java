package com.iy.api.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 用户基础信息表
 *
 * @author liuxiaonan
 * @since 2026-07-19 22:37
 */
@Data
@TableName("sys_user_info")
public class SysUserInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联账号表user_id，一对一唯一
     */
    private Long userId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别：0未知 1男 2女
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 出生时间
     */
    private String birthTime;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区/县
     */
    private String district;

    /**
     * 详细收货/居住地址
     */
    private String address;

    /**
     * 扩展自定义字段（无需加字段直接存JSON）
     */
    private String extraJson;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 更新人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}
