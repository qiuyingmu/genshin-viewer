package com.genshin.modules.gacha.service;

import com.genshin.common.BusinessException;
import com.genshin.modules.gacha.dto.GachaImportRequest;
import com.genshin.modules.gacha.entity.GenshinGacha;
import com.genshin.modules.gacha.repository.GachaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GachaService {

    private final GachaRepository gachaRepository;

    public List<GenshinGacha> list(Long userId, String banner) {
        if (banner != null && !banner.isBlank() && !"all".equals(banner)) {
            return gachaRepository.findByUserIdAndBanner(userId, banner);
        }
        return gachaRepository.findByUserId(userId);
    }

    public List<GenshinGacha> importBatch(Long userId, GachaImportRequest request) {
        var results = request.getItems().stream().map(item -> {
            var g = new GenshinGacha();
            g.setUserId(userId);
            g.setName(item.getName());
            g.setRarity(item.getRarity());
            g.setType(item.getType());
            g.setBanner(item.getBanner());
            g.setGachaTime(parseTime(item.getTime()));
            g.setCreatedAt(LocalDateTime.now());
            gachaRepository.insert(g);
            return g;
        }).toList();
        return results;
    }

    public Map<String, Object> stats(Long userId) {
        long total = gachaRepository.findByUserId(userId).size();
        long fiveStar = gachaRepository.countByRarity(userId, 5);
        long fourStar = gachaRepository.countByRarity(userId, 4);
        return Map.of(
                "total", total,
                "fiveStar", fiveStar,
                "fourStar", fourStar
        );
    }

    public void delete(Long userId, Long gachaId) {
        GenshinGacha g = gachaRepository.selectById(gachaId);
        if (g == null) throw BusinessException.notFound("抽卡记录", gachaId);
        if (!g.getUserId().equals(userId)) throw BusinessException.forbidden("无权删除");
        gachaRepository.deleteById(gachaId);
    }

    private LocalDateTime parseTime(String time) {
        if (time == null || time.isBlank()) return LocalDateTime.now();
        try {
            return LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
