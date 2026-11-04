package com.example.mbtitravel.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mbtitravel.model.UserProfile;

@Database(entities = {UserProfile.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Legăm DAO-ul creat mai devreme
    public abstract UserProfileDao userProfileDao();

    // Creăm o instanță unică a bazei de date (Design Pattern-ul Singleton)
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "mbti_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}