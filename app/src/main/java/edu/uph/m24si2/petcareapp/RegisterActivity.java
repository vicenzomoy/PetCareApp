package edu.uph.m24si2.petcareapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNama, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top,
                    systemBars.right, systemBars.bottom);
            return insets;
        });

        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        db = AppDatabase.getDatabase(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registerUser() {

        String fullname = etNama.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        if (fullname.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirm.isEmpty()) {

            Toast.makeText(this,
                    "Semua data harus diisi.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            Toast.makeText(this,
                    "Format email tidak valid.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {

            Toast.makeText(this,
                    "Konfirmasi password tidak sesuai.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        User checkUser = db.userDao().getUserByEmail(email);

        if (checkUser != null) {

            Toast.makeText(this,
                    "Email sudah terdaftar.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(email, fullname, password);

        db.userDao().insert(user);

        Toast.makeText(this,
                "Registrasi berhasil.",
                Toast.LENGTH_SHORT).show();

        finish();
    }
}