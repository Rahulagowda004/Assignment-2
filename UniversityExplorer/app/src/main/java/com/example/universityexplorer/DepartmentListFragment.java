package com.example.universityexplorer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class DepartmentListFragment extends Fragment {

    public interface OnDepartmentSelectedListener {
        void onDepartmentSelected(String universityName, String departmentName);
    }

    private static final String ARG_UNIVERSITY = "university";
    private static final String STATE_SELECTED_DEPARTMENT = "selected_department";
    private OnDepartmentSelectedListener listener;
    private String universityName;
    private String selectedDepartment;

    public static DepartmentListFragment newInstance(String universityName) {
        DepartmentListFragment fragment = new DepartmentListFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_UNIVERSITY, universityName);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDepartmentSelectedListener) {
            listener = (OnDepartmentSelectedListener) context;
        } else {
            throw new IllegalStateException("Host activity must select departments");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_department_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        universityName = getArguments() == null ? null
                : getArguments().getString(ARG_UNIVERSITY);
        if (savedInstanceState != null) {
            selectedDepartment = savedInstanceState.getString(STATE_SELECTED_DEPARTMENT);
        }

        TextView universityText = view.findViewById(R.id.department_university);
        universityText.setText(universityName);

        UniversityRepository.University university =
                UniversityRepository.findUniversity(universityName);
        List<String> names = new ArrayList<>();
        if (university != null) {
            for (UniversityRepository.Department department : university.departments) {
                names.add(department.name);
            }
        }

        ListView listView = view.findViewById(R.id.department_list);
        listView.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, names));
        listView.setOnItemClickListener((parent, itemView, position, id) -> {
            selectedDepartment = names.get(position);
            listener.onDepartmentSelected(universityName, selectedDepartment);
        });
        view.findViewById(R.id.department_back_button).setOnClickListener(button ->
                requireActivity().finish());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_SELECTED_DEPARTMENT, selectedDepartment);
        super.onSaveInstanceState(outState);
    }
}
