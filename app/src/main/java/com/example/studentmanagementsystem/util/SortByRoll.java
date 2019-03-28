package com.example.studentmanagementsystem.util;

import com.example.studentmanagementsystem.model.StudentTemplate;

import java.util.Comparator;

public class SortByRoll implements Comparator<StudentTemplate> {

    @Override
    public int compare(StudentTemplate o1, StudentTemplate o2) {
        if(Integer.parseInt(o1.getStudentTemplateRoll())>Integer.parseInt(o2.getStudentTemplateRoll()))
            return 1;
        else
            return -1;
//        return (o1.getStudentTemplateRoll().compareTo(o2.getStudentTemplateRoll()));
    }
}