package com.mirea.gulyaevstepanalekseevich.notebook;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.mirea.gulyaevstepanalekseevich.notebook.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void saveFile(View view) {
        String fileName = binding.fileNameInput.getText().toString().trim();
        String quote = binding.quoteInput.getText().toString().trim();

        if (fileName.isEmpty() || quote.isEmpty()) {
            Toast.makeText(this, "Enter file name and quote", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File docsDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);


            File file = new File(docsDir, fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(quote.getBytes(StandardCharsets.UTF_8));
                Toast.makeText(this, "File saved to: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void loadFile(View view) {
        String fileName = binding.fileNameInput.getText().toString().trim();
        if (fileName.isEmpty()) {
            Toast.makeText(this, "Enter file name", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);

            if (!file.exists()) {
                Toast.makeText(this, "File not found: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                return;
            }

            try (FileInputStream fis = new FileInputStream(file);
                 BufferedReader reader = new BufferedReader(
                         new InputStreamReader(fis, StandardCharsets.UTF_8))) {

                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                binding.quoteInput.setText(content.toString().trim());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error reading file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}