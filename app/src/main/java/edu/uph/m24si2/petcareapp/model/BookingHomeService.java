package edu.uph.m24si2.petcareapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "booking_home_service",
        foreignKeys =  {

                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                ),

                @ForeignKey(
                        entity = Pet.class,
                        parentColumns = "id",
                        childColumns = "petId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("userId"),
                @Index("petId")
        }
    )
public class BookingHomeService {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;

    private String address;

    private String landmark;

    private String phoneNumber;

    public BookingHomeService(int userId, String address, String landmark, String phoneNumber) {
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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