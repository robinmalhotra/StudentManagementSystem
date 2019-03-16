package com.example.studentmanagementsystem.util;

import com.example.studentmanagementsystem.model.StudentTemplate;

import java.util.Comparator;

public class SortByName implements Comparator<StudentTemplate> {

    @Override
    public int compare(StudentTemplate o1, StudentTemplate o2) {

        return (o1.getStudentTemplateName().compareTo(o2.getStudentTemplateName()));
    }
}