package com.mirea.gulyaevstepanalekseevich.toastapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private int countSymbolsEditText(EditText editText) {
        return editText.getText().length();
    }

    public void onClickCountSymbols(View view){

        EditText countSymbolsText = findViewById(R.id.countSymbolsText);
        int numOfSymbols = countSymbolsEditText(countSymbolsText);

        Toast toast = Toast.makeText(getApplicationContext(),  "Student #8 Gruppa BISO-01-20 Number of symbols -" + " " + Integer.toString(numOfSymbols) ,Toast.LENGTH_SHORT);
        toast.show();
    }
}