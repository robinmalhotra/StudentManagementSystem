package com.example.studentmanagementsystem;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.studentmanagementsystem.R.layout.activity_create_student;

public class CreateStudent extends AppCompatActivity{
    //Link View Fields to the activity.



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);
        EditText nameInput = (EditText)findViewById(R.id.studentNameText);
        EditText rollInput = (EditText)findViewById(R.id.studentRollNumberText);
        EditText standardInput = (EditText)findViewById(R.id.studentStandardText);
        EditText ageInput = (EditText) findViewById(R.id.studentAgeText);



        //Set the visibility of the Buttons to be default.
        Button changeButton = (Button) findViewById(R.id.saveStudent);
        changeButton.setVisibility(TextView.VISIBLE);
        Button updateButton = (Button) findViewById(R.id.updateStudent);
        updateButton.setVisibility(TextView.INVISIBLE);

        //To Edit the Student Details from Alert Dialog
        if(getIntent().hasExtra("thisIsEdit")) {
            int studentPosition;
            studentPosition = getIntent().getIntExtra("viewStudentPosition",0);

            nameInput.setText(MainActivity.mStudentList.get(studentPosition).
                    getStudentTemplateName());
            nameInput.setEnabled(true);

            rollInput.setText(MainActivity.mStudentList.get(studentPosition).
                    getStudentTemplateRoll());
            rollInput.setEnabled(true);

            standardInput.setText(MainActivity.mStudentList.get(studentPosition).
                    getStudentTemplateStandard());
            standardInput.setEnabled(true);

            ageInput.setText(MainActivity.mStudentList.get(studentPosition).
                    getStudentTemplateAge());
            ageInput.setEnabled(true);

            TextView changeText = (TextView)findViewById(R.id.textView2);
            changeText.setVisibility(TextView.INVISIBLE);

            TextView showText = (TextView)findViewById(R.id.tvStudentDetails);
            showText.setVisibility(TextView.VISIBLE);

            changeButton.setText("Update Student");

            changeButton = (Button) findViewById(R.id.saveStudent);
            changeButton.setVisibility(TextView.VISIBLE);

            updateButton = (Button) findViewById(R.id.updateStudent);
            updateButton.setVisibility(TextView.INVISIBLE);

        }

            //To View the Student Details.
            if(getIntent().hasExtra("thisIsView")) {

                int studentPosition;
                studentPosition = getIntent().getIntExtra("viewStudentPosition",0);

                nameInput.setText(MainActivity.mStudentList.get(studentPosition).
                        getStudentTemplateName());
                nameInput.setEnabled(false);

                rollInput.setText(MainActivity.mStudentList.get(studentPosition).
                        getStudentTemplateRoll());
                rollInput.setEnabled(false);

                standardInput.setText(MainActivity.mStudentList.get(studentPosition).
                        getStudentTemplateStandard());
                standardInput.setEnabled(false);

                ageInput.setText(MainActivity.mStudentList.get(studentPosition).
                        getStudentTemplateAge());
                ageInput.setEnabled(false);

                TextView changeText = (TextView)findViewById(R.id.textView2);
                changeText.setVisibility(TextView.INVISIBLE);

                TextView showText = (TextView)findViewById(R.id.tvStudentDetails);
                showText.setVisibility(TextView.VISIBLE);

                changeButton = (Button) findViewById(R.id.saveStudent);
                changeButton.setVisibility(TextView.INVISIBLE);

                updateButton = (Button) findViewById(R.id.updateStudent);
                updateButton.setVisibility(TextView.VISIBLE);

            }
    }

    public void updateStudentButton (View view) {
        EditText nameInput = (EditText)findViewById(R.id.studentNameText);
        nameInput.setEnabled(true);
        EditText rollInput = (EditText)findViewById(R.id.studentRollNumberText);
        rollInput.setEnabled(true);
        EditText standardInput = (EditText)findViewById(R.id.studentStandardText);
        standardInput.setEnabled(true);
        EditText ageInput = (EditText) findViewById(R.id.studentAgeText);
        ageInput.setEnabled(true);

        Button changeButton = (Button) findViewById(R.id.saveStudent);
        changeButton.setVisibility(TextView.VISIBLE);
        Button updateButton = (Button) findViewById(R.id.updateStudent);
        updateButton.setVisibility(TextView.INVISIBLE);

    }

    public void addStudentButton(View view) {

        int position = getIntent().getIntExtra("viewStudentPosition",0);

        Button updateButton = (Button) findViewById(R.id.updateStudent);
        updateButton.setVisibility(TextView.INVISIBLE);
        Button changeButton = (Button) findViewById(R.id.saveStudent);
        changeButton.setVisibility(TextView.VISIBLE);
        //Declare the EditText refrences.
        EditText nameInput;
        EditText rollInput;
        EditText standardInput;
        EditText ageInput;

        //Connect the Edit Fields to the EditText refrences and set it Enabled.
        nameInput = (EditText)findViewById(R.id.studentNameText);
        nameInput.setEnabled(true);
        rollInput = (EditText)findViewById(R.id.studentRollNumberText);
        rollInput.setEnabled(true);
        standardInput = (EditText)findViewById(R.id.studentStandardText);
        standardInput.setEnabled(true);
        ageInput = (EditText) findViewById(R.id.studentAgeText);
        ageInput.setEnabled(true);

        //Get the strings that are typed in the EditText Fields.
        String getStudentName = nameInput.getText().toString();
        String getStudentStandard = standardInput.getText().toString();
        String getStudentRoll = rollInput.getText().toString();
        String getStudentAge = ageInput.getText().toString();

        //If Name has no length then show error and request focus.
        if(getStudentName.length()==0) {
            nameInput.requestFocus();
            nameInput.setError("Name cannot be Empty");
        }
        //if Name's range lies outside the Alphabetic Range, then show error and request focus.
        else if (!getStudentName.matches("\\b[a-zA-Z]+\\s[a-zA-Z]+\\b")) {
            nameInput.requestFocus();
            nameInput.setError("Enter Alphabets only");
        }
        else if (!getStudentRoll.matches("(?:\\b|-)([1-9]{1,2}[0]?|100)\\b")) {
                rollInput.requestFocus();
                rollInput.setError("Valid Roll Number between 100 - 1000");

            if(!MainActivity.mStudentList.isEmpty()) {
                for(StudentTemplate holStudent:MainActivity.mStudentList) {
                    if(getStudentRoll.equals(holStudent.getStudentTemplateRoll())) {
                        rollInput.requestFocus();
                        rollInput.setError("Unique Roll No. needed");
                    }
                }
            }
        }

        else if(!getStudentStandard.matches("[0-9]+")) {
            standardInput.requestFocus();
            standardInput.setError("Standard should be between 1 - 12");
        }

        else if(!getStudentAge.matches("[0-9]+")) {
            ageInput.requestFocus();
            ageInput.setError("Age should be between 10 - 23");
        }

        else {
            if (getIntent().hasExtra("thisIsView") || getIntent().hasExtra("thisIsEdit")) {

                StudentTemplate studentToUpdate = MainActivity.mStudentList.get(position);


                studentToUpdate.setStudentTemplateName(getStudentName);
                studentToUpdate.setStudentTemplateRoll(getStudentRoll);
                studentToUpdate.setStudentTemplateStandard(getStudentStandard);
                studentToUpdate.setStudentTemplateAge(getStudentAge);
                MainActivity.adapter.notifyDataSetChanged();

                Toast.makeText(CreateStudent.this, "Student Updated", Toast.LENGTH_LONG).show();
                finish();

            } else {
                //Create an object of Student with the values that we got from the Interface.
                StudentTemplate studentIndividual = new StudentTemplate(getStudentName,
                        getStudentRoll, getStudentStandard,
                        getStudentAge);

                MainActivity.mStudentList.add(studentIndividual);
                Toast.makeText(CreateStudent.this, "Student Added", Toast.LENGTH_LONG).show();
                MainActivity.adapter.notifyDataSetChanged();
                finish();
            }
        }



        }

    }
