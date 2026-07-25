package edu.uph.m24si2.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PaymentSuccessActivity extends AppCompatActivity {
    private TextView tvBookingCode;
    private TextView tvStatus;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_success);
        View mainView = findViewById(R.id.main);
        int paddingLeft = mainView.getPaddingLeft();
        int paddingTop = mainView.getPaddingTop();
        int paddingRight = mainView.getPaddingRight();
        int paddingBottom = mainView.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left + paddingLeft,
                    systemBars.top + paddingTop,
                    systemBars.right + paddingRight,
                    systemBars.bottom + paddingBottom
            );
            return insets;
        });
        tvBookingCode = findViewById(R.id.tvBookingCode);

        tvStatus = findViewById(R.id.tvStatus);

        btnHome = findViewById(R.id.btnHome);

        loadData();

        btnHome.setOnClickListener(v -> backToHome());
    }

    private void loadData() {

        String bookingCode =
                getIntent().getStringExtra("BOOKING_CODE");

        if (bookingCode == null) {
            bookingCode = "-";
        }

        tvBookingCode.setText(bookingCode);

        tvStatus.setText("Menunggu Konfirmasi");

    }

    private void backToHome() {

        Intent intent =
                new Intent(
                        PaymentSuccessActivity.this,
                        MainActivity.class
                );

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        finish();

    }
}