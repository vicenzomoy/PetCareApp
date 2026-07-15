package edu.uph.m24si2.petcareapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.User;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView btnBack;

    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etPhone;

    private MaterialButton btnSave;

    private AppDatabase db;

    private SharedPreferences preferences;

    private User currentUser;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = AppDatabase.getDatabase(this);

        preferences = getSharedPreferences("PetCare", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        initView();

        loadProfile();

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> updateProfile());
    }

    private void initView() {

        btnBack = findViewById(R.id.btnBack);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);

        btnSave = findViewById(R.id.btnSave);

    }

    private void loadProfile() {

        currentUser = db.userDao().getUserById(userId);

        if (currentUser == null) {

            Toast.makeText(
                    this,
                    "User tidak ditemukan",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        etName.setText(currentUser.getFullname());

        etEmail.setText(currentUser.getEmail());
    }

    private void updateProfile() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty()) {

            etName.setError("Nama wajib diisi");
            return;

        }

        if (email.isEmpty()) {

            etEmail.setError("Email wajib diisi");
            return;

        }

        currentUser.setFullname(name);
        currentUser.setEmail(email);

        db.userDao().update(currentUser);

        Toast.makeText(
                this,
                "Profil berhasil diperbarui",
                Toast.LENGTH_SHORT
        ).show();

        finish();

    }

}