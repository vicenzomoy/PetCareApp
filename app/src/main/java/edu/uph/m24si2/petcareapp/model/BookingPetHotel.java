package edu.uph.m24si2.petcareapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "booking_pet_hotel")
public class BookingPetHotel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int petId;

    private String petName;

    private String roomType;

    private String checkInDate;

    private String checkOutDate;

    private int totalDays;

    private int roomPrice;

    private int totalPrice;

    private String note;

    private String status;

    private String bookingCode;

    private String createdAt;

    public BookingPetHotel() {
    }

    public BookingPetHotel(int petId,
                           String petName,
                           String roomType,
                           String checkInDate,
                           String checkOutDate,
                           int totalDays,
                           int roomPrice,
                           int totalPrice,
                           String note,
                           String status,
                           String bookingCode,
                           String createdAt) {

        this.petId = petId;
        this.petName = petName;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalDays = totalDays;
        this.roomPrice = roomPrice;
        this.totalPrice = totalPrice;
        this.note = note;
        this.status = status;
        this.bookingCode = bookingCode;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }


    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }


    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }


    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }


    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }


    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }


    public int getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(int roomPrice) {
        this.roomPrice = roomPrice;
    }


    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    private String branchName;

    private String branchAddress;

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}