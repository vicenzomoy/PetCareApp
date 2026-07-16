package edu.uph.m24si2.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.Locale;

import edu.uph.m24si2.petcareapp.model.BookingRequest;
import edu.uph.m24si2.petcareapp.util.BookingType;

public class BookingSummaryActivity extends AppCompatActivity {

    // Pet
    private TextView tvPetName;
    private TextView tvPetType;

    // Schedule
    private TextView tvDate;
    private TextView tvTime;

    // Home Service
    private MaterialCardView cardHome;
    private TextView tvAddress;
    private TextView tvLandmark;
    private TextView tvPhone;

    // Grooming
    private MaterialCardView cardGrooming;
    private TextView tvService;

    // Pet Hotel
    private MaterialCardView cardHotel;
    private TextView tvBranchName;
    private TextView tvBranchAddress;
    private TextView tvRoomType;
    private TextView tvCheckIn;
    private TextView tvCheckOut;
    private TextView tvTotalDays;

    // Notes
    private TextView tvNotes;

    // Price
    private TextView tvPrice;

    private Button btnPayment;

    private BookingRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        initView();

        request = (BookingRequest)
                getIntent().getSerializableExtra("booking");

        if (request == null) {
            finish();
            return;
        }

        loadData();

        btnPayment.setOnClickListener(v -> openPayment());
    }

    private void initView() {

        tvPetName = findViewById(R.id.tvPetName);
        tvPetType = findViewById(R.id.tvPetType);

        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);

        cardHome = findViewById(R.id.cardHome);
        tvAddress = findViewById(R.id.tvAddress);
        tvLandmark = findViewById(R.id.tvLandmark);
        tvPhone = findViewById(R.id.tvPhone);

        cardGrooming = findViewById(R.id.cardGrooming);
        tvService = findViewById(R.id.tvService);

        cardHotel = findViewById(R.id.cardHotel);
        tvBranchName = findViewById(R.id.tvBranchName);
        tvBranchAddress = findViewById(R.id.tvBranchAddress);
        tvRoomType = findViewById(R.id.tvRoomType);
        tvCheckIn = findViewById(R.id.tvCheckIn);
        tvCheckOut = findViewById(R.id.tvCheckOut);
        tvTotalDays = findViewById(R.id.tvTotalDays);

        tvNotes = findViewById(R.id.tvNotes);

        tvPrice = findViewById(R.id.tvPrice);

        btnPayment = findViewById(R.id.btnPayment);
    }

    private void loadData() {

        tvPetName.setText("Nama Hewan : " + request.getPetName());

        tvPetType.setText("Jenis : " + request.getPetType());

        tvNotes.setText(request.getNotes());

        tvPrice.setText(formatRupiah(request.getPrice()));

        switch (request.getBookingType()) {

            case BookingType.HOME_SERVICE:

                showHomeService();
                break;

            case BookingType.GROOMING:

                showGrooming();
                break;

            case BookingType.PET_HOTEL:

                showPetHotel();
                break;
        }

    }

    private void showHomeService() {

        cardHome.setVisibility(View.VISIBLE);
        cardGrooming.setVisibility(View.GONE);
        cardHotel.setVisibility(View.GONE);

        tvDate.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.VISIBLE);

        tvDate.setText("Tanggal : " + request.getBookingDate());

        tvTime.setText("Jam : " + request.getBookingTime());

        tvAddress.setText("Alamat :\n" + request.getAddress());

        tvLandmark.setText("Patokan : " + request.getLandmark());

        tvPhone.setText("No HP : " + request.getPhoneNumber());

    }

    private void showGrooming() {

        cardHome.setVisibility(View.GONE);
        cardGrooming.setVisibility(View.VISIBLE);
        cardHotel.setVisibility(View.GONE);

        tvDate.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.VISIBLE);

        tvDate.setText("Tanggal : " + request.getBookingDate());

        tvTime.setText("Jam : " + request.getBookingTime());

        tvService.setText("Layanan : " + request.getService());

    }

    private void showPetHotel() {

        cardHome.setVisibility(View.GONE);
        cardGrooming.setVisibility(View.GONE);
        cardHotel.setVisibility(View.VISIBLE);

        tvBranchName.setText("Cabang : " + request.getBranchName());

        tvBranchAddress.setText("Alamat : " + request.getBranchAddress());

        tvDate.setVisibility(View.GONE);
        tvTime.setVisibility(View.GONE);

        tvRoomType.setText("Room : " + request.getRoomType());

        tvCheckIn.setText("Check In : " + request.getCheckInDate());

        tvCheckOut.setText("Check Out : " + request.getCheckOutDate());

        tvTotalDays.setText(
                "Durasi : " +
                        request.getTotalDays() +
                        " Hari");

    }

    private String formatRupiah(int price) {

        NumberFormat format =
                NumberFormat.getCurrencyInstance(
                        new Locale("id", "ID"));

        return format.format(price);

    }

    private void openPayment() {

        Intent intent =
                new Intent(
                        BookingSummaryActivity.this,
                        PaymentActivity.class);

        intent.putExtra("booking", request);

        startActivity(intent);

    }
}