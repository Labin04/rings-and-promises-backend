package com.ringsandpromises.backend.repository;

import com.ringsandpromises.backend.entity.ContactInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInquiryRepository extends JpaRepository<ContactInquiry, Long> {
}