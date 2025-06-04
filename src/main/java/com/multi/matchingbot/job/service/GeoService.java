package com.multi.matchingbot.job.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GeoService {

    private final WebClient webClient;

    public GeoService(@Value("${kakao.rest-api-key}") String kakaoApiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, kakaoApiKey)
                .build();
    }

    public double[] getLatLngFromAddress(String address) {
        Map response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/address.json")
                        .queryParam("query", address)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List documents = (List) response.get("documents");
        if (documents == null || documents.isEmpty()) {
            return new double[]{37.5665, 126.9780}; // fallback 좌표
        }

        Map first = (Map) documents.get(0);
        return new double[]{
                Double.parseDouble((String) first.get("y")), // 위도
                Double.parseDouble((String) first.get("x"))  // 경도
        };
    }
}

