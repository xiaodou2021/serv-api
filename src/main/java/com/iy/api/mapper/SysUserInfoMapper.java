package com.iy.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.iy.api.model.entity.SysUserInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* 用户基础信息表 Mapper
*
* @author liuxiaonan
* @since 2026-07-19 22:37
*/
@Mapper
public interface SysUserInfoMapper extends BaseMapper<SysUserInfoEntity> {
}
