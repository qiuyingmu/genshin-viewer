package com.genshin.modules.character.controller;

import com.genshin.aop.OperationLog;
import com.genshin.common.Result;
import com.genshin.modules.character.entity.GenshinCharacter;
import com.genshin.modules.character.service.CharacterService;
import com.genshin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping
    public Result<List<GenshinCharacter>> list(@AuthenticationPrincipal AuthUser user) {
        return Result.success(characterService.list(user.getUserId()));
    }

    @PostMapping
    @OperationLog(action = "添加", resource = "角色")
    public Result<GenshinCharacter> create(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody Map<String, Object> body) {
        return Result.success(characterService.create(user.getUserId(), body));
    }

    @DeleteMapping("/{id}")
    @OperationLog(action = "删除", resource = "角色")
    public Result<Void> delete(@AuthenticationPrincipal AuthUser user, @PathVariable Long id) {
        characterService.delete(user.getUserId(), id);
        return Result.success();
    }
}
