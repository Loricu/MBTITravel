package com.example.mbtitravel.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mbtitravel.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Insert
    void insertUsers(List<User> users);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users ORDER BY age DESC LIMIT 1")
    User getOldestUser();

    @Query("DELETE FROM users WHERE age > :maxAge")
    void deleteUsersOlderThan(int maxAge);

    @Query("UPDATE users SET last_name = :newLastName WHERE first_name = :firstName AND last_name = :oldLastName")
    void updateLastName(String firstName, String oldLastName, String newLastName);

    @Query("SELECT * FROM users ORDER BY last_name DESC")
    List<User> getUsersOrderedByLastNameDesc();

    @Query("DELETE FROM users")
    void deleteAllUsers();
}