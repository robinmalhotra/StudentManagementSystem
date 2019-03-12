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


public class CreateStudent extends AppCompatActivity {
    private Intent holdIntent;
    private static final int ROLL_MAX = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);
        holdIntent = getIntent();

        StudentTemplate catchStudent = holdIntent.getParcelableExtra("thisStudent");
        //Link the EditText fields.
        EditText nameInput = findViewById(R.id.etStudentNameText);
        EditText rollInput = findViewById(R.id.etStudentRollNumberText);
        EditText standardInput = findViewById(R.id.etStudentStandardText);
        EditText ageInput = findViewById(R.id.etStudentAgeText);

        //Set the visibility of the Buttons to be default.
        Button changeButton = findViewById(R.id.btnSaveStudent);
        changeButton.setVisibility(TextView.VISIBLE);
        Button updateButton = findViewById(R.id.btnUpdateStudent);
        updateButton.setVisibility(TextView.INVISIBLE);

        //To Edit the Student Details from Alert Dialog
        if (getIntent().hasExtra("thisIsEdit")) {

            nameInput.setText(catchStudent.getStudentTemplateName());
            nameInput.setEnabled(true);

            rollInput.setText(catchStudent.getStudentTemplateRoll());
            rollInput.setEnabled(true);

            standardInput.setText(catchStudent.getStudentTemplateStandard());
            standardInput.setEnabled(true);

            ageInput.setText(catchStudent.getStudentTemplateAge());
            ageInput.setEnabled(true);

            TextView changeText = (TextView) findViewById(R.id.tvAddStudentDetails);
            changeText.setVisibility(TextView.INVISIBLE);

            TextView showText = (TextView) findViewById(R.id.tvStudentDetails);
            showText.setVisibility(TextView.VISIBLE);

            changeButton.setText("Update Student");

            changeButton = findViewById(R.id.btnSaveStudent);
            changeButton.setVisibility(TextView.VISIBLE);

            updateButton = findViewById(R.id.btnUpdateStudent);
            updateButton.setVisibility(TextView.INVISIBLE);

        }


        /** To View the Student Details only. This sets the EditText fields disabled so user cant
         *   change it.
         */
        if (getIntent().hasExtra("thisIsView")) {

            nameInput.setText(catchStudent.getStudentTemplateName());
            nameInput.setEnabled(false);

            rollInput.setText(catchStudent.getStudentTemplateRoll());
            rollInput.setEnabled(false);

            standardInput.setText(catchStudent.getStudentTemplateStandard());
            standardInput.setEnabled(false);

            ageInput.setText(catchStudent.getStudentTemplateAge());
            ageInput.setEnabled(false);

            TextView changeText = findViewById(R.id.tvAddStudentDetails);
            changeText.setVisibility(TextView.INVISIBLE);

            TextView showText = findViewById(R.id.tvStudentDetails);
            showText.setVisibility(TextView.VISIBLE);

            changeButton = findViewById(R.id.btnSaveStudent);
            changeButton.setVisibility(TextView.INVISIBLE);

            updateButton = findViewById(R.id.btnUpdateStudent);
            updateButton.setVisibility(TextView.VISIBLE);

        }
    }



    /** Updates the EditText fields to be enabled so User can update the student from the same
     *  activity and then goes invisible.
     *
     * @param view
     */
    public void updateStudentButton(View view) {
        EditText nameInput = findViewById(R.id.etStudentNameText);
        nameInput.setEnabled(true);
        EditText rollInput = findViewById(R.id.etStudentRollNumberText);
        rollInput.setEnabled(true);
        EditText standardInput = findViewById(R.id.etStudentStandardText);
        standardInput.setEnabled(true);
        EditText ageInput = findViewById(R.id.etStudentAgeText);
        ageInput.setEnabled(true);

        Button changeButton = findViewById(R.id.btnSaveStudent);
        changeButton.setVisibility(TextView.VISIBLE);
        Button updateButton = findViewById(R.id.btnUpdateStudent);
        updateButton.setVisibility(TextView.INVISIBLE);

    }

    /**Creates the student object that is sent to the main activity for either adding or updating.
     *
     * @param view
     */
    public void addStudentButton(View view) {

        Button updateButton = findViewById(R.id.btnUpdateStudent);
        updateButton.setVisibility(TextView.INVISIBLE);
        Button changeButton = findViewById(R.id.btnSaveStudent);
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
        ageInput = findViewById(R.id.etStudentAgeText);
        ageInput.setEnabled(true);

        //Get the strings that are typed in the EditText Fields.
        String getStudentName = nameInput.getText().toString();
        String getStudentStandard = standardInput.getText().toString();
        String getStudentRoll = rollInput.getText().toString();
        int intRoll = Integer.parseInt(getStudentRoll);
        String getStudentAge = ageInput.getText().toString();

        boolean rollmatch=false;

        //Changes the boolean rollmatch to true and true means we would check for the validation.
        if(holdIntent.hasExtra("rollsList")){
            ArrayList<Integer> thisRollsList=holdIntent.getIntegerArrayListExtra("rollsList");
            for(Integer thisRoll:thisRollsList) {
                if(thisRoll==intRoll){
                    rollmatch=true;
                }
            }
        }

        //If Name has no length then show error and request focus.
        if (getStudentName.length() == 0) {
            nameInput.requestFocus();
            nameInput.setError("Name cannot be Empty");
        }
        //if Name's range lies outside the Alphabetic Range, then show error and request focus.
        else if (!getStudentName.matches("\\b[a-zA-Z]+\\s[a-zA-Z]+\\b")) {
            nameInput.requestFocus();
            nameInput.setError("Enter Alphabets only");
        }
        //Check to see if the Roll number is within Range.
        else if (intRoll < 1 || intRoll > ROLL_MAX) {
            rollInput.requestFocus();
            rollInput.setError("Valid Roll Number between 100 - 1000");
            Log.d("yyyyyy", "addStudentButton: rollnotinrange");
        }
       //Check to see if the Roll Id already exists.
        else if(rollmatch==true) {
            rollInput.requestFocus();
            rollInput.setError("Roll Id already exists");
        }
        //Standard should be between 1 to 12.
        else if (!getStudentStandard.matches("([1-9]|1[0-2])")) {
            standardInput.requestFocus();
            standardInput.setError("Standard should be between 1 - 12");
        }
        //Age should be between 7 to 18
        else if (!getStudentAge.matches("([7-9]|1[0-8])")) {
            ageInput.requestFocus();
            ageInput.setError("Age should be between 7 - 18");
        }

        //if All the validations are passed, then add the student or update the student based
        // on user's choice.
        else {

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

            }
            else {

                //Create an object of Student with the values that we got from the Interface.

                StudentTemplate studentToAdd = new StudentTemplate(getStudentName,
                        getStudentRoll, getStudentStandard,
                        getStudentAge);

                returnStudentIntent.putExtra("addedStudent", studentToAdd);
                setResult(RESULT_OK, returnStudentIntent);
                Toast.makeText(CreateStudent.this, "Student Added", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
