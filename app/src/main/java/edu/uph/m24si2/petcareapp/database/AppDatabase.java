package edu.uph.m24si2.petcareapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import edu.uph.m24si2.petcareapp.database.dao.UserDao;
import edu.uph.m24si2.petcareapp.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

}