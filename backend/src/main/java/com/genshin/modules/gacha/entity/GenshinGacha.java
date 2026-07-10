package com.genshin.modules.gacha.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("genshin_gacha")
public class GenshinGacha {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private Integer rarity;
    private String type;
    private String banner;
    private LocalDateTime gachaTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
