package com.ringsandpromises.backend.contact;

import jakarta.validation.constraints.*;
import java.util.List;

public class ContactRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9\\s-]{7,15}$", message = "Please provide a valid phone number")
    private String phone;

    @NotBlank(message = "Event type is required")
    private String eventType;

    private String eventDate;

    private String guestCount;

    private String venuePreference;

    private String budgetRange;

    private List<String> services;

    @Size(max = 2000, message = "Vision text must be under 2000 characters")
    private String vision;

    // Getters and setters

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
}