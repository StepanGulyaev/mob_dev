package com.mirea.gulyaevstepanalekseevich.audiorecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 200;
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isWork = false;
    private boolean isStartRecording = true;
    private boolean isStartPlaying = true;

    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    private String recordFilePath;

    private Button recordButton;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordButton = findViewById(R.id.recordButton);
        playButton = findViewById(R.id.playButton);
        playButton.setEnabled(false);

        recordFilePath = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "audiorecordtest.3gp").getAbsolutePath();
        Log.d(TAG, "recordFilePath: " + recordFilePath);

        // разрешения
        int audioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        isWork = audioPermission == PackageManager.PERMISSION_GRANTED;

        if (!isWork) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_CODE_PERMISSION);
        }


        recordButton.setOnClickListener(v -> {
            if (!isWork) return;

            if (isStartRecording) {
                startRecording();
                recordButton.setText("Stop recording");
                playButton.setEnabled(false);
            } else {
                stopRecording();
                recordButton.setText("Start recording");
                playButton.setEnabled(true);
            }
            isStartRecording = !isStartRecording;
        });

        playButton.setOnClickListener(v -> {
            if (isStartPlaying) {
                startPlaying();
                playButton.setText("Stop playing");
                recordButton.setEnabled(false);
            } else {
                stopPlaying();
                playButton.setText("Start playing");
                recordButton.setEnabled(true);
            }
            isStartPlaying = !isStartPlaying;
        });
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
            recorder.start();
            Log.d(TAG, "Recording started");
        } catch (IOException | IllegalStateException e) {
            Log.e(TAG, "Recording error: ", e);
        }
    }

    private void stopRecording() {
        try {
            recorder.stop();
            Log.d(TAG, "Recording stopped");
        } catch (RuntimeException e) {
            Log.e(TAG, "Error stopping recorder", e);
        } finally {
            recorder.release();
            recorder = null;
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
            Log.d(TAG, "Playback started");
        } catch (IOException e) {
            Log.e(TAG, "Playback failed", e);
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
            Log.d(TAG, "Playback stopped");
        }
    }

}