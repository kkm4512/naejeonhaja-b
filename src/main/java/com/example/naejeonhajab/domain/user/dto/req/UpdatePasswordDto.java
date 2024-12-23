package com.example.naejeonhajab.domain.user.dto.req;

import com.example.naejeonhajab.annotation.AllowedEmailDomains;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDto {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @AllowedEmailDomains(domains = {"gmail.com", "naver.com", "kakao.com"},
            message = "이메일은 kakao,naver,gmail만 가능합니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[@$!%*?&].*[@$!%*?&])(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "비밀번호는 최소 하나의 대문자, 최소 두 개의 특수문자, 최소 하나의 숫자를 포함해야 합니다."
    )
    private String password;

}
