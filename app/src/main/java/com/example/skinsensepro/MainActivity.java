package com.example.skinsensepro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.skinsensepro.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int BARCODE_SCAN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNavigation();

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_filled);
        }

        binding.fab.setOnClickListener(view -> openBarcodeScanner());
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            resetMenuIcons();

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                item.setIcon(R.drawable.home_filled);
            } else if (item.getItemId() == R.id.nav_scanned) {
                selectedFragment = new ScannedItemsFragment();
                item.setIcon(R.drawable.scanned_filled);
            } else if (item.getItemId() == R.id.nav_liked) {
                selectedFragment = new LikedFragment();
                item.setIcon(R.drawable.liked_filled);
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                item.setIcon(R.drawable.profile_filled);
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    private void resetMenuIcons() {
        binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_holo);
        binding.bottomNavigation.getMenu().findItem(R.id.nav_scanned).setIcon(R.drawable.scanned_holo);
        binding.bottomNavigation.getMenu().findItem(R.id.nav_liked).setIcon(R.drawable.liked_holo);
        binding.bottomNavigation.getMenu().findItem(R.id.nav_profile).setIcon(R.drawable.profile_holo);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.fragmentContainer.getId(), fragment)
                .commit();
    }

    private void openBarcodeScanner() {
        Intent intent = new Intent(this, BarcodeScannerActivity.class);
        startActivityForResult(intent, BARCODE_SCAN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BARCODE_SCAN_REQUEST && resultCode == RESULT_OK && data != null) {
            String scanResult = data.getStringExtra("SCAN_RESULT");
            if (scanResult != null) {
                Toast.makeText(this, "Scanned: " + scanResult, Toast.LENGTH_SHORT).show();
                // Handle the scanned barcode here
            }
        }
    }
}
