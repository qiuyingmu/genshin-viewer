package com.genshin.modules.artifact.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("genshin_artifact")
public class GenshinArtifact {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String setId;
    private String slot;
    private Integer stars;
    private Integer level;
    private String mainStat;
    private String mainValue;
    private String subs;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
