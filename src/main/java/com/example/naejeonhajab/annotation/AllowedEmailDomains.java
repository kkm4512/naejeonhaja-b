package com.example.naejeonhajab.annotation;

import com.example.naejeonhajab.annotation.annotationValidator.AllowedEmailDomainsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedEmailDomainsValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedEmailDomains {
    String message() default "허용되지 않은 이메일 도메인입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] domains() default {}; // 허용할 도메인 목록
}
