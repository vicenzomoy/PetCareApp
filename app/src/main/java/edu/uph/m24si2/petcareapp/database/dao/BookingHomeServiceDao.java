package edu.uph.m24si2.petcareapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.uph.m24si2.petcareapp.model.BookingHomeService;

@Dao
public interface BookingHomeServiceDao {
    @Insert
    long insert(BookingHomeService bookingHomeService);

    @Update
    void update(BookingHomeService bookingHomeService);

    @Delete
    void delete(BookingHomeService bookingHomeService);

    @Query("SELECT * FROM booking_home_service ORDER BY id DESC")
    List<BookingHomeService> getAllHomeServiceBooking();

    @Query("SELECT * FROM booking_home_service WHERE id = :id")
    BookingHomeService getById(int id);

    @Query("SELECT * FROM booking_home_service WHERE userId=:userId ORDER BY id DESC")
    List<BookingHomeService> getByUser(int userId);
}
