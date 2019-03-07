package com.example.studentmanagementsystem;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import java.io.Serializable;
import java.util.ArrayList;

public class StudentTemplate implements Serializable, Cloneable {

    private String studentTemplateName;
    private String studentTemplateRoll;
    private String studentTemplateStandard;
    private String studentTemplateAge;

    public StudentTemplate(String studentTemplateName, String studentTemplateRoll, String studentTemplateStandard, String studentTemplateAge) {
        this.studentTemplateName = studentTemplateName;
        this.studentTemplateRoll = studentTemplateRoll;
        this.studentTemplateStandard = studentTemplateStandard;
        this.studentTemplateAge = studentTemplateAge;
    }

    protected StudentTemplate(Parcel in) {
        studentTemplateName = in.readString();
        studentTemplateRoll = in.readString();
        studentTemplateStandard = in.readString();
        studentTemplateAge = in.readString();
    }

    //Getter Methods.
    public String getStudentTemplateName() {
        return studentTemplateName;
    }

    public String getStudentTemplateRoll() {
        return studentTemplateRoll;
    }

    public String getStudentTemplateStandard() {
        return studentTemplateStandard;
    }

    public String getStudentTemplateAge() {
        return studentTemplateAge;
    }
    //Setter Methods.

    public void setStudentTemplateName(String studentTemplateName) {
        this.studentTemplateName = studentTemplateName;
    }

    public void setStudentTemplateRoll(String studentTemplateRoll) {
        this.studentTemplateRoll = studentTemplateRoll;
    }

    public void setStudentTemplateStandard(String studentTemplateStandard) {
        this.studentTemplateStandard = studentTemplateStandard;
    }

    public void setStudentTemplateAge(String studentTemplateAge) {
        this.studentTemplateAge = studentTemplateAge;
    }
}
