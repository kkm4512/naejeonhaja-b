package com.example.naejeonhaja.domain.game.health;

import com.example.naejeonhaja.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.naejeonhaja.common.response.enums.BaseApiResponse.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {
    @GetMapping()
    public ApiResponse<Void> healthCheck() {
        return ApiResponse.of(SUCCESS, null);
    }
}
