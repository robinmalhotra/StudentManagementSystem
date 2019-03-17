package com.example.studentmanagementsystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundAsyncTasks;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundIntentService;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundService;
import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;

import java.util.ArrayList;


public class CreateStudentActivity extends AppCompatActivity {
    private Intent holdIntent;
    private static final int ROLL_MAX = 100;
    private StudentHelperDatabase studentHelperDatabase;
    private String oldIdOfStudent;

    public String getOldIdOfStudent() {
        return oldIdOfStudent;
    }

    public void setOldIdOfStudent(String oldIdOfStudent) {
        this.oldIdOfStudent = oldIdOfStudent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);
        holdIntent = getIntent();

        studentHelperDatabase = new StudentHelperDatabase(this);

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

            changeButton.setText(getString(R.string.update_student));

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

        StudentTemplate studentTemplate = getIntent().getParcelableExtra("thisStudent");
        setOldIdOfStudent(studentTemplate.getStudentTemplateRoll());

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

        studentHelperDatabase = new StudentHelperDatabase(this);

        Button updateButton = findViewById(R.id.btnUpdateStudent);
        updateButton.setVisibility(TextView.INVISIBLE);
        Button changeButton = findViewById(R.id.btnSaveStudent);
        changeButton.setVisibility(TextView.VISIBLE);
        //Declare the EditText refrences.
        EditText etNameInput;
        EditText etRollInput;
        EditText etStandardInput;
        EditText etAgeInput;
        String operationOnStudent;


        //Connect the Edit Fields to the EditText refrences and set it Enabled.
        etNameInput = findViewById(R.id.etStudentNameText);
        etNameInput.setEnabled(true);
        etRollInput = findViewById(R.id.etStudentRollNumberText);
        etRollInput.setEnabled(true);
        etStandardInput = findViewById(R.id.etStudentStandardText);
        etStandardInput.setEnabled(true);
        etAgeInput = findViewById(R.id.etStudentAgeText);
        etAgeInput.setEnabled(true);

        //Get the strings that are typed in the EditText Fields.
        String stringOfStudentName = etNameInput.getText().toString();
        String stringOfStudentStandard = etStandardInput.getText().toString();
        String stringOfStudentRoll = etRollInput.getText().toString();
        int intRoll = Integer.parseInt(stringOfStudentRoll);
        String stringOfStudentAge = etAgeInput.getText().toString();

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
        if (stringOfStudentName.length() == 0) {
            etNameInput.requestFocus();
            etNameInput.setError("Name cannot be Empty");
        }
        //if Name's range lies outside the Alphabetic Range, then show error and request focus.
        else if (!stringOfStudentName.matches("\\b[a-zA-Z]+\\s[a-zA-Z]+\\b")) {
            etNameInput.requestFocus();
            etNameInput.setError("Enter Alphabets only");
        }
        //Check to see if the Roll number is within Range.
        else if (intRoll < 1 || intRoll > ROLL_MAX) {
            etRollInput.requestFocus();
            etRollInput.setError("Valid Roll Number between 1 - 100");
        }
        //Check to see if the Roll Id already exists.
        else if(rollmatch) {
            etRollInput.requestFocus();
            etRollInput.setError("Roll Id already exists");
        }
        //Standard should be between 1 to 12.
        else if (!stringOfStudentStandard.matches("([1-9]|1[0-2])")) {
            etStandardInput.requestFocus();
            etStandardInput.setError("Standard should be between 1 - 12");
        }
        //Age should be between 7 to 18
        else if (!stringOfStudentAge.matches("([7-9]|1[0-8])")) {
            etAgeInput.requestFocus();
            etAgeInput.setError("Age should be between 7 - 18");
        }

        //if All the validations are passed, then add the student or update the student based
        // on user's choice.
        else {

            Intent checkIntent = getIntent();
            Intent returnStudentIntent = new Intent();


            if (checkIntent.hasExtra("thisIsView") || checkIntent.hasExtra("thisIsEdit")) {

                StudentTemplate studentToUpdate = holdIntent.getParcelableExtra("thisStudent");

                studentToUpdate.setStudentTemplateName(stringOfStudentName);
                studentToUpdate.setStudentTemplateRoll(stringOfStudentRoll);
                studentToUpdate.setStudentTemplateStandard(stringOfStudentStandard);
                studentToUpdate.setStudentTemplateAge(stringOfStudentAge);

                operationOnStudent="updateIt";//getString(R.string.update_student_operation);

                generateDialog(studentToUpdate,operationOnStudent);
                //studentHelperDatabase.getWritableDatabase();
                //studentHelperDatabase.updateStudentInDb(studentToUpdate);

                returnStudentIntent.putExtra("updatedStudent", studentToUpdate);
                returnStudentIntent.putExtra("oldIdOfStudent",getOldIdOfStudent());
                setResult(RESULT_OK, returnStudentIntent);


                //Toast.makeText(CreateStudentActivity.this, "Student added in DB", Toast.LENGTH_LONG).show();


            }
            else {

                //Create an object of Student with the values that we got from the Interface.

                StudentTemplate studentToAdd = new StudentTemplate(stringOfStudentName,
                        stringOfStudentRoll, stringOfStudentStandard,
                        stringOfStudentAge);

                operationOnStudent = "addIt";//getString(R.string.add_student_operation);

                //studentHelperDatabase.getWritableDatabase();
                //studentHelperDatabase.addStudentinDb(studentToAdd);

                generateDialog(studentToAdd,operationOnStudent);
                Log.d("yyyyyy", "addStudentButton: student adds");

                returnStudentIntent.putExtra("addedStudent", studentToAdd);
                setResult(RESULT_OK, returnStudentIntent);
                Toast.makeText(CreateStudentActivity.this, "Student Added", Toast.LENGTH_LONG).show();

            }
        }
    }


    protected void generateDialog(final StudentTemplate studentToHandle, final String operationOnStudent) {



        final String[] items = {getString(R.string.alert_service),
                getString(R.string.alert_intentservice),
                getString(R.string.alert_async)};
        final int useService = 0, useIntentService = 1, useAsyncTasks = 2;

        //Alert Dialog that has context of this activity.
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateStudentActivity.this);
        builder.setTitle(R.string.userchoice_dbhandle_alert);
        //Sets the items of the Dialog.
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (which) {
                    case useService:
                        //Send the intent if the User chooses the VIEW option.
                        Intent forService = new Intent(CreateStudentActivity.this,
                                BackgroundService.class);
                        forService.putExtra("studentForDb", studentToHandle);
                        forService.putExtra("operation",operationOnStudent);
                        startService(forService);
                        Log.d("yyyyyy", "generateDialog: "+ studentToHandle.getStudentTemplateName());
                        finish();

                        break;

                    //Send the intent if the User choses the EDIT option.
                    case useIntentService:
                        Intent forIntentService = new Intent(CreateStudentActivity.this,
                                BackgroundIntentService.class);
                        forIntentService.putExtra("studentForDb", studentToHandle);
                        forIntentService.putExtra("operation",operationOnStudent);
                        startService(forIntentService);
                        finish();

                        break;
                    //Delete the Student.
                    case useAsyncTasks:
                        Intent forAsyncTasks = new Intent(CreateStudentActivity.this,
                                BackgroundAsyncTasks.class);
                        forAsyncTasks.putExtra(getString(R.string.operation_on_student),operationOnStudent);
                        forAsyncTasks.putExtra(getString(R.string.operation_on_student),operationOnStudent);
                        finish();
                        //startService(forIntentService);
                        break;
                }

            }
        });
        AlertDialog mAlert = builder.create();
        mAlert.show();

    }

}


