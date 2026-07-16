package edu.uph.m24si2.petcareapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.radiobutton.MaterialRadioButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.BookingGrooming;
import edu.uph.m24si2.petcareapp.model.BookingHomeService;
import edu.uph.m24si2.petcareapp.model.BookingPetHotel;
import edu.uph.m24si2.petcareapp.model.BookingRequest;
import edu.uph.m24si2.petcareapp.util.BookingType;

public class PaymentActivity extends AppCompatActivity {
    private TextView tvTotalPayment;
    private TextView tvPaymentInfo;
    private RadioGroup rgPayment;
    private MaterialRadioButton rbTransfer;
    private MaterialRadioButton rbQris;
    private Button btnPay;
    private BookingRequest request;

    private AppDatabase db;
    private SharedPreferences preferences;
    private int userId;

    private String paymentMethod = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = AppDatabase.getDatabase(this);
        preferences = getSharedPreferences("PetCare", MODE_PRIVATE);
        userId = preferences.getInt("userId",-1);

        tvTotalPayment = findViewById(R.id.tvTotalPayment);
        tvPaymentInfo = findViewById(R.id.tvPaymentInfo);
        rgPayment = findViewById(R.id.rgPayment);
        rbTransfer = findViewById(R.id.rbTransfer);
        rbQris = findViewById(R.id.rbQris);
        btnPay = findViewById(R.id.btnPay);

        request = (BookingRequest)
                getIntent().getSerializableExtra("booking");

        if(request == null){
            finish();
            return;
        }

        tvTotalPayment.setText(formatRupiah(request.getPrice()));

        setupPaymentMethod();

        btnPay.setOnClickListener(v -> payBooking());
    }

    private String formatRupiah(int value){

        NumberFormat format =
                NumberFormat.getCurrencyInstance(
                        new Locale("id","ID"));

        return format.format(value);

    }

    private void setupPaymentMethod() {

        rgPayment.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.rbTransfer) {

                paymentMethod = "Bank Transfer";

                tvPaymentInfo.setText(
                        "Transfer ke\n\n" +
                                "Bank BCA\n" +
                                "123456789\n" +
                                "a.n PetCare"
                );

            } else if (checkedId == R.id.rbQris) {

                paymentMethod = "E-Wallet";

                tvPaymentInfo.setText(
                        "Bayar menggunakan QRIS");

            }

        });

    }

    private void payBooking(){

        if(rgPayment.getCheckedRadioButtonId() == -1){

            Toast.makeText(
                    this,
                    "Pilih metode pembayaran",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        showSuccessDialog();

    }

    private void showSuccessDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Pembayaran Berhasil")
                .setMessage("Booking Anda telah berhasil dibuat.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {

                    String bookingCode = saveBooking();

                    Intent intent = new Intent(
                            PaymentActivity.this,
                            PaymentSuccessActivity.class
                    );

                    intent.putExtra("BOOKING_CODE", bookingCode);

                    startActivity(intent);
                    finish();

                })
                .show();
    }

    private String saveBooking() {

        switch (request.getBookingType()) {

            case BookingType.GROOMING:
                return saveGrooming();

            case BookingType.HOME_SERVICE:
                return saveHomeService();

            case BookingType.PET_HOTEL:
                return savePetHotel();

            default:
                return "";
        }
    }

    private String saveGrooming() {

        BookingGrooming booking = new BookingGrooming();

        String code = generateCode("GR");

        booking.setUserId(userId);

        booking.setPetId(request.getPetId());

        booking.setService(request.getService());

        booking.setBookingDate(request.getBookingDate());

        booking.setBookingTime(request.getBookingTime());

        booking.setNotes(request.getNotes());

        booking.setPrice(request.getPrice());

        booking.setStatus("Menunggu Konfirmasi");

        booking.setBookingCode(code);

        booking.setCreatedAt(
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                ).format(new Date())
        );

        booking.setPaymentMethod(paymentMethod);

        db.bookingGroomingDao().insert(booking);

        return code;
    }

    private String saveHomeService() {

        BookingHomeService booking =
                new BookingHomeService();

        String code = generateCode("HS");

        booking.setUserId(userId);

        booking.setPetId(request.getPetId());

        booking.setBookingDate(request.getBookingDate());

        booking.setBookingTime(request.getBookingTime());

        booking.setAddress(request.getAddress());

        booking.setLandmark(request.getLandmark());

        booking.setPhoneNumber(request.getPhoneNumber());

        booking.setNotes(request.getNotes());

        booking.setPrice(request.getPrice());

        booking.setStatus("Menunggu Konfirmasi");

        booking.setBookingCode(code);

        booking.setCreatedAt(
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                ).format(new Date())
        );

        booking.setPaymentMethod(paymentMethod);

        db.bookingHomeServiceDao().insert(booking);

        return code;
    }

    private String savePetHotel() {

        BookingPetHotel booking =
                new BookingPetHotel();

        String code = generateCode("PH");

        booking.setUserId(userId);

        booking.setPetId(request.getPetId());

        booking.setRoomType(request.getRoomType());

        booking.setCheckInDate(request.getCheckInDate());

        booking.setCheckOutDate(request.getCheckOutDate());

        booking.setTotalDays(request.getTotalDays());

        booking.setTotalPrice(request.getPrice());

        booking.setRoomPrice(request.getPrice() / request.getTotalDays());

        booking.setNote(request.getNotes());

        booking.setStatus("Menunggu Konfirmasi");

        booking.setBookingCode(code);

        booking.setCreatedAt(
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                ).format(new Date())
        );

        booking.setPaymentMethod(paymentMethod);

        db.bookingPetHotelDao().insertBooking(booking);

        return code;
    }

    private String generateCode(String prefix){

        return prefix + System.currentTimeMillis();

    }
}