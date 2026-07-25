package edu.uph.m24si2.petcareapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();
        
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());

        // Load Grooming
        List<BookingGrooming> groomingList = db.bookingGroomingDao().getBookingByUser(userId);
        for (BookingGrooming b : groomingList) {
            // Cek apakah tanggal sudah lewat
            if (!"Selesai".equals(b.getStatus()) && isPastDate(b.getBookingDate(), sdf, today)) {
                b.setStatus("Selesai");
                db.bookingGroomingDao().update(b);
            }
            
            // Hanya tampilkan yang statusnya Selesai
            if ("Selesai".equals(b.getStatus())) {
                bookingItems.add(new BookingItem(
                        b.getId(), b.getPetId(), "Grooming: " + b.getService(),
                        b.getBookingDate(), b.getBookingTime(), b.getPrice(),
                        b.getStatus(), b, "Grooming", b.getRating()
                ));
            }
        }

        // Load Pet Hotel
        List<BookingPetHotel> hotelList = db.bookingPetHotelDao().getBookingByUser(userId);
        for (BookingPetHotel b : hotelList) {
            // Cek apakah tanggal sudah lewat (Check out)
            if (!"Selesai".equals(b.getStatus()) && isPastDate(b.getCheckOutDate(), sdf, today)) {
                b.setStatus("Selesai");
                db.bookingPetHotelDao().updateBooking(b);
            }

            // Hanya tampilkan yang statusnya Selesai
            if ("Selesai".equals(b.getStatus())) {
                bookingItems.add(new BookingItem(
                        b.getId(), b.getPetId(), "Pet Hotel: " + b.getRoomType(),
                        "In: " + b.getCheckInDate(), "Out: " + b.getCheckOutDate(), b.getTotalPrice(),
                        b.getStatus(), b, "PetHotel", b.getRating()
                ));
            }
        }

        // Load Home Service
        List<BookingHomeService> homeList = db.bookingHomeServiceDao().getByUser(userId);
        for (BookingHomeService b : homeList) {
            // Cek apakah tanggal sudah lewat
            if (!"Selesai".equals(b.getStatus()) && isPastDate(b.getBookingDate(), sdf, today)) {
                b.setStatus("Selesai");
                db.bookingHomeServiceDao().update(b);
            }

            // Hanya tampilkan yang statusnya Selesai
            if ("Selesai".equals(b.getStatus())) {
                bookingItems.add(new BookingItem(
                        b.getId(), b.getPetId(), "Home Service",
                        b.getBookingDate(), b.getBookingTime(), b.getPrice(),
                        b.getStatus(), b, "HomeService", b.getRating()
                ));
            }
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

    private boolean isPastDate(String dateStr, SimpleDateFormat sdf, Date today) {
        if (dateStr == null || dateStr.isEmpty()) return false;
        try {
            Date date = sdf.parse(dateStr);
            return date != null && date.before(today);
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public void onComplete(BookingItem item) {
        // Handle completion based on type
        if (item.getType().equals("Grooming")) {
            BookingGrooming b = (BookingGrooming) item.getOriginalObject();
            b.setStatus("Selesai");
            db.bookingGroomingDao().update(b);
        } else if (item.getType().equals("PetHotel")) {
            BookingPetHotel b = (BookingPetHotel) item.getOriginalObject();
            b.setStatus("Selesai");
            db.bookingPetHotelDao().updateBooking(b);
        } else if (item.getType().equals("HomeService")) {
            BookingHomeService b = (BookingHomeService) item.getOriginalObject();
            b.setStatus("Selesai");
            db.bookingHomeServiceDao().update(b);
        }

        Toast.makeText(this, "Booking diselesaikan!", Toast.LENGTH_SHORT).show();
        loadData(); // Refresh list
    }

    @Override
    public void onRate(BookingItem item) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_rating, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        
        new AlertDialog.Builder(this)
                .setTitle("Beri Rating")
                .setView(dialogView)
                .setPositiveButton("Simpan", (dialog, which) -> {
                    float rating = ratingBar.getRating();
                    saveRating(item, rating);
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void saveRating(BookingItem item, float rating) {
        if (item.getType().equals("Grooming")) {
            BookingGrooming b = (BookingGrooming) item.getOriginalObject();
            b.setRating(rating);
            db.bookingGroomingDao().update(b);
        } else if (item.getType().equals("PetHotel")) {
            BookingPetHotel b = (BookingPetHotel) item.getOriginalObject();
            b.setRating(rating);
            db.bookingPetHotelDao().updateBooking(b);
        } else if (item.getType().equals("HomeService")) {
            BookingHomeService b = (BookingHomeService) item.getOriginalObject();
            b.setRating(rating);
            db.bookingHomeServiceDao().update(b);
        }

        Toast.makeText(this, "Terima kasih atas rating Anda!", Toast.LENGTH_SHORT).show();
        loadData();
    }
}
