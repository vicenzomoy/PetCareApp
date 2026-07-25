package edu.uph.m24si2.petcareapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.BookingRequest;
import edu.uph.m24si2.petcareapp.model.Branch;
import edu.uph.m24si2.petcareapp.model.Pet;
import edu.uph.m24si2.petcareapp.util.BookingType;

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
    private ImageView btnBack;

    // Database
    private AppDatabase db;

    // Data
    private List<Pet> petList;
    private Pet selectedPet;
    private List<Branch> branchList;
    private Branch selectedBranch;
    private AutoCompleteTextView autoBranch;
    private MaterialCardView cardBranchInfo;
    private TextView tvBranchAddress;
    private TextView tvBranchRating;
    private TextView tvBranchRoom;
    private TextView tvBranchOpenHour;

    // Date
    private Calendar checkInCalendar;
    private Calendar checkOutCalendar;

    // Price
    private int roomPrice;
    private int totalDays;
    private int totalPrice;

    private SharedPreferences preferences;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_pet_hotel);

        db = AppDatabase.getDatabase(this);

        // PREFERENCES
        preferences = getSharedPreferences("PetCare", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        initView();
        loadPets();
        setupBranch();
        setupDatePicker();
        setupRoomType();

        btnBack.setOnClickListener(v -> finish());
        btnBooking.setOnClickListener(v -> saveBooking());
    }

    private void initView() {

        cardBranchInfo = findViewById(R.id.cardBranchInfo);

        autoPet = findViewById(R.id.autoPet);

        autoBranch = findViewById(R.id.autoBranch);

        tvBranchAddress = findViewById(R.id.tvBranchAddress);
        tvBranchRating = findViewById(R.id.tvBranchRating);
        tvBranchRoom = findViewById(R.id.tvBranchRoom);
        tvBranchOpenHour = findViewById(R.id.tvBranchOpenHour);

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
        btnBack = findViewById(R.id.btnBack);
    }

    private void loadPets() {

        db = AppDatabase.getDatabase(this);

        petList = db.petDao().getPetByUser(userId);

        if (petList.isEmpty()) {
            Toast.makeText(this, "Silakan tambahkan hewan terlebih dahulu.", Toast.LENGTH_LONG).show();
            finish();
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

    private void setupBranch() {

        branchList = new ArrayList<>();

        branchList.add(
                new Branch(
                        "PetCare Medan Centre",
                        "Jl. Gatot Subroto No.215",
                        4.9f,
                        8,
                        5,
                        2,
                        "08.00 - 20.00"
                )
        );

        branchList.add(
                new Branch(
                        "PetCare Medan Sunggal",
                        "Jl. Sunggal No. 125, Medan Sunggal",
                        4.8f,
                        9,
                        5,
                        2,
                        "08.00 - 20.00 WIB"
                )
        );

        branchList.add(
                new Branch(
                        "PetCare Medan Helvetia",
                        "Jl. Kapten Muslim No. 98, Medan Helvetia",
                        4.7f,
                        7,
                        4,
                        2,
                        "08.00 - 20.00 WIB"
                )
        );

        branchList.add(
                new Branch(
                        "PetCare Medan Polonia",
                        "Jl. Polonia Raya No. 20, Medan Polonia",
                        4.9f,
                        6,
                        5,
                        4,
                        "08.00 - 20.00 WIB"
                )
        );

        branchList.add(
                new Branch(
                        "PetCare Medan Petisah",
                        "Jl. Iskandar Muda No. 143, Medan Petisah",
                        4.8f,
                        8,
                        6,
                        3,
                        "08.00 - 20.00 WIB"
                )
        );

        List<String> branchNames = new ArrayList<>();
        for(Branch branch : branchList){

            branchNames.add(branch.getName());

        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        branchNames
                );

        autoBranch.setAdapter(adapter);

        autoBranch.setOnClickListener(v->autoBranch.showDropDown());


        autoBranch.setOnItemClickListener((parent, view, position, id)->{

            selectedBranch = branchList.get(position);

            cardBranchInfo.setVisibility(View.VISIBLE);

            tvBranchAddress.setText(
                    "📍 " + selectedBranch.getAddress());

            tvBranchRating.setText(
                    "⭐ " + selectedBranch.getRating());

            tvBranchRoom.setText(
                    "Standard : "
                            + selectedBranch.getStandardRoom()
                            + "\nDeluxe : "
                            + selectedBranch.getDeluxeRoom()
                            + "\nVIP : "
                            + selectedBranch.getVipRoom());

            tvBranchOpenHour.setText(
                    "Jam Operasional : "
                            + selectedBranch.getOpenHour());

        });
    }

    private void setupDatePicker() {

        checkInCalendar = Calendar.getInstance();
        checkOutCalendar = Calendar.getInstance();

        etCheckIn.setOnClickListener(v -> showCheckInPicker());

        etCheckOut.setOnClickListener(v -> {

            if (etCheckIn.getText().toString().isEmpty()) {

                Toast.makeText(
                        this,
                        "Silakan pilih tanggal Check In terlebih dahulu",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            showCheckOutPicker();

        });
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

                    // Reset Check Out
                    etCheckOut.setText("");

                    checkOutCalendar = Calendar.getInstance();

                    totalDays = 0;
                    totalPrice = 0;

                    tvTotalDays.setText("Lama Menginap : 0 Malam");
                    tvPrice.setText("Harga / Malam : Rp " + roomPrice);
                    tvTotalPrice.setText("Total : Rp0");

                    calculateTotal();

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.getDatePicker().setMinDate(checkInCalendar.getTimeInMillis());

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
        Calendar minCheckOut = (Calendar) checkInCalendar.clone();
        minCheckOut.add(Calendar.DAY_OF_MONTH, 1);

        dialog.getDatePicker().setMinDate(
                minCheckOut.getTimeInMillis()
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

        if (totalDays < 1) {

            Toast.makeText(
                    this,
                    "Tanggal Check Out minimal 1 hari setelah Check In",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        totalPrice = totalDays * roomPrice;

        tvTotalDays.setText("Lama Menginap : " + totalDays + " Night");

        tvPrice.setText("Harga / Malam : Rp " + roomPrice);

        tvTotalPrice.setText("Total : Rp " + totalPrice);

    }

    private void saveBooking() {

        if (selectedPet == null) {
            Toast.makeText(this,
                    "Silakan pilih hewan peliharaan",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedBranch == null) {
            Toast.makeText(this,
                    "Silakan pilih lokasi Pet Hotel",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (roomPrice == 0) {
            Toast.makeText(this,
                    "Pilih tipe kamar",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (etCheckIn.getText().toString().isEmpty()) {
            Toast.makeText(this,
                    "Pilih tanggal Check In",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (etCheckOut.getText().toString().isEmpty()) {
            Toast.makeText(this,
                    "Pilih tanggal Check Out",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Cek overlap booking hotel
        String checkIn = etCheckIn.getText().toString();
        String checkOut = etCheckOut.getText().toString();
        int count = db.bookingPetHotelDao().checkOverlap(selectedPet.getId(), checkIn, checkOut);
        if (count > 0) {
            Toast.makeText(this, "Hewan ini sudah terdaftar di hotel pada tanggal tersebut!", Toast.LENGTH_LONG).show();
            return;
        }

        String roomType;

        if (rbStandard.isChecked()) {
            roomType = "Standard";
        } else if (rbDeluxe.isChecked()) {
            roomType = "Deluxe";
        } else {
            roomType = "VIP";
        }

        BookingRequest request = new BookingRequest();

        request.setBookingType(BookingType.PET_HOTEL);

        request.setPetId(selectedPet.getId());
        request.setPetName(selectedPet.getName());
        request.setPetType(selectedPet.getType());

        request.setRoomType(roomType);

        request.setCheckInDate(etCheckIn.getText().toString());

        request.setCheckOutDate(etCheckOut.getText().toString());

        request.setTotalDays(totalDays);

        request.setNotes(etNote.getText().toString());

        request.setPrice(totalPrice);

        // Branch
        request.setBranchName(selectedBranch.getName());
        request.setBranchAddress(selectedBranch.getAddress());

        Intent intent =
                new Intent(
                        BookingPetHotelActivity.this,
                        BookingSummaryActivity.class);

        intent.putExtra("booking", request);

        startActivity(intent);

    }
}
