package com.mirea.gulyaevstepanalekseevich.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class NavHeaderFragment extends Fragment {

    private TextView headerUsername, headerEmail;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "UserProfilePrefs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_header_main, container, false);

        headerUsername = view.findViewById(R.id.headerUsername);
        headerEmail = view.findViewById(R.id.headerEmail);

        prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadProfileData();

        return view;
    }

    private void loadProfileData() {
        String username = prefs.getString("username", "Username");
        String email = prefs.getString("email", "email@example.com");

        headerUsername.setText(username);
        headerEmail.setText(email);
    }

    public void refreshProfileData() {
        if (isAdded()) {
            loadProfileData();
        }
    }
}