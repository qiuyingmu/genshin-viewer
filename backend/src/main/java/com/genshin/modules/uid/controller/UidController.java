package com.genshin.modules.uid.controller;

import com.genshin.common.Result;
import com.genshin.modules.uid.service.UidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/uid")
@RequiredArgsConstructor
public class UidController {

    private final UidService uidService;

    @GetMapping("/{uid}")
    public Result<Map<String, Object>> queryUid(@PathVariable String uid) {
        if (uid == null || !uid.matches("\\d{8,9}")) {
            return Result.error("UID 格式不正确，应为 8-9 位数字");
        }
        try {
            Map<String, Object> data = uidService.queryUid(uid);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("UID 查询失败: " + e.getMessage());
        }
    }
}
