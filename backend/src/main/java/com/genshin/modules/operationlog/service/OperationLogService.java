package com.genshin.modules.operationlog.service;

import com.genshin.modules.operationlog.entity.SysOperationLog;
import com.genshin.modules.operationlog.repository.OperationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationLogService {

    private final OperationLogRepository operationLogRepository;

    public List<SysOperationLog> list(int limit) {
        return operationLogRepository.findRecent(limit);
    }

    public List<SysOperationLog> listByUser(Long userId, int limit) {
        return operationLogRepository.findByUserId(userId, limit);
    }
}
