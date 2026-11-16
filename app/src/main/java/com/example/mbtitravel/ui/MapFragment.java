package com.example.mbtitravel.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mbtitravel.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    // Funcția modernă care cere permisiunea pe ecran ("Permiteți accesul la locație?")
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    enableUserLocation();
                } else {
                    Toast.makeText(getContext(), "Avem nevoie de permisiune pentru a-ți arăta traseul pe GPS!", Toast.LENGTH_LONG).show();
                }
            });

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Conectăm codul Java de containerul Hărții din XML
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Setăm butonul din colț care centrează harta pe GPS-ul telefonului
        FloatingActionButton fabLocation = view.findViewById(R.id.fabLocation);
        fabLocation.setOnClickListener(v -> {
            if (mMap != null) {
                checkLocationPermission();
            }
        });
    }

    // Funcția care rulează imediat ce harta s-a încărcat cu succes de pe internet
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Punem harta pe un oraș de start (București) până citește GPS-ul
        LatLng startLocation = new LatLng(44.4268, 26.1025);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 12f));

        // Verificăm permisiunea și pornim GPS-ul
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        // Verificăm dacă avem deja permisiunea aprobată în trecut
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        } else {
            // Dacă nu, afișăm fereastra de sistem pentru a cere permisiunea
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true); // Acest cod afișează automat "Punctul Albastru" al GPS-ului
            }
        }
    }
}