package com.example.universityexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = findViewById(R.id.search_university_button);
        searchButton.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, UniversityActivity.class)));

        Button exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(view -> finishAffinity());
    }
}