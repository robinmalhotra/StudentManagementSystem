package com.example.studentmanagementsystem.validator;


import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    /*
    method to check if user enters valid name only i.e characters
    *@param name-name of student
    *@return true or false depending upon validation
    */
    public static boolean isValidName(String name) {
        String PATTERN = Constants.NAME_MATCH;
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    /*
    method to check if user enters valid roll no only i.e integer values
    *@param rollNo-roll number of student
    *@return true or false depending upon validation
    */
    public static boolean isValidRollNo(String rollNo) {
        String PATTERN = Constants.ROLL_MATCH;
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(rollNo);
        return matcher.matches();
    }
    /*method to check if the entered roll no is unique or not
     *@param rollNo-roll number of student
     *@param studentList - arraylist of object Student
     *@return false if roll no is unique else true
     */
    public static boolean isUniqueRollNo(String rollNo,final ArrayList<Integer> rollsList){
        for(Integer studentroll: rollsList){
            if(studentroll.equals(rollNo)){
                return false;
            }
        }
        return true;
    }
}