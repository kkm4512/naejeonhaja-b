package com.example.naejeonhajab.domain.inquiry.service;

import com.example.naejeonhajab.domain.inquiry.dto.InquiryRequestDto;
import com.example.naejeonhajab.domain.inquiry.dto.InquiryResponseDto;
import com.example.naejeonhajab.domain.inquiry.entity.Inquiry;
import com.example.naejeonhajab.domain.inquiry.entity.Tag;
import com.example.naejeonhajab.domain.inquiry.repository.InquiryRepository;
import com.example.naejeonhajab.domain.user.entity.User;
import com.example.naejeonhajab.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Slf4j(topic = "InquiryService")
@Service
@RequiredArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;

    @Transactional
    public void createInquiry(InquiryRequestDto dto, AuthUser authuser) {
        try {
            User user = User.of(authuser);
            Inquiry inquiry = Inquiry.of(dto, user);
            List<Tag> tags = Tag.of(dto,inquiry);
            inquiry.getTags().addAll(tags);  // Inquiry 객체에 태그 추가
            inquiryRepository.save(inquiry);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Page<InquiryResponseDto> getAllInquiries(Pageable pageable) {
        return getAllInquiriesByPage(pageable);
    }

    @Transactional(readOnly = true)
    protected Page<InquiryResponseDto> getAllInquiriesByPage(Pageable pageable) {
        Page<Inquiry> result = inquiryRepository.findAll(pageable);
        return result.map(InquiryResponseDto::of);
    }
}
