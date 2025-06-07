package com.mirea.gulyaevstepanalekseevich.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.mirea.gulyaevstepanalekseevich.mireaproject.databinding.FragmentFirebaseAuthBinding;


public class AuthHostActivity extends AppCompatActivity {
    private static final String TAG = "AuthHostActivity";
    private FragmentFirebaseAuthBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentFirebaseAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();

        binding.signInButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();
            signIn(email, password);
        });

        binding.createAccountButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();
            createAccount(email, password);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm(email, password)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        // Immediately proceed to MainActivity:
                        goToMainAndFinish();
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(
                                AuthHostActivity.this,
                                "Account creation failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm(email, password)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        goToMainAndFinish();
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(
                                AuthHostActivity.this,
                                "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }


    private boolean validateForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            binding.emailEditText.setError("Required.");
            valid = false;
        } else {
            binding.emailEditText.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            binding.passwordEditText.setError("Required.");
            valid = false;
        } else {
            binding.passwordEditText.setError(null);
        }

        return valid;
    }

    private void goToMainAndFinish() {
        Intent intent = new Intent(AuthHostActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
