package com.genshin.modules.artifact.controller;

import com.genshin.aop.OperationLog;
import com.genshin.common.Result;
import com.genshin.modules.artifact.dto.ArtifactRequest;
import com.genshin.modules.artifact.entity.GenshinArtifact;
import com.genshin.modules.artifact.service.ArtifactService;
import com.genshin.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artifacts")
@RequiredArgsConstructor
public class ArtifactController {

    private final ArtifactService artifactService;

    @GetMapping
    public Result<List<GenshinArtifact>> list(
            @AuthenticationPrincipal AuthUser user,
            @RequestParam(required = false) String set,
            @RequestParam(required = false) String slot,
            @RequestParam(required = false) String stat) {
        return Result.success(artifactService.list(user.getUserId(), set, slot, stat));
    }

    @PostMapping
    @OperationLog(action = "添加", resource = "圣遗物")
    public Result<GenshinArtifact> create(
            @AuthenticationPrincipal AuthUser user,
            @Valid @RequestBody ArtifactRequest request) {
        return Result.success(artifactService.create(user.getUserId(), request));
    }

    @DeleteMapping("/{id}")
    @OperationLog(action = "删除", resource = "圣遗物")
    public Result<Void> delete(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable Long id) {
        artifactService.delete(user.getUserId(), id);
        return Result.success();
    }
}
