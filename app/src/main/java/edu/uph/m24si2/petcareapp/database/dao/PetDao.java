package edu.uph.m24si2.petcareapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import edu.uph.m24si2.petcareapp.model.Pet;

@Dao
public interface PetDao {

    @Insert
    void insert(Pet pet);

    @Query("SELECT * FROM pets")
    List<Pet> getAllPets();

    @Query("SELECT * FROM pets WHERE id = :id")
    Pet getPetById(int id);

    @Query("SELECT * FROM pets WHERE userId = :userId")
    List<Pet> getPetByUser(int userId);
}
