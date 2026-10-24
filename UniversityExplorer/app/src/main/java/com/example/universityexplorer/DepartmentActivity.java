package com.example.universityexplorer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DepartmentActivity extends AppCompatActivity
        implements DepartmentListFragment.OnDepartmentSelectedListener {

    public static final String EXTRA_UNIVERSITY = "extra_university";
    public static final String EXTRA_DEPARTMENT = "extra_department";
    public static final String EXTRA_PROFESSOR = "extra_professor";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String EXTRA_WEBSITE = "extra_website";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        if (savedInstanceState == null) {
            String universityName = getIntent().getStringExtra(EXTRA_UNIVERSITY);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.department_container,
                            DepartmentListFragment.newInstance(universityName))
                    .commit();
        }
    }

    @Override
    public void onDepartmentSelected(String universityName, String departmentName) {
        UniversityRepository.Department department =
                UniversityRepository.findDepartment(universityName, departmentName);
        if (department == null) {
            return;
        }
        Intent intent = new Intent(this, ProfessorActivity.class);
        intent.putExtra(EXTRA_UNIVERSITY, universityName);
        intent.putExtra(EXTRA_DEPARTMENT, departmentName);
        intent.putExtra(EXTRA_PROFESSOR, department.professor.name);
        intent.putExtra(EXTRA_DESCRIPTION, department.professor.description);
        intent.putExtra(EXTRA_WEBSITE, ProfessorActivity.SAMPLE_WEBSITE);
        startActivity(intent);
    }
}
