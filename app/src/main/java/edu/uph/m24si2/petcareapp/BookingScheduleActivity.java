package edu.uph.m24si2.petcareapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uph.m24si2.petcareapp.adapter.BookingAdapter;
import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.Booking;
import edu.uph.m24si2.petcareapp.model.BookingPetHotel;
import edu.uph.m24si2.petcareapp.model.Pet;

public class BookingScheduleActivity extends AppCompatActivity {

    private RecyclerView rvBooking;
    private View emptyState;

    private AppDatabase db;

    private List<Booking> bookingList = new ArrayList<>();
    private List<Pet> petList;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_schedule);

        rvBooking = findViewById(R.id.rvBooking);
        emptyState = findViewById(R.id.emptyState);

        db = AppDatabase.getDatabase(this);
        
        SharedPreferences preferences = getSharedPreferences("PetCare", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        loadBooking();
    }

    private void loadBooking() {
        bookingList.clear();
        
        // 1. Ambil semua hewan milik user untuk filtering
        petList = db.petDao().getPetByUser(userId);
        List<Integer> userPetIds = new ArrayList<>();
        for (Pet pet : petList) {
            userPetIds.add(pet.getId());
        }

        // 2. Ambil Booking Grooming & Home Service (dari tabel booking)
        List<Booking> allGeneralBookings = db.bookingDao().getAllBooking();
        for (Booking b : allGeneralBookings) {
            if (userPetIds.contains(b.getPetId())) {
                bookingList.add(b);
            }
        }

        // 3. Ambil Booking Pet Hotel (dari tabel booking_pet_hotel)
        List<BookingPetHotel> hotelBookings = db.bookingPetHotelDao().getAllBookings();
        for (BookingPetHotel hotel : hotelBookings) {
            if (userPetIds.contains(hotel.getPetId())) {
                // Konversi model PetHotel ke Booking agar bisa ditampilkan di adapter yang sama
                Booking b = new Booking();
                b.setPetId(hotel.getPetId());
                b.setService("Pet Hotel (" + hotel.getRoomType() + ")");
                b.setBookingDate("Check-in: " + hotel.getCheckInDate());
                b.setBookingTime("Check-out: " + hotel.getCheckOutDate());
                b.setStatus(hotel.getStatus());
                b.setPrice(hotel.getTotalPrice());
                bookingList.add(b);
            }
        }

        // Tampilkan ke RecyclerView
        if (bookingList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvBooking.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvBooking.setVisibility(View.VISIBLE);

            BookingAdapter adapter = new BookingAdapter(this, bookingList, petList);
            rvBooking.setLayoutManager(new LinearLayoutManager(this));
            rvBooking.setAdapter(adapter);
        }
    }
}
