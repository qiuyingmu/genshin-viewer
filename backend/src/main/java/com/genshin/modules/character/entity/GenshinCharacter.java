package com.genshin.modules.character.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("genshin_character")
public class GenshinCharacter {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String charId;
    private Integer level;
    private Integer friendship;
    private Integer constellation;
    private String talents;
    private String weapon;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
