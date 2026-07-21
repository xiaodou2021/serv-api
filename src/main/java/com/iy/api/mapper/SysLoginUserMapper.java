package com.iy.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.iy.api.model.entity.SysLoginUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* 用户登录账号表 Mapper
*
* @author liuxiaonan
* @since 2026-07-19 22:37
*/
@Mapper
public interface SysLoginUserMapper extends BaseMapper<SysLoginUserEntity> {
    
    SysLoginUserEntity selectByAccount(String account);
}