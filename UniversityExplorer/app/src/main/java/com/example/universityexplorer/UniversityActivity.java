package com.example.universityexplorer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class UniversityActivity extends AppCompatActivity
        implements UniversityListFragment.OnUniversitySelectedListener,
        DepartmentListFragment.OnDepartmentSelectedListener {

    public static final String EXTRA_UNIVERSITY = "extra_university";
    private static final String STATE_SELECTED_UNIVERSITY = "selected_university";
    private static final String STATE_SELECTED_DEPARTMENT = "selected_department";
    private static final String STATE_HAS_DEPARTMENT_SELECTION = "has_department_selection";
    private static final String UNIVERSITY_FRAGMENT_TAG = "university_list";
    private static final String DETAIL_FRAGMENT_TAG = "detail";

    private String selectedUniversity;
    private String selectedDepartment;
    private boolean hasDepartmentSelection;
    private boolean landscapeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);
        landscapeMode = findViewById(R.id.department_container) != null;

        if (savedInstanceState != null) {
            selectedUniversity = savedInstanceState.getString(STATE_SELECTED_UNIVERSITY);
            selectedDepartment = savedInstanceState.getString(STATE_SELECTED_DEPARTMENT);
            hasDepartmentSelection = savedInstanceState.getBoolean(STATE_HAS_DEPARTMENT_SELECTION);
        }
        if (getSupportFragmentManager().findFragmentByTag(UNIVERSITY_FRAGMENT_TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.university_container, new UniversityListFragment(),
                            UNIVERSITY_FRAGMENT_TAG)
                    .commit();
        }

        if (landscapeMode) {
            if (selectedUniversity == null) {
                showInitialUniversityOnly();
            } else if (!hasDepartmentSelection || selectedDepartment == null) {
                showDepartmentFragment(selectedUniversity);
            } else {
                showProfessorFragment(selectedUniversity, selectedDepartment);
            }
        }
    }

    @Override
    public void onUniversitySelected(String universityName) {
        selectedUniversity = universityName;
        selectedDepartment = null;
        hasDepartmentSelection = false;
        if (landscapeMode) {
            showDepartmentFragment(universityName);
        } else {
            Intent intent = new Intent(this, DepartmentActivity.class);
            intent.putExtra(EXTRA_UNIVERSITY, universityName);
            startActivity(intent);
        }
    }

    @Override
    public void onDepartmentSelected(String universityName, String departmentName) {
        selectedUniversity = universityName;
        selectedDepartment = departmentName;
        hasDepartmentSelection = true;
        if (landscapeMode) {
            showProfessorFragment(universityName, departmentName);
        } else {
            startProfessorActivity(universityName, departmentName);
        }
    }

    private void startProfessorActivity(String universityName, String departmentName) {
        UniversityRepository.Department department =
                UniversityRepository.findDepartment(universityName, departmentName);
        if (department == null) {
            return;
        }
        Intent intent = new Intent(this, ProfessorActivity.class);
        intent.putExtra(DepartmentActivity.EXTRA_UNIVERSITY, universityName);
        intent.putExtra(DepartmentActivity.EXTRA_DEPARTMENT, departmentName);
        intent.putExtra(DepartmentActivity.EXTRA_PROFESSOR, department.professor.name);
        intent.putExtra(DepartmentActivity.EXTRA_DESCRIPTION, department.professor.description);
        intent.putExtra(DepartmentActivity.EXTRA_WEBSITE, ProfessorActivity.SAMPLE_WEBSITE);
        startActivity(intent);
    }

    private void showInitialUniversityOnly() {
        setPaneVisibility(false, false);
        removeFragmentInContainer(R.id.department_container);
        removeFragmentInContainer(R.id.professor_container);
    }

    private void showDepartmentFragment(String universityName) {
        setPaneVisibility(true, false);
        removeFragmentInContainer(R.id.professor_container);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.department_container,
                        DepartmentListFragment.newInstance(universityName), DETAIL_FRAGMENT_TAG)
                .commit();
    }

    private void showProfessorFragment(String universityName, String departmentName) {
        setPaneVisibility(true, true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.department_container,
                        DepartmentListFragment.newInstance(universityName), "department_list")
                .replace(R.id.professor_container,
                        ProfessorDetailsFragment.newInstance(universityName, departmentName),
                        "professor_details")
                .commit();
    }

    public void showSelectedDepartment() {
        if (landscapeMode && selectedUniversity != null) {
            selectedDepartment = null;
            hasDepartmentSelection = false;
            showDepartmentFragment(selectedUniversity);
        } else {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_SELECTED_UNIVERSITY, selectedUniversity);
        outState.putString(STATE_SELECTED_DEPARTMENT, selectedDepartment);
        outState.putBoolean(STATE_HAS_DEPARTMENT_SELECTION, hasDepartmentSelection);
        super.onSaveInstanceState(outState);
    }

    public void showInitialUniversityOnlyFromBack() {
        if (landscapeMode) {
            selectedUniversity = null;
            selectedDepartment = null;
            hasDepartmentSelection = false;
            showInitialUniversityOnly();
        } else {
            finish();
        }
    }

    private void setPaneVisibility(boolean departmentVisible, boolean professorVisible) {
        findViewById(R.id.department_container).setVisibility(
                departmentVisible ? android.view.View.VISIBLE : android.view.View.GONE);
        findViewById(R.id.professor_container).setVisibility(
                professorVisible ? android.view.View.VISIBLE : android.view.View.GONE);
        android.widget.LinearLayout.LayoutParams departmentParams =
                (android.widget.LinearLayout.LayoutParams) findViewById(
                        R.id.department_container).getLayoutParams();
        android.widget.LinearLayout.LayoutParams professorParams =
                (android.widget.LinearLayout.LayoutParams) findViewById(
                        R.id.professor_container).getLayoutParams();
        android.widget.LinearLayout.LayoutParams universityParams =
                (android.widget.LinearLayout.LayoutParams) findViewById(
                        R.id.university_container).getLayoutParams();
        universityParams.weight = departmentVisible ? 0.31f : 1f;
        departmentParams.weight = departmentVisible ? 0.31f : 0f;
        professorParams.weight = professorVisible ? 0.38f : 0f;
        findViewById(R.id.university_container).setLayoutParams(universityParams);
        findViewById(R.id.department_container).setLayoutParams(departmentParams);
        findViewById(R.id.professor_container).setLayoutParams(professorParams);
    }

    private void removeFragmentInContainer(int containerId) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(containerId);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commitNow();
        }
    }
}
