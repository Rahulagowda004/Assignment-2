package com.example.universityexplorer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class ProfessorActivity extends AppCompatActivity
        implements UniversityListFragment.OnUniversitySelectedListener,
        DepartmentListFragment.OnDepartmentSelectedListener {

    public static final String SAMPLE_WEBSITE = "https://example.edu/professor";
    private static final String STATE_UNIVERSITY = "professor_university";
    private static final String STATE_DEPARTMENT = "professor_department";
    private static final String UNIVERSITY_FRAGMENT_TAG = "university_list";
    private static final String DEPARTMENT_FRAGMENT_TAG = "department_list";
    private static final String PROFESSOR_FRAGMENT_TAG = "professor_details";

    private String universityName;
    private String departmentName;
    private boolean landscapeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);
        landscapeMode = findViewById(R.id.university_container) != null;

        if (savedInstanceState == null) {
            universityName = getIntent().getStringExtra(DepartmentActivity.EXTRA_UNIVERSITY);
            departmentName = getIntent().getStringExtra(DepartmentActivity.EXTRA_DEPARTMENT);
        } else {
            universityName = savedInstanceState.getString(STATE_UNIVERSITY);
            departmentName = savedInstanceState.getString(STATE_DEPARTMENT);
        }

        if (landscapeMode) {
            renderLandscapeState();
        } else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.professor_container,
                            ProfessorDetailsFragment.newInstance(universityName, departmentName))
                    .commit();
        }
    }

    @Override
    public void onUniversitySelected(String universityName) {
        this.universityName = universityName;
        departmentName = null;
        if (landscapeMode) {
            renderLandscapeState();
        }
    }

    @Override
    public void onDepartmentSelected(String universityName, String departmentName) {
        this.universityName = universityName;
        this.departmentName = departmentName;
        if (landscapeMode) {
            renderLandscapeState();
        }
    }

    private void renderLandscapeState() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction =
                fragmentManager.beginTransaction();

        if (fragmentManager.findFragmentByTag(UNIVERSITY_FRAGMENT_TAG) == null) {
            transaction.replace(R.id.university_container, new UniversityListFragment(),
                    UNIVERSITY_FRAGMENT_TAG);
        }

        if (universityName != null) {
            transaction.replace(R.id.department_container,
                    DepartmentListFragment.newInstance(universityName),
                    DEPARTMENT_FRAGMENT_TAG);
        }

        if (universityName != null && departmentName != null) {
            transaction.replace(R.id.professor_container,
                    ProfessorDetailsFragment.newInstance(universityName, departmentName),
                    PROFESSOR_FRAGMENT_TAG);
        }
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_UNIVERSITY, universityName);
        outState.putString(STATE_DEPARTMENT, departmentName);
        super.onSaveInstanceState(outState);
    }
}
