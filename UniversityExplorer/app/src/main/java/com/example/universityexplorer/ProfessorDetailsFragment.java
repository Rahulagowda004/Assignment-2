package com.example.universityexplorer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfessorDetailsFragment extends Fragment {

    private static final String ARG_UNIVERSITY = "university";
    private static final String ARG_DEPARTMENT = "department";
    private static final String ARG_PROFESSOR = "professor";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_WEBSITE = "website";
    private static final String ARG_MAPS_URL = "maps_url";

    public static ProfessorDetailsFragment newInstance(String universityName, String departmentName) {
        ProfessorDetailsFragment fragment = new ProfessorDetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_UNIVERSITY, universityName);
        arguments.putString(ARG_DEPARTMENT, departmentName);
        UniversityRepository.Department department =
                UniversityRepository.findDepartment(universityName, departmentName);
        if (department != null) {
            arguments.putString(ARG_PROFESSOR, department.professor.name);
            arguments.putString(ARG_DESCRIPTION, department.professor.description);
            arguments.putString(ARG_WEBSITE, department.professor.website);
            arguments.putString(ARG_MAPS_URL, department.professor.mapsUrl);
        }
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        String universityName = arguments == null ? "" : arguments.getString(ARG_UNIVERSITY, "");
        String departmentName = arguments == null ? "" : arguments.getString(ARG_DEPARTMENT, "");
        String professorName = arguments == null ? "" : arguments.getString(ARG_PROFESSOR, "");
        String description = arguments == null ? "" : arguments.getString(ARG_DESCRIPTION, "");
        String website = arguments == null ? ProfessorActivity.SAMPLE_WEBSITE
                : arguments.getString(ARG_WEBSITE, ProfessorActivity.SAMPLE_WEBSITE);
        String mapsUrl = arguments == null ? "" : arguments.getString(ARG_MAPS_URL, "");

        ((TextView) view.findViewById(R.id.professor_name)).setText(professorName);
        ((TextView) view.findViewById(R.id.professor_department)).setText(departmentName);
        ((TextView) view.findViewById(R.id.professor_university)).setText(universityName);
        ((TextView) view.findViewById(R.id.professor_description)).setText(description);

        view.findViewById(R.id.maps_button).setOnClickListener(button -> openMaps(mapsUrl));
        view.findViewById(R.id.website_button).setOnClickListener(button -> openWebsite(website));
        view.findViewById(R.id.professor_back_button).setOnClickListener(button -> {
            if (requireActivity() instanceof UniversityActivity) {
                ((UniversityActivity) requireActivity()).showSelectedDepartment();
            } else if (requireActivity() instanceof DepartmentActivity) {
                ((DepartmentActivity) requireActivity()).showSelectedDepartment();
            } else {
                requireActivity().finish();
            }
        });
    }

    private void openMaps(String mapsUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(requireContext(), R.string.maps_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private void openWebsite(String website) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(requireContext(), R.string.browser_unavailable, Toast.LENGTH_LONG).show();
        }
    }
}
