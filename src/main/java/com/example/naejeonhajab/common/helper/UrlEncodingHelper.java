package com.example.naejeonhajab.common.helper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlEncodingHelper {
    public static String encodeToUrl(String text) {
        try {
            return URLEncoder.encode(text, StandardCharsets.UTF_8)
                    .replace("+", "%20");  // 이 줄이 중요!
        } catch (Exception e) {
            throw new RuntimeException("URL 인코딩 실패", e);
        }
    }


    public static String encodeToUrlConditionally(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        if (shouldEncode(value)) {
            return encodeToUrl(value); // 위에 정의된 메서드 호출
        }

        return value;
    }

    // ASCII 외 문자가 있는지 확인 (예: 한글, 공백, 특수문자 포함 여부)
    private static boolean shouldEncode(String value) {
        for (char c : value.toCharArray()) {
            // 공백이거나, 비ASCII 문자거나, URL 특수문자면 인코딩
            if (c > 127 || c == ' ' || "!@#$%^&*()=+[]{}|\\:;\"'<>,/?".indexOf(c) >= 0) {
                return true;
            }
        }
        return false;
    }

}
