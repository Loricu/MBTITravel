package com.example.mbtitravel.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mbtitravel.R;

public class TestFragment extends Fragment {

    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadioGroup rgQ1 = view.findViewById(R.id.rgQ1);
        RadioGroup rgQ2 = view.findViewById(R.id.rgQ2);
        RadioGroup rgQ3 = view.findViewById(R.id.rgQ3);
        RadioGroup rgQ4 = view.findViewById(R.id.rgQ4);
        Button btnSubmit = view.findViewById(R.id.btnSubmitTest);

        btnSubmit.setOnClickListener(v -> {
            StringBuilder mbti = new StringBuilder();

            // 1. Extragere E vs I
            if (rgQ1.getCheckedRadioButtonId() == R.id.rbE) {
                mbti.append("E");
            } else {
                mbti.append("I");
            }

            // 2. Extragere S vs N
            if (rgQ2.getCheckedRadioButtonId() == R.id.rbS) {
                mbti.append("S");
            } else {
                mbti.append("N");
            }

            // 3. Extragere T vs F
            if (rgQ3.getCheckedRadioButtonId() == R.id.rbT) {
                mbti.append("T");
            } else {
                mbti.append("F");
            }

            // 4. Extragere J vs P
            if (rgQ4.getCheckedRadioButtonId() == R.id.rbJ) {
                mbti.append("J");
            } else {
                mbti.append("P");
            }

            String mbtiResult = mbti.toString();

            // Pachetul de date care se transmite către ProfileFragment
            Bundle bundle = new Bundle();
            bundle.putString("mbti_result", mbtiResult);

            Navigation.findNavController(v).navigate(R.id.action_testFragment_to_profileFragment, bundle);
        });
    }
}