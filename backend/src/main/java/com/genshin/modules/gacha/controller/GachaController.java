package com.genshin.modules.gacha.controller;

import com.genshin.aop.OperationLog;
import com.genshin.common.Result;
import com.genshin.modules.gacha.dto.GachaImportRequest;
import com.genshin.modules.gacha.entity.GenshinGacha;
import com.genshin.modules.gacha.service.GachaService;
import com.genshin.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gacha")
@RequiredArgsConstructor
public class GachaController {

    private final GachaService gachaService;

    @GetMapping
    public Result<List<GenshinGacha>> list(
            @AuthenticationPrincipal AuthUser user,
            @RequestParam(required = false) String banner) {
        return Result.success(gachaService.list(user.getUserId(), banner));
    }

    @PostMapping("/import")
    @OperationLog(action = "导入", resource = "抽卡记录")
    public Result<List<GenshinGacha>> importBatch(
            @AuthenticationPrincipal AuthUser user,
            @Valid @RequestBody GachaImportRequest request) {
        return Result.success(gachaService.importBatch(user.getUserId(), request));
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> stats(@AuthenticationPrincipal AuthUser user) {
        return Result.success(gachaService.stats(user.getUserId()));
    }

    @DeleteMapping("/{id}")
    @OperationLog(action = "删除", resource = "抽卡记录")
    public Result<Void> delete(@AuthenticationPrincipal AuthUser user, @PathVariable Long id) {
        gachaService.delete(user.getUserId(), id);
        return Result.success();
    }
}
