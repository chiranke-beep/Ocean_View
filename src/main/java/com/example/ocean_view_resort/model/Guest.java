package com.example.ocean_view_resort.model;

import java.time.LocalDateTime;

public class Guest {
    private int guestId;
    private String name;
    private String address;
    private String contactNumber;
    private LocalDateTime createdAt;

    // Constructor
    public Guest() {}

    public Guest(int guestId, String name, String address, String contactNumber, LocalDateTime createdAt) {
        this.guestId = guestId;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestId=" + guestId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
