package com.example.naejeonhajab.domain.health;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<String> albTest() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
