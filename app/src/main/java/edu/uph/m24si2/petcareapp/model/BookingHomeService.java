package edu.uph.m24si2.petcareapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "booking_home_service",
        foreignKeys = @ForeignKey(
                entity = Booking.class,
                parentColumns = "id",
                childColumns = "bookingId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("bookingId")}
)
public class BookingHomeService {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int bookingId;

    private String address;

    private String landmark;

    private String phoneNumber;

    public BookingHomeService(int bookingId, String address, String landmark, String phoneNumber) {
        this.bookingId = bookingId;
        this.address = address;
        this.landmark = landmark;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}