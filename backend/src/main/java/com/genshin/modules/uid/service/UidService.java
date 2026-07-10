package com.genshin.modules.uid.service;

import com.genshin.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class UidService {

    private final String enkaBaseUrl;
    private final RestTemplate restTemplate;

    public UidService(@Value("${enka.api.base-url}") String enkaBaseUrl) {
        this.enkaBaseUrl = enkaBaseUrl;
        this.restTemplate = new RestTemplate();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> queryUid(String uid) {
        String url = enkaBaseUrl + "/" + uid + "/";
        log.info("Proxying Enka API: {}", url);
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null) {
                throw new RuntimeException("Enka API returned empty response");
            }
            return response;
        } catch (Exception e) {
            log.error("Enka API request failed for uid={}: {}", uid, e.getMessage());
            throw new RuntimeException("UID 查询失败: " + e.getMessage());
        }
    }
}
