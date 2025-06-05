package com.mirea.gulyaevstepanalekseevich.mireaproject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;


public class NavNotebook extends Fragment {

    private TextView fileContent;
    private static final String SECRET_KEY = "MySecretKey123";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_notebook, container, false);

        fileContent = view.findViewById(R.id.fileContent);
        Button btnCreate = view.findViewById(R.id.btnCreate);
        Button btnLoad = view.findViewById(R.id.btnLoad);

        btnCreate.setOnClickListener(v -> showCreateDialog());
        btnLoad.setOnClickListener(v -> showLoadDialog());

        return view;
    }

    private void showCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_record, null);

        EditText inputFilename = dialogView.findViewById(R.id.inputFilename);
        EditText inputRecord = dialogView.findViewById(R.id.inputRecord);

        builder.setView(dialogView)
                .setTitle("Create Encrypted Record")
                .setPositiveButton("Encrypt & Save", (dialog, id) -> {
                    String filename = inputFilename.getText().toString().trim();
                    String content = inputRecord.getText().toString().trim();

                    if (filename.isEmpty() || content.isEmpty()) {
                        Toast.makeText(requireContext(), "Filename and content required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!filename.endsWith(".txt")) {
                        filename += ".txt";
                    }

                    saveEncryptedFile(filename, content);
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        builder.create().show();
    }

    private void showLoadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_load_record, null);

        EditText inputFilename = dialogView.findViewById(R.id.inputFilename);

        builder.setView(dialogView)
                .setTitle("Load Encrypted File")
                .setPositiveButton("Load", (dialog, id) -> {
                    String filename = inputFilename.getText().toString().trim();
                    if (filename.isEmpty()) {
                        Toast.makeText(requireContext(), "Enter filename", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!filename.endsWith(".txt")) {
                        filename += ".txt";
                    }

                    loadFile(filename);
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        builder.create().show();
    }

    private void saveEncryptedFile(String filename, String content) {
        try {
            String encrypted = simpleEncrypt(content);
            try (FileOutputStream fos = requireContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
                fos.write(encrypted.getBytes());
                Toast.makeText(requireContext(), "File saved: " + filename, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error saving file", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFile(String filename) {
        try (FileInputStream fis = requireContext().openFileInput(filename)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            String encrypted = new String(bytes);
            String decrypted = simpleDecrypt(encrypted);
            fileContent.setText(decrypted);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error loading file", Toast.LENGTH_SHORT).show();
            fileContent.setText("File not found or decryption failed");
        }
    }

    private String simpleEncrypt(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            output.append((char) (input.charAt(i) ^ SECRET_KEY.charAt(i % SECRET_KEY.length())));
        }
        return Base64.encodeToString(output.toString().getBytes(), Base64.DEFAULT);
    }

    private String simpleDecrypt(String input) {
        try {
            byte[] decoded = Base64.decode(input, Base64.DEFAULT);
            String decodedStr = new String(decoded);
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < decodedStr.length(); i++) {
                output.append((char) (decodedStr.charAt(i) ^ SECRET_KEY.charAt(i % SECRET_KEY.length())));
            }
            return output.toString();
        } catch (Exception e) {
            return "Decryption failed";
        }
    }
}