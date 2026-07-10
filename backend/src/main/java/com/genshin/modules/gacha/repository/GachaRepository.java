package com.genshin.modules.gacha.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.genshin.modules.gacha.entity.GenshinGacha;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GachaRepository extends BaseMapper<GenshinGacha> {

    @Select("SELECT * FROM genshin_gacha WHERE user_id = #{userId} ORDER BY gacha_time DESC")
    List<GenshinGacha> findByUserId(Long userId);

    @Select("SELECT * FROM genshin_gacha WHERE user_id = #{userId} AND banner = #{banner} ORDER BY gacha_time DESC")
    List<GenshinGacha> findByUserIdAndBanner(Long userId, String banner);

    @Select("SELECT COUNT(*) FROM genshin_gacha WHERE user_id = #{userId} AND rarity = #{rarity}")
    long countByRarity(Long userId, int rarity);
}
