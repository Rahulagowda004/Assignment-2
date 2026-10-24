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
    private static final String UNIVERSITY_FRAGMENT_TAG = "university_list";
    private static final String DETAIL_FRAGMENT_TAG = "detail";

    private String selectedUniversity;
    private String selectedDepartment;
    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);
        twoPane = findViewById(R.id.detail_container) != null;

        if (savedInstanceState != null) {
            selectedUniversity = savedInstanceState.getString(STATE_SELECTED_UNIVERSITY);
            selectedDepartment = savedInstanceState.getString(STATE_SELECTED_DEPARTMENT);
        }

        if (getSupportFragmentManager().findFragmentByTag(UNIVERSITY_FRAGMENT_TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.university_container, new UniversityListFragment(),
                            UNIVERSITY_FRAGMENT_TAG)
                    .commit();
        }

        if (twoPane && selectedUniversity == null) {
            showLandscapePrompt();
        }
    }

    @Override
    public void onUniversitySelected(String universityName) {
        selectedUniversity = universityName;
        selectedDepartment = null;
        if (twoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container,
                            DepartmentListFragment.newInstance(universityName), DETAIL_FRAGMENT_TAG)
                    .commit();
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
        if (twoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container,
                            ProfessorDetailsFragment.newInstance(universityName, departmentName),
                            DETAIL_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
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

    private void showLandscapePrompt() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, new LandscapePromptFragment(), DETAIL_FRAGMENT_TAG)
                .commit();
    }

    public void showSelectedDepartment() {
        if (twoPane && selectedUniversity != null) {
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container,
                            DepartmentListFragment.newInstance(selectedUniversity), DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_SELECTED_UNIVERSITY, selectedUniversity);
        outState.putString(STATE_SELECTED_DEPARTMENT, selectedDepartment);
        super.onSaveInstanceState(outState);
    }

    public static class LandscapePromptFragment extends Fragment {
        @Override
        public android.view.View onCreateView(@NonNull android.view.LayoutInflater inflater,
                                               android.view.ViewGroup container,
                                               Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_detail_prompt, container, false);
        }
    }
}
