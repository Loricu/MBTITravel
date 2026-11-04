package com.example.mbtitravel.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mbtitravel.model.UserProfile;

@Dao
public interface UserProfileDao {

    // Funcție pentru a salva un profil nou
    @Insert
    void insertProfile(UserProfile profile);

    // Funcție pentru a citi ultimul profil salvat (limităm la 1 pentru a lua mereu contul curent)
    @Query("SELECT * FROM user_profile ORDER BY id DESC LIMIT 1")
    UserProfile getLastProfile();

    // Funcție pentru a șterge profilul (în caz că vrea să refacă testul)
    @Query("DELETE FROM user_profile")
    void deleteAll();
}