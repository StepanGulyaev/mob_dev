package com.mirea.gulyaevstepanalekseevich.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

public class MyProgressDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getContext();
        if (context == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("First step...");

        Activity activity = getActivity();
        if (activity != null) {
            View rootView = activity.findViewById(android.R.id.content);
            if (rootView != null) {
                Snackbar.make(rootView, "Second step...", Snackbar.LENGTH_LONG).show();
            }
        }

        return progressDialog;
    }
}