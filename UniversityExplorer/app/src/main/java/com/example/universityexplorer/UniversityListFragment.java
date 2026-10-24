package com.example.universityexplorer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class UniversityListFragment extends Fragment {

    public interface OnUniversitySelectedListener {
        void onUniversitySelected(String universityName);
    }

    private static final String STATE_SELECTED_UNIVERSITY = "selected_university";
    private OnUniversitySelectedListener listener;
    private String selectedUniversity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnUniversitySelectedListener) {
            listener = (OnUniversitySelectedListener) context;
        } else {
            throw new IllegalStateException("Host activity must select universities");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_university_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            selectedUniversity = savedInstanceState.getString(STATE_SELECTED_UNIVERSITY);
        }

        List<String> names = new ArrayList<>();
        for (UniversityRepository.University university : UniversityRepository.getUniversities()) {
            names.add(university.name);
        }

        ListView listView = view.findViewById(R.id.university_list);
        listView.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, names));
        listView.setOnItemClickListener((parent, itemView, position, id) -> {
            selectedUniversity = names.get(position);
            listener.onUniversitySelected(selectedUniversity);
        });

        view.findViewById(R.id.university_back_button).setOnClickListener(button ->
                requireActivity().finish());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_SELECTED_UNIVERSITY, selectedUniversity);
        super.onSaveInstanceState(outState);
    }
}
