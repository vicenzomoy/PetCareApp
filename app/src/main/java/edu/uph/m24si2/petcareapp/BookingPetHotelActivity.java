package edu.uph.m24si2.petcareapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.BookingPetHotel;
import edu.uph.m24si2.petcareapp.model.Pet;

public class BookingPetHotelActivity extends AppCompatActivity {

    // Konstanta Harga
    private static final int PRICE_STANDARD = 75000;
    private static final int PRICE_DELUXE = 120000;
    private static final int PRICE_VIP = 180000;

    // View
    private AutoCompleteTextView autoPet;
    private TextInputEditText etCheckIn;
    private TextInputEditText etCheckOut;
    private TextInputEditText etNote;

    private RadioGroup rgRoomType;
    private RadioButton rbStandard;
    private RadioButton rbDeluxe;
    private RadioButton rbVip;

    private TextView tvTotalDays;
    private TextView tvPrice;
    private TextView tvTotalPrice;

    private Button btnBooking;

    // Database
    private AppDatabase db;

    // Data
    private List<Pet> petList;
    private Pet selectedPet;

    // Date
    private Calendar checkInCalendar;
    private Calendar checkOutCalendar;

    // Price
    private int roomPrice;
    private int totalDays;
    private int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_pet_hotel);

        db = AppDatabase.getDatabase(this);

        initView();
        loadPets();
        setupDatePicker();
        setupRoomType();

        btnBooking.setOnClickListener(v -> saveBooking());
    }

    private void initView() {

        autoPet = findViewById(R.id.autoPet);

        etCheckIn = findViewById(R.id.etCheckIn);
        etCheckOut = findViewById(R.id.etCheckOut);
        etNote = findViewById(R.id.etNote);

        rgRoomType = findViewById(R.id.rgRoomType);

        rbStandard = findViewById(R.id.rbStandard);
        rbDeluxe = findViewById(R.id.rbDeluxe);
        rbVip = findViewById(R.id.rbVip);

        tvTotalDays = findViewById(R.id.tvTotalDays);
        tvPrice = findViewById(R.id.tvPrice);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        btnBooking = findViewById(R.id.btnBooking);
    }

    private void loadPets() {
        petList = db.petDao().getAllPets();

        if (petList.isEmpty()) {
            Toast.makeText(this, "No pets found. Please add a pet first.", Toast.LENGTH_LONG).show();
        }

        List<String> petNames = new ArrayList<>();
        for (Pet pet : petList) {
            petNames.add(pet.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                petNames
        );

        autoPet.setAdapter(adapter);

        // Munculkan dropdown saat diklik
        autoPet.setOnClickListener(v -> autoPet.showDropDown());

        autoPet.setOnItemClickListener((parent, view, position, id) -> {
            String selectedName = (String) parent.getItemAtPosition(position);
            for (Pet pet : petList) {
                if (pet.getName().equals(selectedName)) {
                    selectedPet = pet;
                    break;
                }
            }
        });
    }

    private void setupDatePicker() {

        checkInCalendar = Calendar.getInstance();
        checkOutCalendar = Calendar.getInstance();

        etCheckIn.setOnClickListener(v -> showCheckInPicker());

        etCheckOut.setOnClickListener(v -> showCheckOutPicker());

    }

    private void showCheckInPicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    checkInCalendar.set(year, month, dayOfMonth);

                    SimpleDateFormat sdf =
                            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    etCheckIn.setText(sdf.format(checkInCalendar.getTime()));

                    calculateTotal();

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();

    }

    private void showCheckOutPicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    checkOutCalendar.set(year, month, dayOfMonth);

                    SimpleDateFormat sdf =
                            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    etCheckOut.setText(sdf.format(checkOutCalendar.getTime()));

                    calculateTotal();

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();

    }

    private void setupRoomType() {

        rgRoomType.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.rbStandard) {
                roomPrice = PRICE_STANDARD;
            } else if (checkedId == R.id.rbDeluxe) {
                roomPrice = PRICE_DELUXE;
            } else if (checkedId == R.id.rbVip) {
                roomPrice = PRICE_VIP;
            }

            calculateTotal();

        });

    }

    private void calculateTotal() {

        if (roomPrice == 0) {
            return;
        }

        if (etCheckIn.getText().toString().isEmpty()) {
            return;
        }

        if (etCheckOut.getText().toString().isEmpty()) {
            return;
        }

        long diff =
                checkOutCalendar.getTimeInMillis()
                        - checkInCalendar.getTimeInMillis();

        totalDays = (int) TimeUnit.MILLISECONDS.toDays(diff);

        if (totalDays <= 0) {

            totalDays = 1;

        }

        totalPrice = totalDays * roomPrice;

        tvTotalDays.setText("Length of Stay : " + totalDays + " Night");

        tvPrice.setText("Price / Night : Rp " + roomPrice);

        tvTotalPrice.setText("Total : Rp " + totalPrice);

    }

    private String generateBookingCode() {
        return "PH" + System.currentTimeMillis();
    }

    private void saveBooking() {

        if (selectedPet == null) {
            Toast.makeText(this, "Please choose pet", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roomPrice == 0) {

            Toast.makeText(this,
                    "Choose room type",
                    Toast.LENGTH_SHORT).show();

            return;

        }

        if (etCheckIn.getText().toString().isEmpty()) {

            Toast.makeText(this,
                    "Choose check in",
                    Toast.LENGTH_SHORT).show();

            return;

        }

        if (etCheckOut.getText().toString().isEmpty()) {

            Toast.makeText(this,
                    "Choose check out",
                    Toast.LENGTH_SHORT).show();

            return;

        }

        BookingPetHotel booking = new BookingPetHotel();

        booking.setPetId(selectedPet.getId());

        booking.setPetName(selectedPet.getName());

        if (rbStandard.isChecked()) {

            booking.setRoomType("Standard");

        } else if (rbDeluxe.isChecked()) {

            booking.setRoomType("Deluxe");

        } else {

            booking.setRoomType("VIP");

        }

        booking.setCheckInDate(etCheckIn.getText().toString());

        booking.setCheckOutDate(etCheckOut.getText().toString());

        booking.setTotalDays(totalDays);

        booking.setRoomPrice(roomPrice);

        booking.setTotalPrice(totalPrice);

        booking.setNote(etNote.getText().toString());

        booking.setStatus("Pending");

        booking.setBookingCode(generateBookingCode());

        String createdAt =
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault())
                        .format(new Date());

        booking.setCreatedAt(createdAt);

        db.bookingPetHotelDao().insertBooking(booking);

        Toast.makeText(
                this,
                "Booking Success",
                Toast.LENGTH_LONG
        ).show();

        finish();

    }
}