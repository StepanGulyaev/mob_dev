package com.mirea.gulyaevstepanalekseevich.mireaproject; // <-- adjust if your package is different

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraFragment extends Fragment {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private boolean isWork = false;
    private Uri imageUri;
    private ImageView imageView;
    private Button buttonMakePhoto;


    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    public CameraFragment() {
    }

    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        imageView = view.findViewById(R.id.imageView);
        buttonMakePhoto = view.findViewById(R.id.button_make_photo);


        int cameraPermission = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
        );
        isWork = (cameraPermission == PackageManager.PERMISSION_GRANTED);
        if (!isWork) {

            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_PERMISSION
            );
        }




        cameraActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if (result.getResultCode() == Activity.RESULT_OK) {

                                    imageView.setImageURI(imageUri);
                                }
                            }
                        }
                );


        buttonMakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isWork) {
                    return;
                }


                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    File photoFile = createImageFile();
                    String authority = requireContext().getPackageName() + ".fileprovider";
                    imageUri = FileProvider.getUriForFile(
                            requireContext(),
                            authority,
                            photoFile
                    );
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    cameraActivityResultLauncher.launch(cameraIntent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create an image file in your app’s external‐files/Pictures directory.
     * The filename is based on a timestamp, just like your MainActivity example.
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.ENGLISH
        ).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
        );
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isWork = (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
    }
}
