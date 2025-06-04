package com.mirea.gulyaevstepanalekseevich.lesson6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.gulyaevstepanalekseevich.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "Lesson6_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefs = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        binding.groupCode.setText(prefs.getString("group",""));
        binding.listNumber.setText(prefs.getString("list",""));
        binding.favoriteMovie.setText(prefs.getString("fav_movie",""));
    }

    public void onSaveButtonClick(View view) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("group", binding.groupCode.getText().toString());
        editor.putString("list", binding.listNumber.getText().toString());
        editor.putString("fav_movie", binding.favoriteMovie.getText().toString());
        editor.apply();
        Toast.makeText(this, "Data has been saved", Toast.LENGTH_SHORT).show();
    }
}