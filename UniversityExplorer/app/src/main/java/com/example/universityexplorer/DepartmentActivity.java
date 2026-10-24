package com.example.universityexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DepartmentActivity extends AppCompatActivity {

    public static final String EXTRA_UNIVERSITY = "extra_university";
    public static final String EXTRA_DEPARTMENT = "extra_department";
    public static final String EXTRA_PROFESSOR = "extra_professor";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String EXTRA_WEBSITE = "extra_website";

    private static final String[] DEPARTMENTS = {
            "Computer Science",
            "Electrical Engineering",
            "Mechanical Engineering",
            "Mathematics"
    };

    private static final String[] PROFESSORS = {
            "Dr. Avery Morgan",
            "Dr. Jordan Lee",
            "Dr. Taylor Singh",
            "Dr. Riley Chen"
    };

    private static final String[] DESCRIPTIONS = {
            "Sample faculty profile for demonstration purposes. Researches software systems and computing education.",
            "Sample faculty profile for demonstration purposes. Teaches circuits, signals, and embedded systems.",
            "Sample faculty profile for demonstration purposes. Teaches design methods and sustainable machines.",
            "Sample faculty profile for demonstration purposes. Teaches applied mathematics and mathematical modelling."
    };

    private String universityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        universityName = savedInstanceState != null
                ? savedInstanceState.getString(EXTRA_UNIVERSITY)
                : getIntent().getStringExtra(EXTRA_UNIVERSITY);
        if (universityName == null) {
            universityName = "Selected university";
        }
        final String selectedUniversity = universityName;

        TextView universityText = findViewById(R.id.department_university);
        universityText.setText(universityName);

        ListView departmentList = findViewById(R.id.department_list);
        departmentList.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, DEPARTMENTS));
        departmentList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(DepartmentActivity.this, ProfessorActivity.class);
            intent.putExtra(EXTRA_UNIVERSITY, selectedUniversity);
            intent.putExtra(EXTRA_DEPARTMENT, DEPARTMENTS[position]);
            intent.putExtra(EXTRA_PROFESSOR, PROFESSORS[position]);
            intent.putExtra(EXTRA_DESCRIPTION, DESCRIPTIONS[position]);
            intent.putExtra(EXTRA_WEBSITE, ProfessorActivity.SAMPLE_WEBSITE);
            startActivity(intent);
        });

        findViewById(R.id.department_back_button).setOnClickListener(view -> finish());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_UNIVERSITY, universityName);
        super.onSaveInstanceState(outState);
    }
}
