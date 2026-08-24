package com.ringsandpromises.backend.controller;

import com.ringsandpromises.backend.dto.ContactRequest;
import com.ringsandpromises.backend.entity.ContactInquiry;
import com.ringsandpromises.backend.repository.ContactInquiryRepository;
import com.ringsandpromises.backend.service.EmailService;
import com.ringsandpromises.backend.service.PdfService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "http://localhost:5173")
public class ContactController {

    private final ContactInquiryRepository repository;
    private final PdfService pdfService;
    private final EmailService emailService;

    public ContactController(ContactInquiryRepository repository, PdfService pdfService, EmailService emailService) {
        this.repository = repository;
        this.pdfService = pdfService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<?> submitInquiry(@Valid @RequestBody ContactRequest request) {
        ContactInquiry inquiry = new ContactInquiry();
        inquiry.setFullName(request.getFullName());
        inquiry.setEmail(request.getEmail());
        inquiry.setPhone(request.getPhone());
        inquiry.setEventType(request.getEventType());
        inquiry.setEventDate(request.getEventDate());
        inquiry.setGuestCount(request.getGuestCount());
        inquiry.setVenuePreference(request.getVenuePreference());
        inquiry.setBudgetRange(request.getBudgetRange());
        inquiry.setServices(request.getServices());
        inquiry.setVision(request.getVision());

        // Persist and commit the inquiry before producing or sending any documents.
        ContactInquiry savedInquiry = repository.saveAndFlush(inquiry);
        byte[] pdfBytes = pdfService.generateInquiryPdf(savedInquiry);

        emailService.sendCustomerInquiryEmail(savedInquiry, pdfBytes);
        emailService.sendAdminInquiryEmail(savedInquiry, pdfBytes);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedInquiry);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        ContactInquiry inquiry = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        byte[] pdfBytes = pdfService.generateInquiryPdf(inquiry);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header("Content-Disposition", "inline; filename=inquiry.pdf")
            .body(pdfBytes);
    }
}
