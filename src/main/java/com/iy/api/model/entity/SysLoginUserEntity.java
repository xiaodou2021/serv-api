package com.iy.api.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.iy.api.common.annotation.Sensitive;
import com.iy.api.common.enums.SensitiveType;
import com.iy.api.model.vo.UserVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 用户登录账号表
 *
 * @author liuxiaonan
 * @since 2026-07-19 22:37
 */
@Data
@TableName("sys_login_user")
public class SysLoginUserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID,唯一
     */
    private Long userId;

    /**
     * 登录账号，唯一（用户名/手机号/邮箱）
     */
    private String account;

    /**
     * 加密密码（BCrypt加密，不存明文）
     */
    @Sensitive(type = SensitiveType.PASSWORD)
    private String password;

    /**
     * 加密盐值（BCrypt可省略，预留兼容）
     */
    @Sensitive(type = SensitiveType.PASSWORD)
    private String salt;

    /**
     * 账号展示昵称
     */
    private String nickname;

    /**
     * 默认头像地址
     */
    private String avatar;

    /**
     * 绑定邮箱，可用于登录/找回密码
     */
    @Sensitive(type = SensitiveType.EMAIL)
    private String email;

    /**
     * 绑定手机号，可用于登录
     */
    @Sensitive(type = SensitiveType.PHONE)
    private String phone;

    /**
     * 锁定标记：0未锁定 1锁定
     */
    private Integer lockFlag;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 逻辑删除：0未删 1已删
     */
    @TableLogic
    private Integer deleted;

    public UserVO toUserVO() {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(this, userVO);
        return userVO;
    }
}