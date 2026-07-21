package com.iy.api.service.impl;

import com.iy.api.service.ISysUserInfoService;
import com.iy.api.model.entity.SysUserInfoEntity;
import com.iy.api.mapper.SysUserInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户基础信息表 服务实现类
 *
 * @author liuxiaonan
 * @since 2026-07-19 22:37
 */
@Service
public class SysUserInfoServiceImpl extends ServiceImpl<SysUserInfoMapper, SysUserInfoEntity> implements ISysUserInfoService {

}
