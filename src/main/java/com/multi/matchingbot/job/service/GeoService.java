package com.multi.matchingbot.job.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class GeoService {

    private static final String KAKAO_API_KEY = "ff074b52c396de087778f57fa8b9f2db"; // TODO: API 키 등록

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://dapi.kakao.com/v2/local/search/address.json")
            .defaultHeader(HttpHeaders.AUTHORIZATION, KAKAO_API_KEY)
            .build();

    public double[] getLatLngFromAddress(String address) {
        String encoded = UriUtils.encode(address, StandardCharsets.UTF_8);
        Map response = webClient.get()
                .uri("?query=" + encoded)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List documents = (List) response.get("documents");
        if (documents == null || documents.isEmpty()) {
            // fallback 좌표 (서울시청)
            return new double[]{37.5665, 126.9780};
        }

        Map first = (Map) documents.get(0);
        return new double[]{
                Double.parseDouble((String) first.get("y")), // latitude
                Double.parseDouble((String) first.get("x"))  // longitude
        };
    }
}

