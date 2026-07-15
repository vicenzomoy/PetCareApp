package edu.uph.m24si2.petcareapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.uph.m24si2.petcareapp.model.Booking;

@Dao
public interface BookingDao {

    @Insert
    long insert(Booking booking);

    @Update
    void update(Booking booking);

    @Delete
    void delete(Booking booking);

    @Query("SELECT * FROM booking ORDER BY id DESC")
    List<Booking> getAllBooking();

    @Query("SELECT * FROM booking WHERE id=:id")
    Booking getBookingById(int id);

    @Query("SELECT * FROM booking")
    List<Booking> getAllBookings();

    @Query("SELECT * FROM booking WHERE userId=:userId ORDER BY id DESC")
    List<Booking> getBookingByUser(int userId);
}