package com.mirea.gulyaevstepanalekseevich.mireaproject; // adjust if needed

import static android.content.Context.SENSOR_SERVICE;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class AcceleratorFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private TextView azimuthTextView;
    private TextView pitchTextView;
    private TextView rollTextView;

    public AcceleratorFragment() {
    }

    public static AcceleratorFragment newInstance() {
        return new AcceleratorFragment();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        return inflater.inflate(R.layout.fragment_accellerator, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        // 1) Enable Edge-to-Edge (same as your Activity’s code)
        ViewCompat.setOnApplyWindowInsetsListener(
                view.findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );
                    return insets;
                }
        );

        // 2) Bind our TextViews
        azimuthTextView = view.findViewById(R.id.textViewAzimuth);
        pitchTextView   = view.findViewById(R.id.textViewPitch);
        rollTextView    = view.findViewById(R.id.textViewRoll);

        // 3) Get SensorManager and the default accelerometer
        sensorManager = (SensorManager) requireContext().getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Re-register the listener, same as your Activity’s onResume()
        if (accelerometer != null) {
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener to save battery
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Only respond if it’s the accelerometer
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Update each TextView with the new readings
            azimuthTextView.setText(String.format("Azimuth: %s", x));
            pitchTextView.setText(String.format("Pitch: %s",   y));
            rollTextView.setText(String.format("Roll: %s",     z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // We don’t need to do anything here for this example
    }
}
