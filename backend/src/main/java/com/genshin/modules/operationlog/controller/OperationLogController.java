package com.genshin.modules.operationlog.controller;

import com.genshin.common.Result;
import com.genshin.modules.operationlog.entity.SysOperationLog;
import com.genshin.modules.operationlog.service.OperationLogService;
import com.genshin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
public class OperationLogController {

    private final OperationLogService operationLogService;

    @GetMapping
    public Result<List<SysOperationLog>> list(
            @AuthenticationPrincipal AuthUser user,
            @RequestParam(defaultValue = "50") int limit) {
        return Result.success(operationLogService.listByUser(user.getUserId(), Math.min(limit, 200)));
    }
}
