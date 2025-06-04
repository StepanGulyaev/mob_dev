package com.mirea.gulyaevstepanalekseevich.securesharedpreferences;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;


import com.mirea.gulyaevstepanalekseevich.securesharedpreferences.databinding.ActivityMainBinding;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPreferences secureSharedPreferences;
    private static final String PREFS_NAME = "secret_shared_prefs";
    private static final String KEY_IMAGE_RESOURCE = "poet_photo";
    private static final String VALUE_IMAGE_RESOURCE = "velimir";

    private static final String KEY_POET_NAME = "favourite_poet";
    private static final String VALUE_POET_NAME = "Велимир Хлебников";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeEncryptedPreferences();
        loadAndDisplayEncryptedData();
    }

    private void initializeEncryptedPreferences() {
        try {

            String mainKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            secureSharedPreferences = EncryptedSharedPreferences.create(
                    PREFS_NAME,
                    mainKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            secureSharedPreferences.edit()
                    .putString(KEY_POET_NAME,VALUE_POET_NAME)
                    .putString(KEY_IMAGE_RESOURCE, VALUE_IMAGE_RESOURCE)
                    .apply();



        } catch (GeneralSecurityException | IOException e) {
            Log.e("EncryptedPrefs", "Error initializing encrypted preferences", e);
            Toast.makeText(this, "Security error occurred", Toast.LENGTH_SHORT).show();
            secureSharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        }
    }

    private void loadAndDisplayEncryptedData() {

        String encryptedText = secureSharedPreferences.getString(KEY_POET_NAME, "No data found");
        binding.poetName.setText(encryptedText);

        String encryptedImage = secureSharedPreferences.getString(KEY_IMAGE_RESOURCE, "No data found");
        @SuppressLint("DiscouragedApi") int imageResId = getResources().getIdentifier(
                encryptedImage,
                "drawable",
                getPackageName()
        );

        binding.poetImage.setImageResource(imageResId);
    }
}