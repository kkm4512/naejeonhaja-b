package com.example.naejeonhajab.domain.inquiry.service;

import com.example.naejeonhajab.common.exception.InquiryException;
import com.example.naejeonhajab.common.response.enums.InquiryApiResponse;
import com.example.naejeonhajab.domain.inquiry.dto.InquiryRequestDto;
import com.example.naejeonhajab.domain.inquiry.dto.InquiryResponseDto;
import com.example.naejeonhajab.domain.inquiry.entity.Inquiry;
import com.example.naejeonhajab.domain.inquiry.entity.Tag;
import com.example.naejeonhajab.domain.inquiry.repository.InquiryRepository;
import com.example.naejeonhajab.domain.inquiry.repository.TagRepository;
import com.example.naejeonhajab.domain.user.entity.User;
import com.example.naejeonhajab.domain.user.service.UserService;
import com.example.naejeonhajab.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.List;

import static com.example.naejeonhajab.common.response.enums.InquiryApiResponse.INQUIRY_NOT_FOUND;

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
            inquiry.getTags().addAll(tags);
            inquiryRepository.save(inquiry);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Page<InquiryResponseDto> getAllInquiries(Pageable pageable) {
        return getAllInquiriesQuery(pageable);
    }

    @Transactional(readOnly = true)
    public InquiryResponseDto getInquiryById(Long id) {
        Inquiry inquiry = getInquiryByIdQuery(id);
        return InquiryResponseDto.of(inquiry);
    }

    @Transactional
    public void updateInquiry(Long id, InquiryRequestDto dto, AuthUser authuser) {
        User user = User.of(authuser);
        Inquiry inquiry = getInquiryByIdQuery(id);
        user.isMe(inquiry.getUser().getId());
        inquiry.update(dto);
        inquiry.getTags().addAll(Tag.of(dto,inquiry));
        inquiryRepository.save(inquiry);
    }

    @Transactional
    public void deleteInquiry(Long id, AuthUser authuser) {
        User user = User.of(authuser);
        Inquiry inquiry = getInquiryByIdQuery(id);
        user.isMe(inquiry.getUser().getId());
        deleteInquiryQuery(inquiry);
    }

    @Transactional(readOnly = true)
    protected Page<InquiryResponseDto> getAllInquiriesQuery(Pageable pageable) {
        Page<Inquiry> result = inquiryRepository.findAll(pageable);
        return result.map(InquiryResponseDto::of);
    }

    @Transactional
    protected void deleteInquiryQuery(Inquiry inquiry) {
        inquiryRepository.delete(inquiry);
    }


    @Transactional(readOnly = true)
    protected Inquiry getInquiryByIdQuery(Long id){
        return inquiryRepository.findById(id).orElseThrow(() -> new InquiryException(INQUIRY_NOT_FOUND));
    }



}
