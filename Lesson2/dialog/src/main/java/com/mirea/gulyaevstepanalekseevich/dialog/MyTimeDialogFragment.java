package com.mirea.gulyaevstepanalekseevich.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.ConcurrentModificationException;

public class MyTimeDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        Context context = getContext();
        if (context == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        return new TimePickerDialog(
                context,
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format("You have chosen: %02d:%02d", selectedHour, selectedMinute);

                    Activity activity = getActivity();
                    if (activity != null) {
                        View rootView = activity.findViewById(android.R.id.content);
                        if (rootView != null) {
                            Snackbar.make(rootView, time, Snackbar.LENGTH_LONG).show();
                        }
                    }
                },
                hour,
                minute,
                true
        );
    }
}