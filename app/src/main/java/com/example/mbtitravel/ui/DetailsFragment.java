package com.example.mbtitravel.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mbtitravel.R;
import com.example.mbtitravel.data.AttractionRepository;
import com.example.mbtitravel.model.Attraction;

/**
 * Formularul unei atracții. Funcționează în două moduri:
 * - adăugare (attraction_id = -1): câmpuri goale, butonul „Adaugă”;
 * - editare (attraction_id existent): câmpuri precompletate, butoanele „Salvează” și „Șterge”.
 */
public class DetailsFragment extends Fragment {

    public static final String ARG_ATTRACTION_ID = "attraction_id";

    private final AttractionRepository repository = AttractionRepository.getInstance();

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitle = view.findViewById(R.id.tvDetailsTitle);
        EditText etName = view.findViewById(R.id.etAttractionName);
        EditText etCategory = view.findViewById(R.id.etAttractionCategory);
        EditText etDescription = view.findViewById(R.id.etAttractionDescription);
        RadioGroup rgVibe = view.findViewById(R.id.rgVibe);
        Button btnSave = view.findViewById(R.id.btnSaveAttraction);
        Button btnDelete = view.findViewById(R.id.btnDeleteAttraction);

        int id = getArguments() != null ? getArguments().getInt(ARG_ATTRACTION_ID, -1) : -1;
        Attraction existing = id != -1 ? repository.getById(id) : null;

        if (existing != null) {
            tvTitle.setText("Editează atracția");
            btnSave.setText("Salvează modificările");
            btnDelete.setVisibility(View.VISIBLE);
            if (savedInstanceState == null) {
                etName.setText(existing.name);
                etCategory.setText(existing.category);
                etDescription.setText(existing.description);
                rgVibe.check(Attraction.VIBE_SOCIAL.equals(existing.vibe) ? R.id.rbSocial : R.id.rbQuiet);
            }
        } else {
            tvTitle.setText("Adaugă o atracție");
            btnSave.setText("Adaugă");
            btnDelete.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String category = etCategory.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Numele este obligatoriu");
                return;
            }
            if (category.isEmpty()) category = "General";
            String vibe = rgVibe.getCheckedRadioButtonId() == R.id.rbSocial
                    ? Attraction.VIBE_SOCIAL : Attraction.VIBE_QUIET;

            if (existing != null) {
                repository.update(existing.id, name, category, description, vibe);
                Toast.makeText(getContext(), "Atracție actualizată", Toast.LENGTH_SHORT).show();
            } else {
                repository.add(name, category, description, vibe);
                Toast.makeText(getContext(), "Atracție adăugată", Toast.LENGTH_SHORT).show();
            }
            Navigation.findNavController(v).popBackStack();
        });

        btnDelete.setOnClickListener(v -> {
            if (existing == null) return;
            new AlertDialog.Builder(requireContext())
                    .setTitle("Ștergi atracția?")
                    .setMessage("„" + existing.name + "” va fi eliminată din listă.")
                    .setPositiveButton("Șterge", (d, w) -> {
                        repository.delete(existing.id);
                        Toast.makeText(getContext(), "Atracție ștearsă", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).popBackStack();
                    })
                    .setNegativeButton("Renunță", null)
                    .show();
        });
    }
}
