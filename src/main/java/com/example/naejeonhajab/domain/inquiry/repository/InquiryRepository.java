package com.example.naejeonhajab.domain.inquiry.repository;

import com.example.naejeonhajab.domain.inquiry.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    Page<Inquiry> findAll(Pageable pageable);
}
