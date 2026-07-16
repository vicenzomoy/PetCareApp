package edu.uph.m24si2.petcareapp.database;

import android.content.Context;

import edu.uph.m24si2.petcareapp.database.dao.BookingHomeServiceDao;
import edu.uph.m24si2.petcareapp.model.BookingHomeService;
import edu.uph.m24si2.petcareapp.model.BookingPetHotel;
import edu.uph.m24si2.petcareapp.database.dao.BookingDao;
import edu.uph.m24si2.petcareapp.model.Booking;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import edu.uph.m24si2.petcareapp.database.dao.PetDao;
import edu.uph.m24si2.petcareapp.database.dao.UserDao;
import edu.uph.m24si2.petcareapp.model.Pet;
import edu.uph.m24si2.petcareapp.model.User;

import edu.uph.m24si2.petcareapp.database.dao.BookingPetHotelDao;


@Database(
        entities = {
                User.class,
                Pet.class,
                Booking.class,
                BookingPetHotel.class,
                BookingHomeService.class
        },
        version = 6
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract PetDao petDao();

    public abstract BookingDao bookingDao();

    public abstract BookingPetHotelDao bookingPetHotelDao();

    public abstract BookingHomeServiceDao bookingHomeServiceDao();

    public static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "petcareDB"
            )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}