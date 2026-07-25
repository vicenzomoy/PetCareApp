package edu.uph.m24si2.petcareapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.uph.m24si2.petcareapp.model.BookingPetHotel;

@Dao
public interface BookingPetHotelDao {

    @Insert
    void insertBooking(BookingPetHotel booking);

    @Update
    void updateBooking(BookingPetHotel booking);

    @Delete
    void deleteBooking(BookingPetHotel booking);

    @Query("SELECT * FROM booking_pet_hotel ORDER BY id DESC")
    List<BookingPetHotel> getAllBookings();

    @Query("SELECT * FROM booking_pet_hotel WHERE id = :bookingId")
    BookingPetHotel getBookingById(int bookingId);

    @Query("DELETE FROM booking_pet_hotel")
    void deleteAllBookings();

    @Query("SELECT * FROM booking_pet_hotel WHERE userId=:userId ORDER BY id DESC")
    List<BookingPetHotel> getBookingByUser(int userId);

    @Query("SELECT COUNT(*) FROM booking_pet_hotel WHERE petId = :petId AND NOT (checkOutDate < :checkIn OR checkInDate > :checkOut) AND status != 'Cancelled'")
    int checkOverlap(int petId, String checkIn, String checkOut);
}
