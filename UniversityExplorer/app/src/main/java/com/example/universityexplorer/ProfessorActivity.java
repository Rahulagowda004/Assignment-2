package com.example.universityexplorer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ProfessorActivity extends AppCompatActivity {

    public static final String SAMPLE_WEBSITE = "https://example.edu/professor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        if (savedInstanceState == null) {
            String universityName = getIntent().getStringExtra(DepartmentActivity.EXTRA_UNIVERSITY);
            String departmentName = getIntent().getStringExtra(DepartmentActivity.EXTRA_DEPARTMENT);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.professor_container,
                            ProfessorDetailsFragment.newInstance(universityName, departmentName))
                    .commit();
        }
    }
}
