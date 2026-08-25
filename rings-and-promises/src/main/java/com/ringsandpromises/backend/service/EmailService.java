package com.ringsandpromises.backend.service;

import com.ringsandpromises.backend.entity.ContactInquiry;
import jakarta.mail.MessagingException;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

/** Sends inquiry emails after an inquiry has been stored and its PDF has been generated. */
@Service
public class EmailService {

    private static final String PDF_CONTENT_TYPE = "application/pdf";

    private final JavaMailSender mailSender;
    private final String adminEmail;
    private final String fromAddress;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${app.mail.admin-email}") String adminEmail,
            @Value("${app.mail.from-address}") String fromAddress) {
        this.mailSender = mailSender;
        this.adminEmail = adminEmail;
        this.fromAddress = fromAddress;
    }

    @Async
    public void sendCustomerInquiryEmail(ContactInquiry inquiry, byte[] pdfBytes) {
        sendEmail(
            inquiry.getEmail(),
            "We've received your Rings & Promises inquiry",
            "Hello " + inquiry.getFullName() + ",\n\n"
                + "Thank you for contacting Rings & Promises. We have received your event inquiry "
                + "and will be in touch shortly. A copy of your inquiry is attached.\n\n"
                + "Warm regards,\nRings & Promises",
            pdfBytes,
            "rings-and-promises-inquiry-" + inquiry.getId() + ".pdf"
        );
    }

    @Async
    public void sendAdminInquiryEmail(ContactInquiry inquiry, byte[] pdfBytes) {        sendEmail(
            adminEmail,
            "New event inquiry from " + inquiry.getFullName(),
            "A new event inquiry has been received from " + inquiry.getFullName() + " ("
                + inquiry.getEmail() + ", phone: " + inquiry.getPhone()
                + "). The complete inquiry is attached as a PDF.",
            pdfBytes,
            "rings-and-promises-inquiry-" + inquiry.getId() + ".pdf"
        );
    }

    private void sendEmail(String recipient, String subject, String body, byte[] pdfBytes, String attachmentName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body, false);
            helper.addAttachment(attachmentName, new ByteArrayResource(pdfBytes), PDF_CONTENT_TYPE);
            mailSender.send(message);
        } catch (MessagingException | MailException exception) {
            throw new IllegalStateException("Failed to send inquiry email to " + recipient, exception);
        }
    }
}
