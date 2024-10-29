package com.sanapyeong.mtvs_3rd_dreamplanet.component;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserTokenStorage {

    public final Map<String, Long> tokenStorage;

    public UserTokenStorage() {
        this.tokenStorage = new ConcurrentHashMap<>();
    }

    // Bearer 제거
    public String removeBearer(String token) {
        if (token == null) {
            return null;
        }
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    // 토큰 저장
    public void storeToken(String token, Long userId) {
        if (tokenStorage.containsValue(userId)) {
            tokenStorage.remove(getKeyByValue(tokenStorage, userId));
        }
        tokenStorage.put(token, userId);
    }

    // 토큰 조회
    public Long getToken(String token) {
        if (token == null) {
            return null;
        }
        return tokenStorage.get(removeBearer(token));
    }

    private static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null; // 값이 존재하지 않을 경우
    }

    // 토큰 삭제
    public void removeToken(String token) {
        tokenStorage.remove((token));
    }
}
