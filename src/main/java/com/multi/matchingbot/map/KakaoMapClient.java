package com.multi.matchingbot.map;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class KakaoMapClient {

    @Value("${kakao.rest-api-key}")
    private String kakaoApiKey;

    public double[] getLatLngFromAddress(String address) {
        String encodedAddress = UriUtils.encode(address, StandardCharsets.UTF_8);
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + encodedAddress;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kakaoApiKey);  // KakaoAK 포함된 값

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class);

        JSONObject json = new JSONObject(response.getBody());
        JSONArray documents = json.getJSONArray("documents");

        if (documents.isEmpty()) return null;

        JSONObject first = documents.getJSONObject(0);
        double lat = first.getDouble("y");
        double lng = first.getDouble("x");

        return new double[]{lat, lng};
    }
}
