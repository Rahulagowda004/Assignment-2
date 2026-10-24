package com.example.universityexplorer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ProfessorActivity extends AppCompatActivity {

    public static final String SAMPLE_WEBSITE = "https://example.edu/professor";

    private String universityName;
    private String departmentName;
    private String professorName;
    private String description;
    private String website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        if (savedInstanceState == null) {
            universityName = getIntent().getStringExtra(DepartmentActivity.EXTRA_UNIVERSITY);
            departmentName = getIntent().getStringExtra(DepartmentActivity.EXTRA_DEPARTMENT);
            professorName = getIntent().getStringExtra(DepartmentActivity.EXTRA_PROFESSOR);
            description = getIntent().getStringExtra(DepartmentActivity.EXTRA_DESCRIPTION);
            website = getIntent().getStringExtra(DepartmentActivity.EXTRA_WEBSITE);
        } else {
            restoreState(savedInstanceState);
        }
        showProfessorDetails();

        findViewById(R.id.maps_button).setOnClickListener(view -> openMaps());
        findViewById(R.id.website_button).setOnClickListener(view -> openWebsite());
        findViewById(R.id.professor_back_button).setOnClickListener(view -> finish());
    }

    private void showProfessorDetails() {
        ((TextView) findViewById(R.id.professor_name)).setText(professorName);
        ((TextView) findViewById(R.id.professor_department)).setText(departmentName);
        ((TextView) findViewById(R.id.professor_university)).setText(universityName);
        ((TextView) findViewById(R.id.professor_description)).setText(description);
    }

    private void openMaps() {
        String query = String.format(Locale.US, "%s, %s", departmentName, universityName);
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=" + Uri.encode(query)));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(this, R.string.maps_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private void openWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(this, R.string.browser_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(DepartmentActivity.EXTRA_UNIVERSITY, universityName);
        outState.putString(DepartmentActivity.EXTRA_DEPARTMENT, departmentName);
        outState.putString(DepartmentActivity.EXTRA_PROFESSOR, professorName);
        outState.putString(DepartmentActivity.EXTRA_DESCRIPTION, description);
        outState.putString(DepartmentActivity.EXTRA_WEBSITE, website);
        super.onSaveInstanceState(outState);
    }

    private void restoreState(Bundle state) {
        universityName = state.getString(DepartmentActivity.EXTRA_UNIVERSITY);
        departmentName = state.getString(DepartmentActivity.EXTRA_DEPARTMENT);
        professorName = state.getString(DepartmentActivity.EXTRA_PROFESSOR);
        description = state.getString(DepartmentActivity.EXTRA_DESCRIPTION);
        website = state.getString(DepartmentActivity.EXTRA_WEBSITE);
    }
}
