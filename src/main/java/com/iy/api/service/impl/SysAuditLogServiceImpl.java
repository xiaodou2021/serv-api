package com.iy.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iy.api.mapper.SysAuditLogMapper;
import com.iy.api.model.entity.SysAuditLogEntity;
import com.iy.api.service.ISysAuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysAuditLogServiceImpl extends ServiceImpl<SysAuditLogMapper, SysAuditLogEntity> implements ISysAuditLogService {

    @Override
    @Async("auditLogExecutor")
    public void saveAuditLogAsync(SysAuditLogEntity auditLog) {
        try {
            save(auditLog);
        } catch (Exception e) {
            log.error("Failed to save audit log: {}", e.getMessage(), e);
        }
    }
}
