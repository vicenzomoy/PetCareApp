package edu.uph.m24si2.petcareapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.uph.m24si2.petcareapp.model.BookingGrooming;

@Dao
public interface BookingGroomingDao {

    @Insert
    long insert(BookingGrooming booking);

    @Update
    void update(BookingGrooming booking);

    @Delete
    void delete(BookingGrooming booking);

    @Query("SELECT * FROM booking_grooming ORDER BY id DESC")
    List<BookingGrooming> getAllBooking();

    @Query("SELECT * FROM booking_grooming WHERE id=:id")
    BookingGrooming getBookingById(int id);

    @Query("SELECT * FROM booking_grooming")
    List<BookingGrooming> getAllBookings();

    @Query("SELECT * FROM booking_grooming WHERE userId=:userId ORDER BY id DESC")
    List<BookingGrooming> getBookingByUser(int userId);

    @Query("SELECT COUNT(*) FROM booking_grooming WHERE petId = :petId AND bookingDate = :date AND bookingTime = :time AND status != 'Cancelled'")
    int checkDuplicate(int petId, String date, String time);
}