package com.mirea.gulyaevstepanalekseevich.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

public class AudioFragment extends Fragment {

    private static final int REQUEST_CODE_PERMISSION = 200;
    private static final String TAG = "AudioFragment";

    private boolean isWork = false;

    private boolean isStartRecording = true;
    private boolean isStartPlaying = true;

    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    private String recordFilePath;

    private Button recordButton;
    private Button playButton;

    public AudioFragment() {
    }

    public static AudioFragment newInstance(String param1, String param2) {
        AudioFragment fragment = new AudioFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_audio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordButton = view.findViewById(R.id.recordButton);
        playButton = view.findViewById(R.id.playButton);

        File musicDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        recordFilePath = new File(musicDir, "audiorecordtest.3gp").getAbsolutePath();
        Log.d(TAG, "recordFilePath: " + recordFilePath);

        int audioPermission = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
        );
        isWork = (audioPermission == PackageManager.PERMISSION_GRANTED);

        if (!isWork) {
            requestPermissions(
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_CODE_PERMISSION
            );
        }

        recordButton.setOnClickListener(v -> {
            if (!isWork) {
                return;
            }


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

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            isWork = (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
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
