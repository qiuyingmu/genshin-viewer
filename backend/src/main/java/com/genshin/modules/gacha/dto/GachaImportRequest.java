package com.genshin.modules.gacha.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GachaImportRequest {
    @Valid
    @NotNull(message = "记录不能为空")
    private List<GachaItem> items;

    @Data
    public static class GachaItem {
        @NotBlank(message = "名称不能为空")
        private String name;
        @NotNull(message = "稀有度不能为空")
        private Integer rarity;
        private String type = "角色";
        private String banner = "character";
        private String time;
    }
}
