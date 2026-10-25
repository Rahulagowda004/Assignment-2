package com.example.universityexplorer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class UniversityRepository {

    public static final class Professor {
        public final String name;
        public final String description;
        public final String website;
        public final String mapsUrl;

        Professor(String name, String description, String website, String mapsUrl) {
            this.name = name;
            this.description = description;
            this.website = website;
            this.mapsUrl = mapsUrl;
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

    private static Department department(String name, String professorName, String role,
            String website, String mapsUrl) {
        return new Department(name, new Professor(
                professorName, SAMPLE_DESCRIPTION + " " + role, website, mapsUrl));
    }

    private static final List<University> UNIVERSITIES = Collections.unmodifiableList(Arrays.asList(
            new University("University of Guelph",
                    department("Computer Science", "Dr. Avery Morgan", "Teaches software systems and computing education.",
                            "https://www.uoguelph.ca/computing/",
                            "https://www.google.com/maps/search/?api=1&query=University+of+Guelph+Computer+Science"),
                    department("Electrical Engineering", "Dr. Jordan Lee", "Teaches circuits, signals, and embedded systems.",
                            "https://www.uoguelph.ca/engineering/",
                            "https://www.google.com/maps/search/?api=1&query=University+of+Guelph+Electrical+Engineering"),
                    department("Mechanical Engineering", "Dr. Taylor Singh", "Teaches design methods and sustainable machines.",
                            "https://www.uoguelph.ca/engineering/",
                            "https://www.google.com/maps/search/?api=1&query=University+of+Guelph+Mechanical+Engineering"),
                    department("Mathematics", "Dr. Riley Chen", "Teaches applied mathematics and mathematical modelling.",
                            "https://www.uoguelph.ca/mathstat/",
                            "https://www.google.com/maps/search/?api=1&query=University+of+Guelph+Mathematics+Statistics")),
            new University("University of Toronto",
                    department("Computer Science", "Ishtiaque Ahmed", "Teaches algorithms and software engineering.",
                            "https://web.cs.toronto.edu/people/faculty-directory",
                            "https://www.google.com/maps/search/?api=1&query=University+of+Toronto+Computer+Science"),
                    department("Electrical Engineering", "Parham Aarabi", "Teaches communications and digital systems.",
                            "https://www.ece.utoronto.ca/faculty/faculty-directory/",
                            "https://www.google.com/maps/search/?api=1&query=University+of+Toronto+Electrical+Computer+Engineering"),
                    department("Mechanical Engineering", "Chris Beck", "Teaches robotics and engineering design.",
                            "https://directory.utoronto.ca/",
                            "https://www.google.com/maps/search/?api=1&query=University+of+Toronto+Mechanical+Industrial+Engineering"),
                    department("Mathematics", "James Bremer Jr.", "Teaches statistics and discrete mathematics.",
                            "https://directory.utoronto.ca/",
                            "https://www.google.com/maps/search/?api=1&query=University+of+Toronto+Mathematics")),
            new University("University of Waterloo",
                    department("Computer Science", "N. Asokan", "Teaches programming languages and systems.",
                            "https://uwaterloo.ca/computer-science/contacts",
                            "https://www.google.com/maps/search/?api=1&query=University+of+Waterloo+Computer+Science"),
                    department("Electrical Engineering", "Sebastian Fischmeister", "Teaches computer architecture and networks.",
                            "https://uwaterloo.ca/electrical-computer-engineering/contact-group/faculty",
                            "https://www.google.com/maps/search/?api=1&query=University+of+Waterloo+Electrical+Computer+Engineering"),
                    department("Mechanical Engineering", "John Magliaro", "Teaches automation and manufacturing.",
                            "https://uwaterloo.ca/mechanical-mechatronics-engineering/profile-type/faculty",
                            "https://www.google.com/maps/search/?api=1&query=University+of+Waterloo+Mechanical+Mechatronics+Engineering"),
                    department("Mathematics", "Hans De Sterck", "Teaches numerical analysis and modelling.",
                            "https://uwaterloo.ca/applied-mathematics/department-members/faculty-members",
                            "https://www.google.com/maps/search/?api=1&query=University+of+Waterloo+Faculty+of+Mathematics")),
            new University("University of British Columbia",
                    department("Computer Science", "Nick Harvey", "Teaches human-computer interaction and data systems.",
                            "https://www.cs.ubc.ca/people/faculty",
                            "https://www.google.com/maps/search/?api=1&query=University+of+British+Columbia+Computer+Science"),
                    department("Electrical Engineering", "Steven Wilton", "Teaches renewable energy and control systems.",
                            "https://ece.ubc.ca/people/faculty/",
                            "https://www.google.com/maps/search/?api=1&query=University+of+British+Columbia+Electrical+Computer+Engineering"),
                    department("Mechanical Engineering", "Yusuf Altintas", "Teaches materials and mechanical design.",
                            "https://mech.ubc.ca/our-department/",
                            "https://www.google.com/maps/search/?api=1&query=University+of+British+Columbia+Mechanical+Engineering"),
                    department("Mathematics", "Lior Silberman", "Teaches geometry and mathematical computation.",
                            "https://www.math.ubc.ca/about-our-department",
                            "https://www.google.com/maps/search/?api=1&query=University+of+British+Columbia+Mathematics"))
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
