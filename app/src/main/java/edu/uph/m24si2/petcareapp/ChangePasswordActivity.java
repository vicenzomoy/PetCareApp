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

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView btnBack;

    private TextInputEditText etOldPassword;
    private TextInputEditText etNewPassword;
    private TextInputEditText etConfirmPassword;

    private MaterialButton btnSave;

    private AppDatabase db;

    private User currentUser;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        db = AppDatabase.getDatabase(this);

        SharedPreferences preferences =
                getSharedPreferences("PetCare", MODE_PRIVATE);

        userId = preferences.getInt("userId",-1);

        currentUser = db.userDao().getUserById(userId);

        initView();

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> changePassword());

    }

    private void initView(){

        btnBack = findViewById(R.id.btnBack);

        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnSave = findViewById(R.id.btnSave);

    }

    private void changePassword(){

        String oldPassword =
                etOldPassword.getText().toString().trim();

        String newPassword =
                etNewPassword.getText().toString().trim();

        String confirmPassword =
                etConfirmPassword.getText().toString().trim();

        if(oldPassword.isEmpty()
                || newPassword.isEmpty()
                || confirmPassword.isEmpty()){

            Toast.makeText(
                    this,
                    "Semua kolom harus diisi",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if(!oldPassword.equals(currentUser.getPassword())){

            Toast.makeText(
                    this,
                    "Password lama salah",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if(newPassword.length()<6){

            Toast.makeText(
                    this,
                    "Password minimal 6 karakter",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if(!newPassword.equals(confirmPassword)){

            Toast.makeText(
                    this,
                    "Konfirmasi password tidak cocok",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        currentUser.setPassword(newPassword);

        db.userDao().update(currentUser);

        Toast.makeText(
                this,
                "Password berhasil diubah",
                Toast.LENGTH_LONG
        ).show();

        finish();

    }

}
