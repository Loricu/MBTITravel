package com.example.mbtitravel.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mbtitravel.R;
import com.example.mbtitravel.data.AppDatabase;
import com.example.mbtitravel.model.UserProfile;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etName = view.findViewById(R.id.etUserName);
        CheckBox cbRemember = view.findViewById(R.id.cbRemember);
        Button btnTakeTest = view.findViewById(R.id.btnTakeTest);
        Button btnKnowMbti = view.findViewById(R.id.btnKnowMbti);
        EditText etMbtiInput = view.findViewById(R.id.etMbtiInput);

        // Preluăm utilizatorul salvat din SharedPreferences (nume fișier: "UserPrefs", cheie: "USERNAME_KEY")
        SharedPreferences sharedPref = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String savedUser = sharedPref.getString("USERNAME_KEY", null);
        if (savedUser != null && !savedUser.isEmpty()) {
            etName.setText(savedUser);
            cbRemember.setChecked(true);
        }

        // --- Acțiune buton "Află-ți personalitatea (Test MBTI)" ---
        btnTakeTest.setOnClickListener(v -> {
            saveUserPreference(etName.getText().toString().trim(), cbRemember.isChecked());
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_testFragment);
        });

        // --- Acțiune buton "Continuă cu tipul introdus" ---
        btnKnowMbti.setOnClickListener(v -> {
            String typedMbti = etMbtiInput.getText().toString().trim().toUpperCase();
            String userName = etName.getText().toString().trim();

            if (typedMbti.length() == 4) {
                saveUserPreference(userName, cbRemember.isChecked());

                // Salvăm profilul MBTI în Baza de Date Room
                new Thread(() -> {
                    AppDatabase db = AppDatabase.getInstance(requireContext());
                    db.userProfileDao().deleteAll();

                    UserProfile newProfile = new UserProfile();
                    newProfile.mbtiType = typedMbti;
                    db.userProfileDao().insertProfile(newProfile);

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Bundle bundle = new Bundle();
                            bundle.putString("mbti_result", typedMbti);
                            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_profileFragment, bundle);
                        });
                    }
                }).start();
            } else {
                Toast.makeText(getContext(), "Introdu un MBTI valid de 4 litere (ex: INTJ)!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserPreference(String userName, boolean isChecked) {
        SharedPreferences sharedPref = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (isChecked && !userName.isEmpty()) {
            editor.putString("USERNAME_KEY", userName);
        } else {
            editor.remove("USERNAME_KEY");
        }
        editor.apply();
    }
}