package com.iy.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iy.api.model.entity.SysAuditLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysAuditLogMapper extends BaseMapper<SysAuditLogEntity> {
}
