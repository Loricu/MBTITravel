package com.example.mbtitravel.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        // --- Verificăm dacă avem deja un profil salvat ---
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            UserProfile savedProfile = db.userProfileDao().getLastProfile();

            // Dacă există, sărim automat la ecranul de Profil
            if (savedProfile != null && savedProfile.mbtiType != null) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("mbti_result", savedProfile.mbtiType);
                        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment, bundle);
                    });
                }
            }
        }).start();


        Button btnTakeTest = view.findViewById(R.id.btnTakeTest);
        Button btnKnowMbti = view.findViewById(R.id.btnKnowMbti);
        EditText etMbtiInput = view.findViewById(R.id.etMbtiInput);

        btnTakeTest.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_testFragment);
        });

        btnKnowMbti.setOnClickListener(v -> {
            String typedMbti = etMbtiInput.getText().toString().trim().toUpperCase();

            if (typedMbti.length() == 4) {
                // --- Salvăm profilul în Baza de Date ---
                new Thread(() -> {
                    AppDatabase db = AppDatabase.getInstance(requireContext());
                    db.userProfileDao().deleteAll(); // Ștergem vechiul profil dacă există

                    UserProfile newProfile = new UserProfile();
                    newProfile.mbtiType = typedMbti;
                    db.userProfileDao().insertProfile(newProfile);

                    // După salvare, trecem la ecranul următor
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Bundle bundle = new Bundle();
                            bundle.putString("mbti_result", typedMbti);
                            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_profileFragment, bundle);
                        });
                    }
                }).start();

            } else {
                Toast.makeText(getContext(), "Introdu 4 litere!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}