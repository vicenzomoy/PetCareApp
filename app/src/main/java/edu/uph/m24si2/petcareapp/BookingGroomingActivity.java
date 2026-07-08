package edu.uph.m24si2.petcareapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.Pet;
import edu.uph.m24si2.petcareapp.model.Booking;

public class BookingGroomingActivity extends AppCompatActivity {

    private AutoCompleteTextView dropPet;
    private AutoCompleteTextView dropService;

    private Button btnDate, btnTime, btnBooking;

    private EditText etNotes;

    private AppDatabase db;

    private List<Pet> petList = new ArrayList<>();

    private String selectedDate = "";
    private String selectedTime = "";
    private SharedPreferences preferences;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_grooming);

        // PREFERENCES
        preferences = getSharedPreferences("PetCare", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        init();

        loadPet();

        loadService();

        btnDate.setOnClickListener(v -> showDatePicker());

        btnTime.setOnClickListener(v -> showTimePicker());

        btnBooking.setOnClickListener(v -> saveBooking());
    }

    private void init(){

        dropPet = findViewById(R.id.dropPet);

        dropService = findViewById(R.id.dropService);

        btnDate = findViewById(R.id.btnDate);

        btnTime = findViewById(R.id.btnTime);

        btnBooking = findViewById(R.id.btnBooking);

        etNotes = findViewById(R.id.etNotes);

    }

    private void loadPet(){

        db = AppDatabase.getDatabase(this);
        petList = db.petDao().getPetByUser(userId);

        List<String> petNames = new ArrayList<>();

        for(Pet pet : petList){

            petNames.add(pet.getName());

        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        petNames);

        dropPet.setAdapter(adapter);

    }

    private void loadService(){

        String[] services = {

                "Basic Grooming",

                "Full Grooming",

                "Premium Grooming"

        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        services);

        dropService.setAdapter(adapter);

    }

    private void showDatePicker(){

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog =

                new DatePickerDialog(

                        this,

                        (view, year, month, day) -> {

                            selectedDate =
                                    day + "/" + (month + 1) + "/" + year;

                            btnDate.setText(selectedDate);

                        },

                        calendar.get(Calendar.YEAR),

                        calendar.get(Calendar.MONTH),

                        calendar.get(Calendar.DAY_OF_MONTH)

                );

        dialog.show();

    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(
                this,
                (view, hour, minute) -> {
                    selectedTime = hour + ":" + String.format(Locale.getDefault(), "%02d", minute);
                    btnTime.setText(selectedTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        dialog.show();
    }

    private void saveBooking() {
        if (dropPet.getText().toString().isEmpty()) {
            Toast.makeText(this, "Pilih hewan", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Pilih tanggal", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedTime.isEmpty()) {
            Toast.makeText(this, "Pilih jam", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dropService.getText().toString().isEmpty()) {
            Toast.makeText(this, "Pilih jenis grooming", Toast.LENGTH_SHORT).show();
            return;
        }

        int petId = 0;
        for (Pet pet : petList) {
            if (pet.getName().equals(dropPet.getText().toString())) {
                petId = pet.getId();
                break;
            }
        }

        int price = 0;
        switch (dropService.getText().toString()) {
            case "Basic Grooming":
                price = 75000;
                break;
            case "Full Grooming":
                price = 120000;
                break;
            case "Premium Grooming":
                price = 175000;
                break;
        }

        Intent intent = new Intent(
                BookingGroomingActivity.this,
                DetailBookingActivity.class
        );

        intent.putExtra("PET_ID", petId);
        intent.putExtra("PET_NAME", dropPet.getText().toString());
        intent.putExtra("SERVICE", dropService.getText().toString());
        intent.putExtra("DATE", selectedDate);
        intent.putExtra("TIME", selectedTime);
        intent.putExtra("NOTES", etNotes.getText().toString());
        intent.putExtra("PRICE", price);

        startActivity(intent);
    }
}
