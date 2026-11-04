package com.example.mbtitravel.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// @Entity îi spune lui Android să transforme această clasă într-un tabel SQL
@Entity(tableName = "user_profile")
public class UserProfile {

    @PrimaryKey(autoGenerate = true)
    public int id; // ID-ul unic (se generează automat 1, 2, 3...)

    public String mbtiType; // Aici vom salva textul (ex: "INTJ")
}
