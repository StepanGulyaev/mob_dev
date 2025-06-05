package com.mirea.gulyaevstepanalekseevich.timeservice;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.gulyaevstepanalekseevich.timeservice.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String host = "time.nist.gov";
    private final int port = 13;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTimeTask timeTask = new GetTimeTask();
                timeTask.execute();
            }
        });
    }
    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String timeResult = "";
            try {
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);
                reader.readLine();
                timeResult = reader.readLine();
                Log.d(TAG,timeResult);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return timeResult;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null || result.isEmpty()) {
                binding.textDate.setText("Error: No server response");
                binding.textTime.setText("Check connection");
                return;
            }

            Log.d(TAG, "Raw server response: " + result);

            String[] tokens = result.trim().split("\\s+");


            if (tokens.length < 3) {
                binding.textDate.setText("Error: Invalid format");
                binding.textTime.setText(result); // Show raw response for diagnosis
                return;
            }

            try {
                String datePart = tokens[1];
                String timePart = tokens[2];
                SimpleDateFormat parser = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault());
                Date dt = parser.parse(datePart + " " + timePart);

                SimpleDateFormat fmtDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

                binding.textDate.setText("Date: " + fmtDate.format(dt));
                binding.textTime.setText("Time: " + fmtTime.format(dt));

            } catch (ParseException e) {
                binding.textDate.setText("Date parse error");
                Log.e(TAG, "Parse error", e);
            }
        }
    }
}