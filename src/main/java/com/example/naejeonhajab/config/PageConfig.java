package com.example.naejeonhajab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

// Page객체 너무많으니까, 아래 형식으로 바꿔줌
/*
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
*/
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class PageConfig {
}
