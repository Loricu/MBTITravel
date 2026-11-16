package com.example.mbtitravel.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mbtitravel.R;
import com.example.mbtitravel.data.AttractionRepository;
import com.example.mbtitravel.model.Attraction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * Ecranul „Locuri de vizitat”: afișează lista de atracții și permite
 * adăugarea (butonul +), editarea (click pe element) și ștergerea (coș / click lung).
 */
public class AttractionsFragment extends Fragment {

    private AttractionAdapter adapter;
    private TextView tvEmpty;
    private TextView tvCount;
    private final AttractionRepository repository = AttractionRepository.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attractions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String mbti = getArguments() != null ? getArguments().getString("mbti_result") : null;

        tvEmpty = view.findViewById(R.id.tvEmpty);
        tvCount = view.findViewById(R.id.tvAttractionsCount);
        TextView tvSubtitle = view.findViewById(R.id.tvAttractionsSubtitle);
        if (mbti != null) {
            tvSubtitle.setText("⭐ = recomandat pentru tipul tău " + mbti);
        }

        RecyclerView recyclerView = view.findViewById(R.id.rvAttractions);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AttractionAdapter(mbti, new AttractionAdapter.Listener() {
            @Override
            public void onEdit(Attraction attraction) {
                openDetails(view, attraction.id);
            }

            @Override
            public void onDelete(Attraction attraction) {
                confirmDelete(view, attraction);
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddAttraction);
        fabAdd.setOnClickListener(v -> openDetails(v, -1)); // -1 = mod adăugare

        refreshList();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reîncărcăm lista când revenim de pe ecranul de detalii (după adăugare/editare)
        if (adapter != null) refreshList();
    }

    private void refreshList() {
        List<Attraction> all = repository.getAll();
        adapter.submitList(all);
        tvEmpty.setVisibility(all.isEmpty() ? View.VISIBLE : View.GONE);
        tvCount.setText(all.size() + (all.size() == 1 ? " loc" : " locuri"));
    }

    private void openDetails(View v, int attractionId) {
        Bundle args = new Bundle();
        args.putInt(DetailsFragment.ARG_ATTRACTION_ID, attractionId);
        Navigation.findNavController(v).navigate(R.id.action_attractionsFragment_to_detailsFragment, args);
    }

    private void confirmDelete(View root, Attraction attraction) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Ștergi atracția?")
                .setMessage("„" + attraction.name + "” va fi eliminată din listă.")
                .setPositiveButton("Șterge", (dialog, which) -> {
                    repository.delete(attraction.id);
                    refreshList();
                    // Undo: re-adăugăm elementul dacă utilizatorul se răzgândește
                    Snackbar.make(root, "Atracție ștearsă", Snackbar.LENGTH_LONG)
                            .setAction("Anulează", v -> {
                                repository.add(attraction.name, attraction.category,
                                        attraction.description, attraction.vibe);
                                refreshList();
                            })
                            .show();
                })
                .setNegativeButton("Renunță", null)
                .show();
    }
}
