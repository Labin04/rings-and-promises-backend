package com.ringsandpromises.backend.contact;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "contact_inquiries")
public class ContactInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String phone;
    private String eventType;
    private String eventDate;
    private String guestCount;
    private String venuePreference;
    private String budgetRange;

    @ElementCollection
    private List<String> services;

    @Column(length = 2000)
    private String vision;

    private LocalDateTime submittedAt;

    public ContactInquiry() {
        this.submittedAt = LocalDateTime.now();
    }

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getGuestCount() { return guestCount; }
    public void setGuestCount(String guestCount) { this.guestCount = guestCount; }

    public String getVenuePreference() { return venuePreference; }
    public void setVenuePreference(String venuePreference) { this.venuePreference = venuePreference; }

    public String getBudgetRange() { return budgetRange; }
    public void setBudgetRange(String budgetRange) { this.budgetRange = budgetRange; }

    public List<String> getServices() { return services; }
    public void setServices(List<String> services) { this.services = services; }

    public String getVision() { return vision; }
    public void setVision(String vision) { this.vision = vision; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}