package com.example.naejeonhajab.domain.user.dto.req;

import com.example.naejeonhajab.annotation.AllowedEmailDomains;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCodeDto {
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @AllowedEmailDomains(domains = {"gmail.com", "naver.com", "kakao.com"},
            message = "이메일은 kakao,naver,gmail만 가능합니다")
    private String email;

    @NotBlank(message = "인증코드는 필수 입력 항목입니다.")
    private String code;
}
