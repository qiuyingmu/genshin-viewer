package com.genshin.modules.character.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genshin.common.BusinessException;
import com.genshin.modules.character.entity.GenshinCharacter;
import com.genshin.modules.character.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final ObjectMapper objectMapper;

    public List<GenshinCharacter> list(Long userId) {
        return characterRepository.findByUserId(userId);
    }

    public GenshinCharacter create(Long userId, Map<String, Object> body) {
        var ch = new GenshinCharacter();
        ch.setUserId(userId);
        ch.setCharId((String) body.get("charId"));
        ch.setLevel(toInt(body.get("level"), 1));
        ch.setFriendship(toInt(body.get("friendship"), 0));
        ch.setConstellation(toInt(body.get("constellation"), 0));
        ch.setWeapon((String) body.getOrDefault("weapon", ""));
        ch.setTalents(toJson(body.getOrDefault("talents", Map.of())));
        ch.setCreatedAt(LocalDateTime.now());
        characterRepository.insert(ch);
        return ch;
    }

    public void delete(Long userId, Long charId) {
        GenshinCharacter ch = characterRepository.selectById(charId);
        if (ch == null) throw BusinessException.notFound("角色", charId);
        if (!ch.getUserId().equals(userId)) throw BusinessException.forbidden("无权删除");
        characterRepository.deleteById(charId);
    }

    private int toInt(Object val, int def) {
        if (val instanceof Number) return ((Number) val).intValue();
        return def;
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JSON error", e);
            return "{}";
        }
    }
}
