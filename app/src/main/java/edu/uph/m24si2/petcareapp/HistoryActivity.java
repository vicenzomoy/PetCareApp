package edu.uph.m24si2.petcareapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uph.m24si2.petcareapp.adapter.HistoryAdapter;
import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.BookingGrooming;
import edu.uph.m24si2.petcareapp.model.BookingHomeService;
import edu.uph.m24si2.petcareapp.model.BookingItem;
import edu.uph.m24si2.petcareapp.model.BookingPetHotel;
import edu.uph.m24si2.petcareapp.model.Pet;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.OnHistoryActionListener {

    RecyclerView rvHistory;
    TextView tvEmpty;
    ImageButton btnBack;

    AppDatabase db;
    SharedPreferences preferences;
    int userId;
    
    HistoryAdapter adapter;
    List<BookingItem> bookingItems = new ArrayList<>();
    List<Pet> pets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = AppDatabase.getDatabase(this);
        preferences = getSharedPreferences("PetCare", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        rvHistory = findViewById(R.id.rvHistory);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        
        loadData();
    }

    private void loadData() {
        bookingItems.clear();
        
        // Load Pets for name lookup
        pets = db.petDao().getPetByUser(userId);

        // Load Grooming
        List<BookingGrooming> groomingList = db.bookingGroomingDao().getBookingByUser(userId);
        for (BookingGrooming b : groomingList) {
            bookingItems.add(new BookingItem(
                    b.getId(), b.getPetId(), "Grooming: " + b.getService(),
                    b.getBookingDate(), b.getBookingTime(), b.getPrice(),
                    b.getStatus(), b, "Grooming"
            ));
        }

        // Load Pet Hotel
        List<BookingPetHotel> hotelList = db.bookingPetHotelDao().getBookingByUser(userId);
        for (BookingPetHotel b : hotelList) {
            bookingItems.add(new BookingItem(
                    b.getId(), b.getPetId(), "Pet Hotel: " + b.getRoomType(),
                    "In: " + b.getCheckInDate(), "Out: " + b.getCheckOutDate(), b.getTotalPrice(),
                    b.getStatus(), b, "PetHotel"
            ));
        }

        // Load Home Service
        List<BookingHomeService> homeList = db.bookingHomeServiceDao().getByUser(userId);
        for (BookingHomeService b : homeList) {
            bookingItems.add(new BookingItem(
                    b.getId(), b.getPetId(), "Home Service",
                    b.getBookingDate(), b.getBookingTime(), b.getPrice(),
                    b.getStatus(), b, "HomeService"
            ));
        }

        // Sort by ID descending (assuming higher ID is newer)
        Collections.sort(bookingItems, (o1, o2) -> o2.getId() - o1.getId());

        if (bookingItems.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
            adapter = new HistoryAdapter(this, bookingItems, pets, this);
            rvHistory.setAdapter(adapter);
        }
    }

    @Override
    public void onComplete(BookingItem item) {
        // Handle completion based on type
        if (item.getType().equals("Grooming")) {
            BookingGrooming b = (BookingGrooming) item.getOriginalObject();
            b.setStatus("Completed");
            db.bookingGroomingDao().update(b);
        } else if (item.getType().equals("PetHotel")) {
            BookingPetHotel b = (BookingPetHotel) item.getOriginalObject();
            b.setStatus("Completed");
            db.bookingPetHotelDao().updateBooking(b);
        } else if (item.getType().equals("HomeService")) {
            BookingHomeService b = (BookingHomeService) item.getOriginalObject();
            b.setStatus("Completed");
            db.bookingHomeServiceDao().update(b);
        }

        Toast.makeText(this, "Booking diselesaikan!", Toast.LENGTH_SHORT).show();
        loadData(); // Refresh list
    }

    @Override
    public void onRate(BookingItem item) {
        // Placeholder for Rating Feature
        Toast.makeText(this, "Fitur Rating akan segera datang!", Toast.LENGTH_SHORT).show();
    }
}
