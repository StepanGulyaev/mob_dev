package com.mirea.gulyaevstepanalekseevich.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.mirea.gulyaevstepanalekseevich.httpurlconnection.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String TAG = "LocationWeatherApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkAndFetchData();
            }
        });
    }

    private void checkNetworkAndFetchData() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            new FetchDataTask().execute("https://ipinfo.io/json");
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonFetch.setEnabled(false);
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private class FetchDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadJson(urls[0]);
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonFetch.setEnabled(true);

            if (result.startsWith("Error:")) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject jsonResponse = new JSONObject(result);
                updateLocationUI(jsonResponse);

                String coordinates = jsonResponse.getString("loc");
                String[] latLon = coordinates.split(",");
                if (latLon.length == 2) {
                    fetchWeatherData(latLon[0], latLon[1]);
                }
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "JSON parsing error", e);
            }
        }
    }

    private void updateLocationUI(JSONObject locationData) throws JSONException {
        binding.textCity.setText(locationData.optString("city", "N/A"));
        binding.textRegion.setText(locationData.optString("region", "N/A"));
        binding.textCountry.setText(locationData.optString("country", "N/A"));

        String coordinates = locationData.optString("loc", "N/A");
        binding.textCoordinates.setText(coordinates);
    }

    private void fetchWeatherData(String latitude, String longitude) {
        String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" +
                latitude + "&longitude=" + longitude + "&current_weather=true";

        new WeatherTask().execute(weatherUrl);
    }

    private class WeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadJson(urls[0]);
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.startsWith("Error:")) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject jsonResponse = new JSONObject(result);
                JSONObject currentWeather = jsonResponse.getJSONObject("current_weather");

                double temperature = currentWeather.getDouble("temperature");
                double windSpeed = currentWeather.getDouble("windspeed");
                int weatherCode = currentWeather.getInt("weathercode");

                binding.textTemperature.setText(temperature + "°C");
                binding.textWind.setText(windSpeed + " km/h");
                binding.textCondition.setText(getWeatherCondition(weatherCode));

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Weather data error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Weather JSON parsing error", e);
            }
        }
    }

    private String getWeatherCondition(int code) {
        switch (code) {
            case 0: return "Clear sky";
            case 1: return "Mainly clear";
            case 2: return "Partly cloudy";
            case 3: return "Overcast";
            case 45: case 48: return "Fog";
            case 51: case 53: case 55: return "Drizzle";
            case 56: case 57: return "Freezing Drizzle";
            case 61: case 63: case 65: return "Rain";
            case 66: case 67: return "Freezing Rain";
            case 71: case 73: case 75: return "Snow";
            case 77: return "Snow grains";
            case 80: case 81: case 82: return "Rain showers";
            case 85: case 86: return "Snow showers";
            case 95: return "Thunderstorm";
            case 96: case 99: return "Thunderstorm with hail";
            default: return "Unknown";
        }
    }

    private String downloadJson(String urlString) throws IOException {
        InputStream inputStream = null;
        StringBuilder data = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } else {
                return "Error: HTTP " + responseCode;
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data.toString();
    }
}