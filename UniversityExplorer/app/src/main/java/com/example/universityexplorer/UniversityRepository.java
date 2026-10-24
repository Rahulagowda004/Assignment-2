package com.example.universityexplorer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class UniversityRepository {

    public static final class Professor {
        public final String name;
        public final String description;

        Professor(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    public static final class Department {
        public final String name;
        public final Professor professor;

        Department(String name, Professor professor) {
            this.name = name;
            this.professor = professor;
        }
    }

    public static final class University {
        public final String name;
        public final List<Department> departments;

        University(String name, Department... departments) {
            this.name = name;
            this.departments = Collections.unmodifiableList(Arrays.asList(departments));
        }
    }

    private static final String SAMPLE_DESCRIPTION =
            "Sample faculty profile for demonstration purposes.";

    private static Department department(String name, String professorName, String role) {
        return new Department(name, new Professor(professorName, SAMPLE_DESCRIPTION + " " + role));
    }

    private static final List<University> UNIVERSITIES = Collections.unmodifiableList(Arrays.asList(
            new University("University of Guelph",
                    department("Computer Science", "Dr. Avery Morgan", "Teaches software systems and computing education."),
                    department("Electrical Engineering", "Dr. Jordan Lee", "Teaches circuits, signals, and embedded systems."),
                    department("Mechanical Engineering", "Dr. Taylor Singh", "Teaches design methods and sustainable machines."),
                    department("Mathematics", "Dr. Riley Chen", "Teaches applied mathematics and mathematical modelling.")),
            new University("University of Toronto",
                    department("Computer Science", "Dr. Morgan Patel", "Teaches algorithms and software engineering."),
                    department("Electrical Engineering", "Dr. Casey Wilson", "Teaches communications and digital systems."),
                    department("Mechanical Engineering", "Dr. Alex Rivera", "Teaches robotics and engineering design."),
                    department("Mathematics", "Dr. Jamie Brooks", "Teaches statistics and discrete mathematics.")),
            new University("University of Waterloo",
                    department("Computer Science", "Dr. Quinn Foster", "Teaches programming languages and systems."),
                    department("Electrical Engineering", "Dr. Samira Khan", "Teaches computer architecture and networks."),
                    department("Mechanical Engineering", "Dr. Devon Clark", "Teaches automation and manufacturing."),
                    department("Mathematics", "Dr. Robin Ellis", "Teaches numerical analysis and modelling.")),
            new University("University of British Columbia",
                    department("Computer Science", "Dr. Cameron Park", "Teaches human-computer interaction and data systems."),
                    department("Electrical Engineering", "Dr. Drew Martin", "Teaches renewable energy and control systems."),
                    department("Mechanical Engineering", "Dr. Leslie Wong", "Teaches materials and mechanical design."),
                    department("Mathematics", "Dr. Hayden Moore", "Teaches geometry and mathematical computation."))
    ));

    private UniversityRepository() {
    }

    public static List<University> getUniversities() {
        return UNIVERSITIES;
    }

    public static University findUniversity(String name) {
        for (University university : UNIVERSITIES) {
            if (university.name.equals(name)) {
                return university;
            }
        }
        return null;
    }

    public static Department findDepartment(String universityName, String departmentName) {
        University university = findUniversity(universityName);
        if (university != null) {
            for (Department department : university.departments) {
                if (department.name.equals(departmentName)) {
                    return department;
                }
            }
        }
        return null;
    }
}
