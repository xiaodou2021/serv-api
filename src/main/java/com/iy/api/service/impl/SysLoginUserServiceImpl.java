package com.iy.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.iy.api.service.ISysLoginUserService;
import com.iy.api.model.entity.SysLoginUserEntity;
import com.iy.api.mapper.SysLoginUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户登录账号表 服务实现类
 *
 * @author liuxiaonan
 * @since 2026-07-19 22:37
 */
@Service
public class SysLoginUserServiceImpl extends ServiceImpl<SysLoginUserMapper, SysLoginUserEntity> implements ISysLoginUserService {

    @Override
    public SysLoginUserEntity getByAccount(String account) {
        return baseMapper.selectOne(new LambdaQueryWrapper<SysLoginUserEntity>().eq(SysLoginUserEntity::getAccount, account));
    }
}