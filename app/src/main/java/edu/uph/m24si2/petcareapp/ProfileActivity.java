package edu.uph.m24si2.petcareapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.User;

public class ProfileActivity extends AppCompatActivity {

    private ImageView btnBack;

    private TextView tvName;
    private TextView tvEmail;

    private TextView tvPetCount;
    private TextView tvHotelCount;
    private TextView tvGroomingCount;
    private TextView tvHomeServiceCount;

    private MaterialButton btnEditProfile;
    private MaterialButton btnChangePassword;
    private MaterialButton btnBookingHistory;
    private MaterialButton btnLogout;

    private AppDatabase db;

    private SharedPreferences preferences;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = AppDatabase.getDatabase(this);

        preferences = getSharedPreferences("PetCare", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        initView();

        loadProfile();

        loadStatistic();

        btnBack.setOnClickListener(v -> finish());
        btnEditProfile.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            ProfileActivity.this,
                            EditProfileActivity.class
                    )
            );
        });
        btnChangePassword.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            ProfileActivity.this,
                            ChangePasswordActivity.class);

            startActivity(intent);

        });
        btnBookingHistory.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, HistoryActivity.class));
        });
        btnLogout.setOnClickListener(v -> logout());
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadProfile();
        loadStatistic();
    }

    private void initView(){

        btnBack = findViewById(R.id.btnBack);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnBookingHistory = findViewById(R.id.btnBookingHistory);
        btnLogout = findViewById(R.id.btnLogout);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);

        tvPetCount = findViewById(R.id.tvPetCount);
        tvHotelCount = findViewById(R.id.tvHotelCount);
        tvGroomingCount = findViewById(R.id.tvGroomingCount);
        tvHomeServiceCount = findViewById(R.id.tvHomeServiceCount);

    }

    private void loadProfile(){

        User user = db.userDao().getUserById(userId);

        if(user == null){
            return;
        }

        tvName.setText(user.getFullname());

        tvEmail.setText(user.getEmail());

    }

    private void loadStatistic(){

        int petCount =
                db.petDao().getPetByUser(userId).size();

        int groomingCount =
                db.bookingGroomingDao().getBookingByUser(userId).size();

        int hotelCount =
                db.bookingPetHotelDao().getBookingByUser(userId).size();

        int homeServiceCount =
                db.bookingHomeServiceDao().getByUser(userId).size();

        tvPetCount.setText(String.valueOf(petCount));

        tvHotelCount.setText(String.valueOf(hotelCount));

        tvGroomingCount.setText(String.valueOf(groomingCount));

        tvHomeServiceCount.setText(String.valueOf(homeServiceCount));

    }

    private void logout() {

        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.apply();

        Intent intent = new Intent(
                ProfileActivity.this,
                LoginActivity.class);

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }
}