package com.ringsandpromises.backend.service;

import com.ringsandpromises.backend.entity.ContactInquiry;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
@Service
public class PdfService {

    private static final Color BRAND_GREEN = new Color(74, 82, 64);
    private static final Color BRAND_GOLD = new Color(176, 141, 87);
    private static final Color LIGHT_BG = new Color(253, 251, 247);
    private static final Color TEXT_DARK = new Color(26, 26, 26);
    private static final Color TEXT_MUTED = new Color(90, 90, 82);

    public byte[] generateInquiryPdf(ContactInquiry inquiry) {
        Document document = new Document(PageSize.A4, 50, 50, 40, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            addHeader(document);
            addTitle(document);
            addDetailsTable(document, inquiry);
            addVisionSection(document, inquiry);
            addFooter(document);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }

        return out.toByteArray();
    }

    private void addHeader(Document document) throws DocumentException {
        Font brandFont = new Font(Font.HELVETICA, 20, Font.BOLD, BRAND_GOLD);
        Font taglineFont = new Font(Font.HELVETICA, 9, Font.NORMAL, TEXT_MUTED);

        Paragraph brand = new Paragraph("RINGS & PROMISES", brandFont);
        brand.setAlignment(Element.ALIGN_CENTER);
        document.add(brand);

        Paragraph tagline = new Paragraph("EVENT MANAGEMENT — WEDDINGS & CELEBRATIONS", taglineFont);
        tagline.setAlignment(Element.ALIGN_CENTER);
        tagline.setSpacingAfter(20);
        document.add(tagline);

        LineSeparator line = new LineSeparator();
        line.setLineColor(BRAND_GOLD);
        line.setLineWidth(1f);
        document.add(new Chunk(line));
        document.add(Chunk.NEWLINE);
    }

    private void addTitle(Document document) throws DocumentException {
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD, BRAND_GREEN);
        Paragraph title = new Paragraph("New Event Inquiry", titleFont);
        title.setSpacingAfter(4);
        document.add(title);

        Font dateFont = new Font(Font.HELVETICA, 9, Font.NORMAL, TEXT_MUTED);
        Paragraph submitted = new Paragraph(
            "Submitted on " + java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a")),
            dateFont
        );
        submitted.setSpacingAfter(18);
        document.add(submitted);
    }

    private void addDetailsTable(Document document, ContactInquiry inquiry) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.2f, 2f});
        table.setSpacingAfter(20);

        addRow(table, "Full Name", inquiry.getFullName());
        addRow(table, "Email", inquiry.getEmail());
        addRow(table, "Phone", inquiry.getPhone());
        addRow(table, "Event Type", inquiry.getEventType());
        addRow(table, "Preferred Date", nullSafe(inquiry.getEventDate()));
        addRow(table, "Guest Count", nullSafe(inquiry.getGuestCount()));
        addRow(table, "Venue Preference", nullSafe(inquiry.getVenuePreference()));
        addRow(table, "Budget Range", nullSafe(inquiry.getBudgetRange()));
        addRow(table, "Services Requested",
            inquiry.getServices() != null ? String.join(", ", inquiry.getServices()) : "—");

        document.add(table);
    }

    private void addRow(PdfPTable table, String label, String value) {
        Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD, TEXT_DARK);
        Font valueFont = new Font(Font.HELVETICA, 10, Font.NORMAL, TEXT_DARK);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBackgroundColor(LIGHT_BG);
        labelCell.setPadding(8);
        labelCell.setBorderColor(new Color(234, 230, 221));

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "—", valueFont));
        valueCell.setPadding(8);
        valueCell.setBorderColor(new Color(234, 230, 221));

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addVisionSection(Document document, ContactInquiry inquiry) throws DocumentException {
        if (inquiry.getVision() == null || inquiry.getVision().isBlank()) {
            return;
        }

        Font headingFont = new Font(Font.HELVETICA, 11, Font.BOLD, BRAND_GREEN);
        Paragraph heading = new Paragraph("Their Vision", headingFont);
        heading.setSpacingAfter(6);
        document.add(heading);

        Font bodyFont = new Font(Font.HELVETICA, 10, Font.NORMAL, TEXT_DARK);
        Paragraph vision = new Paragraph(inquiry.getVision(), bodyFont);
        vision.setSpacingAfter(20);
        document.add(vision);
    }

    private void addFooter(Document document) throws DocumentException {
        LineSeparator line = new LineSeparator();
        line.setLineColor(new Color(234, 230, 221));
        line.setLineWidth(1f);
        document.add(new Chunk(line));
        document.add(Chunk.NEWLINE);

        Font footerFont = new Font(Font.HELVETICA, 8, Font.NORMAL, TEXT_MUTED);
        Paragraph footer = new Paragraph(
            "Rings & Promises  •  hello@ringsandpromises.com  •  +91 86107 35440  •  Tirunelveli, Tamil Nadu, India",
            footerFont
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    private String nullSafe(String value) {
        return (value == null || value.isBlank()) ? "Not specified" : value;
    }
}