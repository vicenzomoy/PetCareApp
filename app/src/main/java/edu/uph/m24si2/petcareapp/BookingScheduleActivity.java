package edu.uph.m24si2.petcareapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.uph.m24si2.petcareapp.adapter.BookingAdapter;
import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.BookingGrooming;
import edu.uph.m24si2.petcareapp.model.BookingHomeService;
import edu.uph.m24si2.petcareapp.model.BookingItem;
import edu.uph.m24si2.petcareapp.model.BookingPetHotel;
import edu.uph.m24si2.petcareapp.model.Pet;

public class BookingScheduleActivity extends AppCompatActivity implements BookingAdapter.OnBookingActionListener {

    private RecyclerView rvBooking;
    private View emptyState;
    private TabLayout tabLayout;

    private AppDatabase db;

    private final List<BookingItem> upcomingList = new ArrayList<>();
    private final List<BookingItem> pastList = new ArrayList<>();
    private List<Pet> petList;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_schedule);

        rvBooking = findViewById(R.id.rvBooking);
        emptyState = findViewById(R.id.emptyState);
        tabLayout = findViewById(R.id.tabLayout);

        db = AppDatabase.getDatabase(this);
        
        SharedPreferences preferences = getSharedPreferences("PetCare", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        setupTabs();
        loadBooking();
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateList(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadBooking() {
        upcomingList.clear();
        pastList.clear();
        
        petList = db.petDao().getPetByUser(userId);
        List<Integer> userPetIds = new ArrayList<>();
        for (Pet pet : petList) {
            userPetIds.add(pet.getId());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // 1. Grooming
        List<BookingGrooming> allGeneralBookings = db.bookingGroomingDao().getAllBooking();
        for (BookingGrooming b : allGeneralBookings) {
            if (userPetIds.contains(b.getPetId())) {
                BookingItem item = new BookingItem(b.getId(), b.getPetId(), "Grooming: " + b.getService(),
                        b.getBookingDate(), b.getBookingTime(), b.getPrice(),
                        b.getStatus(), b, "Grooming", b.getRating());
                if (isPastDate(b.getBookingDate(), sdf, today)) {
                    pastList.add(item);
                } else {
                    upcomingList.add(item);
                }
            }
        }

        // 2. Pet Hotel
        List<BookingPetHotel> hotelBookings = db.bookingPetHotelDao().getAllBookings();
        for (BookingPetHotel hotel : hotelBookings) {
            if (userPetIds.contains(hotel.getPetId())) {
                BookingItem item = new BookingItem(
                        hotel.getId(), hotel.getPetId(), "Pet Hotel (" + hotel.getRoomType() + ")",
                        "In: " + hotel.getCheckInDate(), "Out: " + hotel.getCheckOutDate(), hotel.getTotalPrice(),
                        hotel.getStatus(), hotel, "PetHotel", hotel.getRating()
                );

                if (isPastDate(hotel.getCheckInDate(), sdf, today)) {
                    pastList.add(item);
                } else {
                    upcomingList.add(item);
                }
            }
        }

        // 3. Home Service
        List<BookingHomeService> homeBookings = db.bookingHomeServiceDao().getAllHomeServiceBooking();
        for (BookingHomeService home : homeBookings) {
            if (userPetIds.contains(home.getPetId())) {
                BookingItem item = new BookingItem(
                        home.getId(), home.getPetId(), "Home Service",
                        home.getBookingDate(), home.getBookingTime(), home.getPrice(),
                        home.getStatus(), home, "HomeService", home.getRating()
                );

                if (isPastDate(home.getBookingDate(), sdf, today)) {
                    pastList.add(item);
                } else {
                    upcomingList.add(item);
                }
            }
        }

        // Default view: Current selected tab
        updateList(tabLayout.getSelectedTabPosition());
    }

    private boolean isPastDate(String dateStr, SimpleDateFormat sdf, Calendar today) {
        try {
            // Remove prefixes if present
            String cleanDate = dateStr.replace("Check-in: ", "").trim();
            Date date = sdf.parse(cleanDate);
            if (date != null) {
                return date.before(today.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateList(int tabPosition) {
        List<BookingItem> currentList = (tabPosition == 0) ? upcomingList : pastList;

        if (currentList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvBooking.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvBooking.setVisibility(View.VISIBLE);

            BookingAdapter adapter = new BookingAdapter(this, currentList, petList, this);
            rvBooking.setLayoutManager(new LinearLayoutManager(this));
            rvBooking.setAdapter(adapter);
        }
    }

    @Override
    public void onComplete(BookingItem item) {
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
        loadBooking();
    }
}
