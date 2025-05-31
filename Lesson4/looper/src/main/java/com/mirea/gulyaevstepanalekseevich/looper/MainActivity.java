package com.mirea.gulyaevstepanalekseevich.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.gulyaevstepanalekseevich.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String info = msg.getData().getString("result");
                binding.textViewInfo.setText(info); // показываем в интерфейсе после ожидания
            }
        };

        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.button.setOnClickListener(v -> {
            String age = binding.editAge.getText().toString();
            String job = binding.editJob.getText().toString();

            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("AGE", age);
            bundle.putString("JOB", job);
            msg.setData(bundle);

            myLooper.mHandler.sendMessage(msg);
        });
    }
}