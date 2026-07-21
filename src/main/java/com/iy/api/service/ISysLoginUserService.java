package com.iy.api.service;

import com.iy.api.model.entity.SysLoginUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户登录账号表 服务类接口
 *
 * @author liuxiaonan
 * @since 2026-07-19 22:37
 */
public interface ISysLoginUserService extends IService<SysLoginUserEntity> {

    SysLoginUserEntity getByAccount(String account);
}