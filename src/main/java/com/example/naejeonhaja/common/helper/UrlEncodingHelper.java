package com.example.naejeonhaja.common.helper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlEncodingHelper {

    public static String encodeToUrl(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8).replace("+", "%20");
    }

    public static String encodeToUrlConditionally(String value) {
        if (value == null || value.isEmpty()) return value;
        return shouldEncode(value) ? encodeToUrl(value) : value;
    }

    private static boolean shouldEncode(String value) {
        for (char c : value.toCharArray()) {
            if (c > 127 || c == ' ' || "!@#$%^&*()=+[]{}|\\:;\"'<>,/?".indexOf(c) >= 0) return true;
        }
        return false;
    }
}
