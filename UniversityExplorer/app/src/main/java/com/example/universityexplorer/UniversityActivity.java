package com.example.universityexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class UniversityActivity extends AppCompatActivity {

    private static final String[] UNIVERSITIES = {
            "University of Guelph",
            "University of Toronto",
            "University of Waterloo",
            "University of British Columbia"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);

        ListView universityList = findViewById(R.id.university_list);
        universityList.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, UNIVERSITIES));

        universityList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(UniversityActivity.this, DepartmentActivity.class);
            intent.putExtra(DepartmentActivity.EXTRA_UNIVERSITY, UNIVERSITIES[position]);
            startActivity(intent);
        });

        findViewById(R.id.university_back_button).setOnClickListener(view -> finish());
    }
}
