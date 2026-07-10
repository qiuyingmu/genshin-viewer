package com.genshin.modules.character.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.genshin.modules.character.entity.GenshinCharacter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CharacterRepository extends BaseMapper<GenshinCharacter> {

    @Select("SELECT * FROM genshin_character WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<GenshinCharacter> findByUserId(Long userId);
}
