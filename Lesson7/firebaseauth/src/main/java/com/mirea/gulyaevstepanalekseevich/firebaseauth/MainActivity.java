package com.mirea.gulyaevstepanalekseevich.firebaseauth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirea.gulyaevstepanalekseevich.firebaseauth.databinding.AuthScreenBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AuthScreenBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate with view binding
        binding = AuthScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Button listeners
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

        binding.signOutButton.setOnClickListener(v -> signOut());

        binding.verifyEmailButton.setOnClickListener(v -> sendEmailVerification());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm(email, password)) return;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        Toast.makeText(MainActivity.this,
                                "Account created successfully. Verification email sent.",
                                Toast.LENGTH_LONG).show();
                        sendEmailVerification();
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this,
                                "Account creation failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        updateUI(null);
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm(email, password)) return;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this,
                                "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        updateUI(null);
                    }
                });
    }



    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            binding.verifyEmailButton.setEnabled(false);
            user.sendEmailVerification()
                    .addOnCompleteListener(this, task -> {
                        binding.verifyEmailButton.setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification:failure", task.getException());
                            Toast.makeText(MainActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
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

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Show signed-in layout
            binding.signedInLayout.setVisibility(View.VISIBLE);
            binding.signedOutLayout.setVisibility(View.GONE);
            binding.statusMessage.setVisibility(View.GONE);

            // Populate user info
            binding.emailValue.setText(user.getEmail());
            binding.passwordValue.setText("••••••••");
            binding.uidValue.setText(user.getUid());

            if (user.isEmailVerified()) {
                binding.verificationStatus.setText("(verified: true)");
                binding.verificationStatus.setTextColor(
                        getResources().getColor(android.R.color.holo_green_dark));
                binding.verifyEmailButton.setEnabled(false);
            } else {
                binding.verificationStatus.setText("(verified: false)");
                binding.verificationStatus.setTextColor(
                        getResources().getColor(android.R.color.holo_red_dark));
                binding.verifyEmailButton.setEnabled(true);
            }
        } else {
            // Show signed-out layout
            binding.signedInLayout.setVisibility(View.GONE);
            binding.signedOutLayout.setVisibility(View.VISIBLE);
            binding.statusMessage.setVisibility(View.GONE);

            binding.emailEditText.setText("");
            binding.passwordEditText.setText("");
        }
    }
}
