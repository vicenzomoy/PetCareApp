package edu.uph.m24si2.petcareapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import edu.uph.m24si2.petcareapp.database.AppDatabase;
import edu.uph.m24si2.petcareapp.model.User;
import edu.uph.m24si2.petcareapp.model.Pet;

public class MainActivity extends AppCompatActivity {

    LinearLayout navBeranda, navBooking, navRiwayat, navProfil;
    MaterialCardView cardAddPet;
    LinearLayout containerPets, btnGrooming, btnPetHotel, btnHomeService;

    TextView tvGreeting;

    AppDatabase db;
    SharedPreferences preferences;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Initialize Database & Preferences
        db = AppDatabase.getDatabase(this);
        preferences = getSharedPreferences("PetCare", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        // Initialize Views
        navBeranda = findViewById(R.id.navBeranda);
        navBooking = findViewById(R.id.navBooking);
        navRiwayat = findViewById(R.id.navRiwayat);
        navProfil = findViewById(R.id.navProfil);
        cardAddPet = findViewById(R.id.cardAddPet);
        containerPets = findViewById(R.id.containerPets);
        btnGrooming = findViewById(R.id.btnGrooming);
        btnPetHotel = findViewById(R.id.btnPetHotel);
        btnHomeService = findViewById(R.id.btnHomeService);
        tvGreeting = findViewById(R.id.tvGreeting);

        // Click Listeners
        navBeranda.setOnClickListener(v -> { /* Already here */ });

        navBooking.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    BookingScheduleActivity.class
            );

            startActivity(intent);
        });

        navRiwayat.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        navProfil.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ProfileActivity.class
            );

            startActivity(intent);

        });

        if (btnGrooming != null) {
            btnGrooming.setOnClickListener(v -> {
                Intent intent = new Intent(this, BookingGroomingActivity.class);
                startActivity(intent);
            });
        }

        if (btnPetHotel != null) {
            btnPetHotel.setOnClickListener(v -> {
                Intent intent = new Intent(this, BookingPetHotelActivity.class);
                startActivity(intent);
            });
        }

        if (btnHomeService != null) {
            btnHomeService.setOnClickListener(v -> {
                Intent intent = new Intent(this, BookingHomeServiceActivity.class);
                startActivity(intent);
            });
        }

        if (cardAddPet != null) {
            cardAddPet.setOnClickListener(v -> {
                Intent intent = new Intent(this, PetProfile.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUser();
        loadPets();
    }

    private void loadUser() {

        User user = db.userDao().getUserById(userId);

        if (user != null) {
            tvGreeting.setText("Halo, " + user.getFullname() + " 👋");
        }

    }

    private void loadPets() {
        if (containerPets == null) return;

        // Clear existing pet cards (keep the Add Pet card)
        // Add Pet card is the last child
        int childCount = containerPets.getChildCount();
        if (childCount > 1) {
            containerPets.removeViews(0, childCount - 1);
        }

        // Fetch pets from DB
        List<Pet> petList = db.petDao().getPetByUser(userId);

        if (petList != null && !petList.isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            
            // Add pet cards before the "Add Pet" card
            for (int i = 0; i < petList.size(); i++) {
                Pet pet = petList.get(i);
                View petView = inflater.inflate(R.layout.item_pet, containerPets, false);
                
                TextView tvName = petView.findViewById(R.id.tvPetName);
                TextView tvType = petView.findViewById(R.id.tvPetType);
                TextView tvBreed = petView.findViewById(R.id.tvPetBreed);
                ImageView imgPet = petView.findViewById(R.id.imgPet);
                ImageButton btnMenu = petView.findViewById(R.id.btnMenu);
                
                tvName.setText(pet.getName());
                tvType.setText(pet.getType());
                tvBreed.setText(pet.getGender()); 
                
                // Set image based on type
                if (pet.getType().equalsIgnoreCase("Kucing")) {
                    imgPet.setImageResource(R.drawable.kucing);
                } else if (pet.getType().equalsIgnoreCase("Anjing")) {
                    imgPet.setImageResource(R.drawable.anjing);
                } else if (pet.getType().equalsIgnoreCase("Burung")) {
                    imgPet.setImageResource(R.drawable.burung);
                } else if (pet.getType().equalsIgnoreCase("Kelinci")) {
                    imgPet.setImageResource(R.drawable.kelinci);
                }

                // Delete Menu Logic
                btnMenu.setOnClickListener(view -> {
                    PopupMenu popup = new PopupMenu(this, view);
                    popup.getMenu().add("Delete");
                    popup.setOnMenuItemClickListener(item -> {
                        if (item.getTitle().equals("Delete")) {
                            new AlertDialog.Builder(this)
                                    .setTitle("Hapus Hewan")
                                    .setMessage("Apakah Anda yakin ingin menghapus " + pet.getName() + "?")
                                    .setPositiveButton("Hapus", (dialog, which) -> {
                                        db.petDao().delete(pet);
                                        Toast.makeText(this, pet.getName() + " dihapus", Toast.LENGTH_SHORT).show();
                                        loadPets(); // Refresh Dashboard
                                    })
                                    .setNegativeButton("Batal", null)
                                    .show();
                            return true;
                        }
                        return false;
                    });
                    popup.show();
                });
                
                // Insert at the beginning
                containerPets.addView(petView, i);
            }
        }
    }
}