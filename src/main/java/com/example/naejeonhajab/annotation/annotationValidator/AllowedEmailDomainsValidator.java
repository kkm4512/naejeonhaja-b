package com.example.naejeonhajab.annotation.annotationValidator;

import com.example.naejeonhajab.annotation.AllowedEmailDomains;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class AllowedEmailDomainsValidator implements ConstraintValidator<AllowedEmailDomains, String> {

    private String[] allowedDomains;

    @Override
    public void initialize(AllowedEmailDomains constraintAnnotation) {
        this.allowedDomains = constraintAnnotation.domains();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // 다른 어노테이션에서 null/empty 처리
        }

        String domain = value.substring(value.indexOf("@") + 1);
        return Arrays.asList(allowedDomains).contains(domain);
    }
}
