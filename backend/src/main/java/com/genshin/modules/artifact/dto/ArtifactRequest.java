package com.genshin.modules.artifact.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ArtifactRequest {
    @NotBlank(message = "套装不能为空")
    private String setId;

    @NotBlank(message = "部位不能为空")
    private String slot;

    @NotNull(message = "星级不能为空")
    private Integer stars;

    private Integer level = 0;

    @NotBlank(message = "主属性不能为空")
    private String mainStat;

    private String mainValue;

    private List<SubStat> subs;

    @Data
    public static class SubStat {
        private String name;
        private String value;
    }
}
