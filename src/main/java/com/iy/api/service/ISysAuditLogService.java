package com.iy.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iy.api.model.entity.SysAuditLogEntity;

public interface ISysAuditLogService extends IService<SysAuditLogEntity> {

    void saveAuditLogAsync(SysAuditLogEntity auditLog);
}
