package com.mirea.gulyaevstepanalekseevich.mireaproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NavProfileFragment extends Fragment {

    private static final String PREFS_NAME = "UserProfilePrefs";
    private TextView loggedAs;
    private EditText editUsername, editEmail;
    private SharedPreferences prefs;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_profile, container, false);

        editUsername = view.findViewById(R.id.etUsername);
        editEmail = view.findViewById(R.id.etEmail);
        Button saveButton = view.findViewById(R.id.btnSaveProfile);

        prefs = requireActivity().getSharedPreferences(PREFS_NAME, 0);

        loadSavedData();
        saveButton.setOnClickListener(v -> saveUserData());
        return view;
    }

    private void loadSavedData() {
        String username = prefs.getString("username", "");
        String email = prefs.getString("email", "");
        editUsername.setText(username);
        editEmail.setText(email);
    }

    private void saveUserData() {
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.putString("email", email);
        editor.apply();

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).refreshProfileData();
        }

        Toast.makeText(requireContext(), "Profile saved", Toast.LENGTH_SHORT).show();
        loadSavedData();

    }
}