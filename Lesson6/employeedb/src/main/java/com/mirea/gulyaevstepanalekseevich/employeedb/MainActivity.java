package com.mirea.gulyaevstepanalekseevich.employeedb;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppDatabase db = App.getInstance().getDatabase();
        SuperheroDao superheroDao = db.superheroDao();
        Superhero superhero = new Superhero();

        superhero.name = "Watermelon Jonny";
        superhero.superpower = "Shots watermelons from his mouth";


        superheroDao.insert(superhero);

        List<Superhero> superheroes = superheroDao.getAll();
        superhero = superheroDao.getById(1);
        superhero.superpower = "Shots watermelons from his mouth and nose";
        superheroDao.update(superhero);
        Log.d(TAG, superhero.name + ":" + superhero.superpower);
    }
}