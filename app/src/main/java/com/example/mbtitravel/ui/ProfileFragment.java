package com.example.mbtitravel.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mbtitravel.R;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvResult = view.findViewById(R.id.tvMbtiResult);
        TextView tvRecommendation = view.findViewById(R.id.tvRecommendation);
        Button btnToMap = view.findViewById(R.id.btnToMap);

        // O valoare implicită în caz că utilizatorul dă click pe "Știu deja tipul meu" de pe Home
        String mbti = "ENFP";

        // 1. Preluarea datelor transmise de la TestFragment (Passage de données)
        if (getArguments() != null && getArguments().containsKey("mbti_result")) {
            mbti = getArguments().getString("mbti_result");
        }

        tvResult.setText(mbti);

        // 2. Generarea recomandărilor dinamice în funcție de personalitate
        StringBuilder recomandare = new StringBuilder("Recomandarea aplicației:\n\n");

        if (mbti.contains("E")) {
            recomandare.append("🚌 Transport: STB / Metrou. Îți place energia mulțimii și orașul viu.\n");
            recomandare.append("📍 Atracții: Piețe publice, centre vechi aglomerate, evenimente.\n\n");
        } else {
            recomandare.append("🚶 Transport: Mers pe jos. Preferi o abordare mai retrasă și liniștită.\n");
            recomandare.append("📍 Atracții: Parcuri, muzee ascunse, cafenele de specialitate.\n\n");
        }

        if (mbti.contains("J")) {
            recomandare.append("📅 Planificare: Urmează un itinerar fix pentru a salva timp.");
        } else {
            recomandare.append("🧭 Planificare: Fii spontan! Explorează fără un orar strict.");
        }

        tvRecommendation.setText(recomandare.toString());

        // 3. Navigarea către Hartă
        btnToMap.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_mapFragment);
        });
    }
}