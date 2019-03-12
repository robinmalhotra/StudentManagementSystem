package com.example.studentmanagementsystem.Student;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.StudentClass.StudentTemplate;

import java.util.ArrayList;


public class CreateStudent extends AppCompatActivity{
    private Intent holdIntent;
    private final int ROLL_MAX=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);
        holdIntent = getIntent();

        StudentTemplate catchStudent = holdIntent.getParcelableExtra("thisStudent");
        //Link the EditText fields.
        EditText nameInput = (EditText)findViewById(R.id.etStudentNameText);
        EditText rollInput = (EditText)findViewById(R.id.etStudentRollNumberText);
        EditText standardInput = (EditText)findViewById(R.id.etStudentStandardText);
        EditText ageInput = (EditText) findViewById(R.id.etStudentAgeText);

        //Set the visibility of the Buttons to be default.
        Button changeButton = (Button) findViewById(R.id.btnSaveStudent);
        changeButton.setVisibility(TextView.VISIBLE);
        Button updateButton = (Button) findViewById(R.id.btnUpdateStudent);
        updateButton.setVisibility(TextView.INVISIBLE);

        //To Edit the Student Details from Alert Dialog
        if(getIntent().hasExtra("thisIsEdit")) {
//            int studentPosition;
//            studentPosition = getIntent().getIntExtra("viewStudentPosition",0);

            nameInput.setText(catchStudent.getStudentTemplateName());
            nameInput.setEnabled(true);

            rollInput.setText(catchStudent.getStudentTemplateRoll());
            rollInput.setEnabled(true);

            standardInput.setText(catchStudent.getStudentTemplateStandard());
            standardInput.setEnabled(true);

            ageInput.setText(catchStudent.getStudentTemplateAge());
            ageInput.setEnabled(true);

            TextView changeText = (TextView)findViewById(R.id.tvAddStudentDetails);
            changeText.setVisibility(TextView.INVISIBLE);

            TextView showText = (TextView)findViewById(R.id.tvStudentDetails);
            showText.setVisibility(TextView.VISIBLE);

            changeButton.setText("Update Student");

            changeButton = (Button) findViewById(R.id.btnSaveStudent);
            changeButton.setVisibility(TextView.VISIBLE);

            updateButton = (Button) findViewById(R.id.btnUpdateStudent);
            updateButton.setVisibility(TextView.INVISIBLE);

        }

            //To View the Student Details only. This sets the EditText fields disabled so user cant
            // change it.
            if(getIntent().hasExtra("thisIsView")) {

//                int studentPosition;
//                studentPosition = getIntent().getIntExtra("viewStudentPosition",0);

                nameInput.setText(catchStudent.getStudentTemplateName());
                nameInput.setEnabled(false);

                rollInput.setText(catchStudent.getStudentTemplateRoll());
                rollInput.setEnabled(false);

                standardInput.setText(catchStudent.getStudentTemplateStandard());
                standardInput.setEnabled(false);

                ageInput.setText(catchStudent.getStudentTemplateAge());
                ageInput.setEnabled(false);

                TextView changeText = (TextView)findViewById(R.id.tvAddStudentDetails);
                changeText.setVisibility(TextView.INVISIBLE);

                TextView showText = (TextView)findViewById(R.id.tvStudentDetails);
                showText.setVisibility(TextView.VISIBLE);

                changeButton = (Button) findViewById(R.id.btnSaveStudent);
                changeButton.setVisibility(TextView.INVISIBLE);

                updateButton = (Button) findViewById(R.id.btnUpdateStudent);
                updateButton.setVisibility(TextView.VISIBLE);

            }
    }
    //Updates the EditText fields to be enabled so User can update the student from the same
    // activity and then goes invisible.
    public void updateStudentButton (View view) {
        EditText nameInput = findViewById(R.id.etStudentNameText);
        nameInput.setEnabled(true);
        EditText rollInput = findViewById(R.id.etStudentRollNumberText);
        rollInput.setEnabled(true);
        EditText standardInput =findViewById(R.id.etStudentStandardText);
        standardInput.setEnabled(true);
        EditText ageInput = findViewById(R.id.etStudentAgeText);
        ageInput.setEnabled(true);

        Button changeButton =  findViewById(R.id.btnSaveStudent);
        changeButton.setVisibility(TextView.VISIBLE);
        Button updateButton =  findViewById(R.id.btnUpdateStudent);
        updateButton.setVisibility(TextView.INVISIBLE);

    }
    //
    public void addStudentButton(View view) {

        Button updateButton =  findViewById(R.id.btnUpdateStudent);
        updateButton.setVisibility(TextView.INVISIBLE);
        Button changeButton =  findViewById(R.id.btnSaveStudent);
        changeButton.setVisibility(TextView.VISIBLE);
        //Declare the EditText refrences.
        EditText nameInput;
        EditText rollInput;
        EditText standardInput;
        EditText ageInput;

        //Connect the Edit Fields to the EditText refrences and set it Enabled.
        nameInput = findViewById(R.id.etStudentNameText);
        nameInput.setEnabled(true);
        rollInput = findViewById(R.id.etStudentRollNumberText);
        rollInput.setEnabled(true);
        standardInput = findViewById(R.id.etStudentStandardText);
        standardInput.setEnabled(true);
        ageInput =  findViewById(R.id.etStudentAgeText);
        ageInput.setEnabled(true);

        //Get the strings that are typed in the EditText Fields.
        String getStudentName = nameInput.getText().toString();
        String getStudentStandard = standardInput.getText().toString();
        String getStudentRoll = rollInput.getText().toString();
        int intRoll = Integer.parseInt(getStudentRoll);
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
        else if (intRoll<1 || intRoll>ROLL_MAX){
                rollInput.requestFocus();
                rollInput.setError("Valid Roll Number between 100 - 1000");
            Log.d("yyyyyy", "addStudentButton: rollnotinrange");

            if(holdIntent.hasExtra("rollsList")) {

                ArrayList<Integer> thisRollsList=holdIntent.getIntegerArrayListExtra("rollsList");
                Log.d("yyyyyy", "addStudentButton: RollsListcaught" +thisRollsList.get(0) );
                for(Integer thisRoll: thisRollsList) {
                    if(getStudentRoll.equals(thisRoll)) {
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
            //if All the validations are passed, then add the student or update the student based
            // on user's choice.
            Intent checkIntent = getIntent();
            Intent returnStudentIntent = new Intent();


            if (checkIntent.hasExtra("thisIsView") || checkIntent.hasExtra("thisIsEdit")) {



                    StudentTemplate studentToUpdate = holdIntent.getParcelableExtra("thisStudent");

                    studentToUpdate.setStudentTemplateName(getStudentName);
                    studentToUpdate.setStudentTemplateRoll(getStudentRoll);
                    studentToUpdate.setStudentTemplateStandard(getStudentStandard);
                    studentToUpdate.setStudentTemplateAge(getStudentAge);


                    returnStudentIntent.putExtra("updatedStudent", studentToUpdate);
                    setResult(RESULT_OK, returnStudentIntent);


                    Toast.makeText(CreateStudent.this, "Student Updated", Toast.LENGTH_LONG).show();
                    finish();


            } else {
                //Create an object of Student with the values that we got from the Interface.

                    Log.d("tttt", "addStudentButton: got it");
                    StudentTemplate studentToAdd = new StudentTemplate(getStudentName,
                            getStudentRoll, getStudentStandard,
                            getStudentAge);
                    Log.d("tttt", "addStudentButton: " + studentToAdd.getStudentTemplateName() + studentToAdd.getStudentTemplateAge());


                    returnStudentIntent.putExtra("addedStudent", studentToAdd);
                    setResult(RESULT_OK, returnStudentIntent);
                    Toast.makeText(CreateStudent.this, "Student Added", Toast.LENGTH_LONG).show();
                    finish();

            }
        }



        }

    }
