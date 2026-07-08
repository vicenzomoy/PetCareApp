package edu.uph.m24si2.petcareapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.Pet;
import edu.uph.m24si2.petcareapp.model.User;

public class PetProfile extends AppCompatActivity {

    EditText etName, etAge, etDesc;
    Spinner spType;
    RadioGroup rgGender;
    Button btnSave;

    AppDatabase db;
    SharedPreferences preferences;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pet_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // PREFERENCES
        preferences = getSharedPreferences("PetCare", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        // DATABASE
        db = AppDatabase.getDatabase(this);

        // FIND VIEW
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etDesc = findViewById(R.id.etDesc);
        spType = findViewById(R.id.spType);
        rgGender = findViewById(R.id.rgGender);
        btnSave = findViewById(R.id.btnSave);

        // SPINNER DATA
        String[] petTypes = {"Kucing", "Anjing", "Kelinci", "Burung"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                petTypes
        );

        spType.setAdapter(adapter);

        // BUTTON SAVE
        btnSave.setOnClickListener(v -> savePet());

        // DEBUG: cek data di database
        List<Pet> pets = db.petDao().getPetByUser(userId);
        for (Pet p : pets) {
            System.out.println("PET: " + p.getName());
        }
    }

    private void savePet() {
        String name = etName.getText().toString();
        String type = spType.getSelectedItem().toString();
        String ageStr = etAge.getText().toString();
        String desc = etDesc.getText().toString();

        // gender
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(selectedGenderId);
        String gender = selectedGender != null ? selectedGender.getText().toString() : "";

        // validasi
        if (name.isEmpty()) {
            Toast.makeText(this, "Nama wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = ageStr.isEmpty() ? 0 : Integer.parseInt(ageStr);

        // CREATE OBJECT
        Pet pet = new Pet();
        pet.setUserId(userId);
        pet.setName(name);
        pet.setType(type);
        pet.setAge(age);
        pet.setGender(gender);
        pet.setDescription(desc);

        // INSERT ROOM
        db.petDao().insert(pet);

        Toast.makeText(this, "Hewan berhasil disimpan", Toast.LENGTH_SHORT).show();
        finish();
    }
}