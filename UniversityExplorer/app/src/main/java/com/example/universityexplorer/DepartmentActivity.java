package com.example.universityexplorer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class DepartmentActivity extends AppCompatActivity
        implements UniversityListFragment.OnUniversitySelectedListener,
        DepartmentListFragment.OnDepartmentSelectedListener {

    public static final String EXTRA_UNIVERSITY = "extra_university";
    public static final String EXTRA_DEPARTMENT = "extra_department";
    public static final String EXTRA_PROFESSOR = "extra_professor";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String EXTRA_WEBSITE = "extra_website";
    private static final String STATE_SELECTED_UNIVERSITY = "selected_university";
    private static final String STATE_SELECTED_DEPARTMENT = "selected_department";
    private static final String UNIVERSITY_FRAGMENT_TAG = "university_list";
    private static final String DEPARTMENT_FRAGMENT_TAG = "department_list";
    private static final String PROFESSOR_FRAGMENT_TAG = "professor_details";

    private String selectedUniversity;
    private String selectedDepartment;
    private boolean landscapeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        landscapeMode = findViewById(R.id.university_container) != null;

        if (savedInstanceState != null) {
            selectedUniversity = savedInstanceState.getString(STATE_SELECTED_UNIVERSITY);
            selectedDepartment = savedInstanceState.getString(STATE_SELECTED_DEPARTMENT);
        } else {
            selectedUniversity = getIntent().getStringExtra(EXTRA_UNIVERSITY);
        }

        if (landscapeMode) {
            renderLandscapeState();
        } else {
            renderPortraitState();
        }
    }

    @Override
    public void onUniversitySelected(String universityName) {
        selectedUniversity = universityName;
        selectedDepartment = null;
        if (landscapeMode) {
            renderLandscapeState();
        }
    }

    @Override
    public void onDepartmentSelected(String universityName, String departmentName) {
        selectedUniversity = universityName;
        selectedDepartment = departmentName;
        if (landscapeMode) {
            renderLandscapeState();
            return;
        }

        startProfessorActivity(universityName, departmentName);
    }

    private void startProfessorActivity(String universityName, String departmentName) {
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

    private void renderLandscapeState() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction =
                fragmentManager.beginTransaction();

        if (fragmentManager.findFragmentByTag(UNIVERSITY_FRAGMENT_TAG) == null) {
            transaction.replace(R.id.university_container, new UniversityListFragment(),
                    UNIVERSITY_FRAGMENT_TAG);
        }

        if (selectedUniversity == null) {
            removeFragment(fragmentManager, transaction, R.id.department_container);
            removeFragment(fragmentManager, transaction, R.id.professor_container);
            setPaneVisibility(false);
        } else if (selectedDepartment == null) {
            transaction.replace(R.id.department_container,
                    DepartmentListFragment.newInstance(selectedUniversity),
                    DEPARTMENT_FRAGMENT_TAG);
            removeFragment(fragmentManager, transaction, R.id.professor_container);
            setPaneVisibility(true);
        } else {
            if (fragmentManager.findFragmentByTag(DEPARTMENT_FRAGMENT_TAG) == null) {
                transaction.replace(R.id.department_container,
                        DepartmentListFragment.newInstance(selectedUniversity),
                        DEPARTMENT_FRAGMENT_TAG);
            }
            transaction.replace(R.id.professor_container,
                    ProfessorDetailsFragment.newInstance(
                            selectedUniversity, selectedDepartment),
                    PROFESSOR_FRAGMENT_TAG);
            setPaneVisibility(true);
        }
        transaction.commit();
    }

    private void renderPortraitState() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction =
                fragmentManager.beginTransaction();
        removeFragmentByTag(fragmentManager, transaction, UNIVERSITY_FRAGMENT_TAG);
        removeFragmentByTag(fragmentManager, transaction, DEPARTMENT_FRAGMENT_TAG);
        removeFragmentByTag(fragmentManager, transaction, PROFESSOR_FRAGMENT_TAG);

        if (selectedDepartment == null) {
            transaction.replace(R.id.department_container,
                    DepartmentListFragment.newInstance(selectedUniversity),
                    DEPARTMENT_FRAGMENT_TAG);
        } else {
            transaction.replace(R.id.department_container,
                    ProfessorDetailsFragment.newInstance(
                            selectedUniversity, selectedDepartment),
                    PROFESSOR_FRAGMENT_TAG);
        }
        transaction.commit();
    }

    private void removeFragmentByTag(FragmentManager fragmentManager,
            androidx.fragment.app.FragmentTransaction transaction, String tag) {
        androidx.fragment.app.Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.remove(fragment);
        }
    }

    private void removeFragment(FragmentManager fragmentManager,
            androidx.fragment.app.FragmentTransaction transaction, int containerId) {
        androidx.fragment.app.Fragment fragment = fragmentManager.findFragmentById(containerId);
        if (fragment != null) {
            transaction.remove(fragment);
        }
    }

    private void setPaneVisibility(boolean rightPaneVisible) {
        findViewById(R.id.department_container).setVisibility(
                rightPaneVisible
                        ? android.view.View.VISIBLE : android.view.View.GONE);
        findViewById(R.id.professor_container).setVisibility(
                rightPaneVisible && selectedDepartment != null
                        ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    public void showSelectedDepartment() {
        selectedDepartment = null;
        if (landscapeMode) {
            renderLandscapeState();
        } else {
            renderPortraitState();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_SELECTED_UNIVERSITY, selectedUniversity);
        outState.putString(STATE_SELECTED_DEPARTMENT, selectedDepartment);
        super.onSaveInstanceState(outState);
    }
}
