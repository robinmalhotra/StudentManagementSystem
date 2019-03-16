package com.example.studentmanagementsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentTemplate implements Parcelable {

    private String studentTemplateName;
    private String studentTemplateRoll;
    private String studentTemplateStandard;
    private String studentTemplateAge;

    //Empty Constructor.
    public StudentTemplate() {}

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

    public static final Creator<StudentTemplate> CREATOR = new Creator<StudentTemplate>() {
        @Override
        public StudentTemplate createFromParcel(Parcel in) {
            return new StudentTemplate(in);
        }

        @Override
        public StudentTemplate[] newArray(int size) {
            return new StudentTemplate[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentTemplateName);
        dest.writeString(studentTemplateRoll);
        dest.writeString(studentTemplateStandard);
        dest.writeString(studentTemplateAge);
    }
}
