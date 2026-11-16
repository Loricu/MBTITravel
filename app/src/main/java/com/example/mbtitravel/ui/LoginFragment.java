package com.example.mbtitravel.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mbtitravel.R;
import com.example.mbtitravel.data.AppDatabase;
import com.example.mbtitravel.model.User;
import com.example.mbtitravel.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {

    private static final String TAG = "ROOM_TASKS";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etUsername = view.findViewById(R.id.etLoginUsername);
        EditText etPassword = view.findViewById(R.id.etLoginPassword);
        CheckBox cbRemember = view.findViewById(R.id.cbRemember);
        Button btnLogin = view.findViewById(R.id.btnLogin);
        TextView tvConsoleLog = view.findViewById(R.id.tvConsoleLog);

        SharedPreferences sharedPref = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String savedUser = sharedPref.getString("USERNAME_KEY", null);
        if (savedUser != null && !savedUser.isEmpty()) {
            etUsername.setText(savedUser);
            cbRemember.setChecked(true);
        }

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Introduceți numele de utilizator și parola!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Salvare utilizator în SharedPreferences dacă "Retenir l'utilisateur" este bifat
            SharedPreferences.Editor editor = sharedPref.edit();
            if (cbRemember.isChecked()) {
                editor.putString("USERNAME_KEY", username);
            } else {
                editor.remove("USERNAME_KEY");
            }
            editor.apply();

            // Rulăm sarcinile Room DB pe un Thread secundar
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(requireContext());
                StringBuilder logBuilder = new StringBuilder();

                // Curățăm tabela users pentru teste repetate curate
                db.userDao().deleteAllUsers();

                // 1. Inserare 5 utilizatori noi (unul fiind Marie Curie)
                List<User> sampleUsers = new ArrayList<>();
                sampleUsers.add(new User("Marie", "Curie", 25));
                sampleUsers.add(new User("Albert", "Einstein", 42));
                sampleUsers.add(new User("Isaac", "Newton", 17));
                sampleUsers.add(new User("Ada", "Lovelace", 16));
                sampleUsers.add(new User("Nikola", "Tesla", 30));

                db.userDao().insertUsers(sampleUsers);
                List<User> allInserted = db.userDao().getAllUsers();

                Log.d(TAG, "=== 1. UTILIZATORI INSERAȚI EN CONSOLE ===");
                logBuilder.append("1. Adăugați 5 utilizatori:\n");
                for (User u : allInserted) {
                    Log.d(TAG, u.toString());
                    logBuilder.append(u.firstName).append(" ").append(u.lastName).append(" (vârstă ").append(u.age).append(")\n");
                }

                // 2. Selectăm cel mai în vârstă utilizator și afișăm NUMAI numele de familie și vârsta
                User oldest = db.userDao().getOldestUser();
                if (oldest != null) {
                    Log.d(TAG, "=== 2. CEL MAI ÎN VÂRSTĂ UTILIZATOR ===");
                    Log.d(TAG, "Nom de famille: " + oldest.lastName + ", Âge: " + oldest.age);
                    logBuilder.append("\n2. Cel mai în vârstă -> Nom de famille: ").append(oldest.lastName).append(", Âge: ").append(oldest.age).append("\n");
                }

                // 3. Marie Curie s-a căsătorit -> Actualizare nume în Baker
                db.userDao().updateLastName("Marie", "Curie", "Baker");
                Log.d(TAG, "=== 3. Marie Curie actualizat în Marie Baker ===");
                logBuilder.append("\n3. Marie Curie s-a căsătorit -> Marie Baker\n");

                // 4. Ștergem toți utilizatorii cu vârsta > 18 ani
                db.userDao().deleteUsersOlderThan(18);
                Log.d(TAG, "=== 4. ȘTERȘI UTILIZATORII > 18 ANI ===");
                logBuilder.append("\n4. Șterși utilizatorii > 18 ani.\n");

                // 5. Afișăm utilizatorii rămași în ordine alfabetică inversă după nume de familie (last_name DESC)
                List<User> descUsers = db.userDao().getUsersOrderedByLastNameDesc();
                Log.d(TAG, "=== 5. ORDINE ALFABETICĂ INVERSĂ Nume Familie ===");
                logBuilder.append("\n5. Utilizatori rămași (last_name DESC):\n");
                for (User u : descUsers) {
                    Log.d(TAG, u.toString());
                    logBuilder.append(u.lastName).append(", ").append(u.firstName).append(" (vârstă ").append(u.age).append(")\n");
                }

                // Verificăm dacă utilizatorul a completat deja testul MBTI
                UserProfile existingProfile = db.userProfileDao().getLastProfile();

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        tvConsoleLog.setText(logBuilder.toString());

                        // Navigăm la test SAU direct la pagina de rezultat (Profil) dacă testul a fost deja efectuat
                        if (existingProfile != null && existingProfile.mbtiType != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("mbti_result", existingProfile.mbtiType);
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_profileFragment, bundle);
                        } else {
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
                        }
                    });
                }
            }).start();
        });
    }
}