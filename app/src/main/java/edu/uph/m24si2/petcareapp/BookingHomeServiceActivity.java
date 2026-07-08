package edu.uph.m24si2.petcareapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.List;

import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.Booking;
import edu.uph.m24si2.petcareapp.model.BookingHomeService;
import edu.uph.m24si2.petcareapp.model.Pet;

public class BookingHomeServiceActivity extends AppCompatActivity {
    private Spinner spPet;
    private EditText etDate, etTime, etAddress, etLandmark, etPhone, etNotes;
    private Button btnBooking;
    private AppDatabase db;
    private List<Pet> petList;
    private SharedPreferences preferences;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_home_service);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getDatabase(this);

        // PREFERENCES
        preferences = getSharedPreferences("PetCare", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        spPet = findViewById(R.id.spPet);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etAddress = findViewById(R.id.etAddress);
        etLandmark = findViewById(R.id.etLandmark);
        etPhone = findViewById(R.id.etPhone);
        etNotes = findViewById(R.id.etNotes);
        btnBooking = findViewById(R.id.btnBooking);

        loadPet();
        setupListener();
    }

    private void loadPet() {

        petList = db.petDao().getPetByUser(userId);

        if (petList.isEmpty()) {

            Toast.makeText(this,
                    "Silakan tambahkan hewan terlebih dahulu.",
                    Toast.LENGTH_LONG).show();

            finish();

        }

        ArrayAdapter<Pet> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        petList
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spPet.setAdapter(adapter);

    }

    private void setupListener() {
        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());
        btnBooking.setOnClickListener(v -> validateBooking());
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    String date = selectedDay + "/"
                            + (selectedMonth + 1)
                            + "/"
                            + selectedYear;

                    etDate.setText(date);

                },
                year,
                month,
                day
        );

        // Ambil waktu sekarang
        Calendar minDate = Calendar.getInstance();

        // Jika sudah jam 21:00 atau lebih
        if (minDate.get(Calendar.HOUR_OF_DAY) >= 21) {
            minDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Set tanggal minimum
        dialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        dialog.show();

    }

    private void showTimePicker() {

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {

                    // Membatasi booking dari jam 8 pagi hingga jam 9 malam
                    if (selectedHour < 8 ||
                            selectedHour > 21 ||
                            (selectedHour == 21 && selectedMinute > 0)) {

                        Toast.makeText(this,
                                "Jam booking hanya 08:00 - 21:00",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String time = String.format(
                            "%02d:%02d",
                            selectedHour,
                            selectedMinute
                    );

                    etTime.setText(time);

                },
                hour,
                minute,
                true
        );

        dialog.show();

    }

    private void validateBooking() {

        if (petList.isEmpty()) {

            Toast.makeText(
                    this,
                    "Silakan tambahkan hewan terlebih dahulu.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String landmark = etLandmark.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (date.isEmpty()) {

            Toast.makeText(this, "Tanggal harus dipilih", Toast.LENGTH_SHORT).show();
            return;
        }

        if (time.isEmpty()) {

            Toast.makeText(this, "Jam harus dipilih", Toast.LENGTH_SHORT).show();
            return;
        }

        if (address.isEmpty()) {

            Toast.makeText(this, "Alamat wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (landmark.isEmpty()) {

            Toast.makeText(this, "Patokan rumah wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.isEmpty()) {

            Toast.makeText(this, "Nomor HP wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.matches("^08[0-9]{8,11}$")) {

            Toast.makeText(this, "Nomor HP tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        goToSummary();

    }

    private void goToSummary() {

        Pet pet = (Pet) spPet.getSelectedItem();

//        Intent intent = new Intent(
//                BookingHomeServiceActivity.this,
//                BookingSummaryActivity.class
//        );
//
//        intent.putExtra("petId", pet.getId());
//        intent.putExtra("petName", pet.getName());
//        intent.putExtra("petType", pet.getType());
//
//        intent.putExtra("service", "Home Service");
//
//        intent.putExtra(
//                "bookingDate",
//                etDate.getText().toString().trim()
//        );
//
//        intent.putExtra(
//                "bookingTime",
//                etTime.getText().toString().trim()
//        );
//
//        intent.putExtra(
//                "address",
//                etAddress.getText().toString().trim()
//        );
//
//        intent.putExtra(
//                "landmark",
//                etLandmark.getText().toString().trim()
//        );
//
//        intent.putExtra(
//                "phone",
//                etPhone.getText().toString().trim()
//        );
//
//        intent.putExtra(
//                "notes",
//                etNotes.getText().toString().trim()
//        );
//
//        intent.putExtra("price", 100000);
//
//        startActivity(intent);

    }
}