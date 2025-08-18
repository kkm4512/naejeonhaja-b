package com.example.naejeonhajab.common.helper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlEncodingHelper {
    public static String encodeToUrl(String text) {
        try {
            // UTF-8로 URL 인코딩
            return URLEncoder.encode(text, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("URL 인코딩 실패", e);
        }
    }

    public static void main(String[] args) {
        String original = "자신있게싸우자";
        String encoded = encodeToUrl(original);

        System.out.println("원본: " + original);
        System.out.println("URL 인코딩: " + encoded);
    }
}
