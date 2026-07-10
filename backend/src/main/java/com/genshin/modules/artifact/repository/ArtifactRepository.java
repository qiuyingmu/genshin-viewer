package com.genshin.modules.artifact.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.genshin.modules.artifact.entity.GenshinArtifact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArtifactRepository extends BaseMapper<GenshinArtifact> {

    @Select("SELECT * FROM genshin_artifact WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<GenshinArtifact> findByUserId(Long userId);
}
