package com.genshin.modules.artifact.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genshin.common.BusinessException;
import com.genshin.modules.artifact.dto.ArtifactRequest;
import com.genshin.modules.artifact.entity.GenshinArtifact;
import com.genshin.modules.artifact.repository.ArtifactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtifactService {

    private final ArtifactRepository artifactRepository;
    private final ObjectMapper objectMapper;

    public List<GenshinArtifact> list(Long userId, String setFilter, String slotFilter, String statFilter) {
        return artifactRepository.findByUserId(userId).stream()
                .filter(a -> setFilter == null || setFilter.isBlank() || setFilter.equals("all") || a.getSetId().equals(setFilter))
                .filter(a -> slotFilter == null || slotFilter.isBlank() || slotFilter.equals("all") || a.getSlot().equals(slotFilter))
                .filter(a -> statFilter == null || statFilter.isBlank() || statFilter.equals("all") || a.getMainStat().equals(statFilter))
                .collect(Collectors.toList());
    }

    public GenshinArtifact create(Long userId, ArtifactRequest request) {
        var artifact = new GenshinArtifact();
        artifact.setUserId(userId);
        artifact.setSetId(request.getSetId());
        artifact.setSlot(request.getSlot());
        artifact.setStars(request.getStars());
        artifact.setLevel(request.getLevel());
        artifact.setMainStat(request.getMainStat());
        artifact.setMainValue(request.getMainValue());
        artifact.setSubs(toJson(request.getSubs()));
        artifact.setCreatedAt(LocalDateTime.now());
        artifactRepository.insert(artifact);
        return artifact;
    }

    public void delete(Long userId, Long artifactId) {
        GenshinArtifact art = artifactRepository.selectById(artifactId);
        if (art == null) throw BusinessException.notFound("圣遗物", artifactId);
        if (!art.getUserId().equals(userId)) throw BusinessException.forbidden("无权删除");
        artifactRepository.deleteById(artifactId);
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JSON serialize error", e);
            return "[]";
        }
    }
}
