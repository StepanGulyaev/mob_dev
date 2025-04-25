package com.mirea.gulyaevstepanalekseevich.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class MyDateDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Context context = getContext();
        if (context == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        return new DatePickerDialog(
                context,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format("You have chosen: %04d-%02d-%02d",
                            selectedYear, selectedMonth + 1, selectedDay);

                    Activity activity = getActivity();
                    if (activity != null) {
                        View rootView = activity.findViewById(android.R.id.content);
                        if (rootView != null) {
                            Snackbar.make(rootView, date, Snackbar.LENGTH_LONG).show();
                        }
                    }
                },
                year,
                month,
                day
        );
    }
}
