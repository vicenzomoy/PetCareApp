package edu.uph.m24si2.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.Booking;

public class DetailBookingActivity extends AppCompatActivity {

    TextView tvCode,tvPet,tvService,tvDate,tvTime,tvPrice;

    Button btnHistory,btnPay;

    AppDatabase db;

    int petId;
    int price;

    String petName;
    String service;
    String date;
    String time;
    String notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_booking);

        db = AppDatabase.getDatabase(this);

        tvCode=findViewById(R.id.tvCode);
        tvPet=findViewById(R.id.tvPet);
        tvService=findViewById(R.id.tvService);
        tvDate=findViewById(R.id.tvDate);
        tvTime=findViewById(R.id.tvTime);
        tvPrice=findViewById(R.id.tvPrice);

        btnPay=findViewById(R.id.btnPay);
        btnHistory=findViewById(R.id.btnHistory);

        petId=getIntent().getIntExtra("PET_ID",0);
        petName=getIntent().getStringExtra("PET_NAME");
        service=getIntent().getStringExtra("SERVICE");
        date=getIntent().getStringExtra("DATE");
        time=getIntent().getStringExtra("TIME");
        notes=getIntent().getStringExtra("NOTES");
        price=getIntent().getIntExtra("PRICE",0);

        Random random=new Random();

        tvCode.setText("PC"+(100000+random.nextInt(900000)));

        tvPet.setText("Hewan : "+petName);

        tvService.setText("Layanan : "+service);

        tvDate.setText("Tanggal : "+date);

        tvTime.setText("Jam : "+time);

        tvPrice.setText("Rp "+price);

        btnPay.setOnClickListener(v -> {

            Booking booking=new Booking(

                    petId,

                    service,

                    date,

                    time,

                    notes,

                    "Paid",

                    price

            );

            db.bookingDao().insert(booking);

            Toast.makeText(
                    DetailBookingActivity.this,
                    "Pembayaran Berhasil",
                    Toast.LENGTH_SHORT
            ).show();

            Toast.makeText(
                    DetailBookingActivity.this,
                    "Pembayaran berhasil!",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        });

    }

}