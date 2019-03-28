package com.example.studentmanagementsystem.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private static final int ROLL_MAX = 1000;
    private StudentHelperDatabase studentHelperDatabase;
    private String oldIdOfStudent;
    private Context context;

    StudentBroadcastReceiver studentBroadcastReceiver = new StudentBroadcastReceiver();

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter(Constants.FILTER_ACTION_KEY);
        LocalBroadcastManager.getInstance(this).registerReceiver(studentBroadcastReceiver,intentFilter);


    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(studentBroadcastReceiver);

    }


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

        StudentTemplate catchStudent = holdIntent.getParcelableExtra(Constants.THISSTUDENT);


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
        if (getIntent().hasExtra(Constants.THISISEDIT)) {

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
        if (getIntent().hasExtra(Constants.THISISVIEW)) {

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
        nameInput.setFocusable(true);
        EditText rollInput = findViewById(R.id.etStudentRollNumberText);
        rollInput.setEnabled(true);
        rollInput.setFocusable(true);
        EditText standardInput = findViewById(R.id.etStudentStandardText);
        standardInput.setEnabled(true);
        standardInput.setFocusable(true);
        EditText ageInput = findViewById(R.id.etStudentAgeText);
        ageInput.setEnabled(true);
        ageInput.setFocusable(true);

        StudentTemplate holdStudent = getIntent().getParcelableExtra(Constants.THISSTUDENT);

        setOldIdOfStudent(holdStudent.getStudentTemplateRoll());

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
        etNameInput.setFocusable(true);
        etRollInput = findViewById(R.id.etStudentRollNumberText);
        etRollInput.setEnabled(true);
        etRollInput.setFocusable(true);
        etStandardInput = findViewById(R.id.etStudentStandardText);
        etStandardInput.setEnabled(true);
        etStandardInput.setFocusable(true);
        etAgeInput = findViewById(R.id.etStudentAgeText);
        etAgeInput.setEnabled(true);
        etAgeInput.setFocusable(true);

        //Get the strings that are typed in the EditText Fields.
        String stringOfStudentName = etNameInput.getText().toString();
        String stringOfStudentStandard = etStandardInput.getText().toString();
        String stringOfStudentRoll = etRollInput.getText().toString();
        int intRoll = Integer.parseInt(stringOfStudentRoll);
        String stringOfStudentAge = etAgeInput.getText().toString();

        boolean rollmatch=false;

        //Changes the boolean rollmatch to true and true means we would check for the validation.
        if(holdIntent.hasExtra(Constants.ROLLSLIST)){
            ArrayList<Integer> thisRollsList=holdIntent.getIntegerArrayListExtra(Constants.ROLLSLIST);
            for(Integer thisRoll:thisRollsList) {
                if(thisRoll==intRoll){
                    rollmatch=true;
                }
            }
        }

        //If Name has no length then show error and request focus.
        if (stringOfStudentName.length() == 0) {
            etNameInput.requestFocus();
            etNameInput.setError(getString(R.string.namecantbeempty));
        }
        //if Name's range lies outside the Alphabetic Range, then show error and request focus.
        else if (!stringOfStudentName.matches(Constants.NAME_MATCH)) {
            etNameInput.requestFocus();
            etNameInput.setError(getString(R.string.enteralphabetsonly));
        }
        //Check to see if the Roll number is within Range.
        else if (intRoll < 1 || intRoll > ROLL_MAX) {
            etRollInput.requestFocus();
            etRollInput.setError(getString(R.string.valid_roll_error));
        }
        //Check to see if the Roll Id already exists.
        else if(rollmatch) {
            etRollInput.requestFocus();
            etRollInput.setError(getString(R.string.rolliderror));
        }
        //Standard should be between 1 to 12.
        else if (!stringOfStudentStandard.matches(Constants.STANDARD_MATCH)) {
            etStandardInput.requestFocus();
            etStandardInput.setError(getString(R.string.standarderror));
        }
        //Age should be between 7 to 18
        else if (!stringOfStudentAge.matches(Constants.AGE_MATCH)) {
            etAgeInput.requestFocus();
            etAgeInput.setError(getString(R.string.ageerror));
        }

        //if All the validations are passed, then add the student or update the student based
        // on user's choice.
        else {

            Intent checkIntent = getIntent();
            Intent returnStudentIntent = new Intent();


            if (checkIntent.hasExtra(Constants.THISISVIEW) || checkIntent.hasExtra(Constants.THISISEDIT)) {

                StudentTemplate studentToUpdate = holdIntent.getParcelableExtra(Constants.THISSTUDENT);

                setOldIdOfStudent(studentToUpdate.getStudentTemplateRoll());
                studentToUpdate.setStudentTemplateName(stringOfStudentName);
                studentToUpdate.setStudentTemplateRoll(stringOfStudentRoll);
                studentToUpdate.setStudentTemplateStandard(stringOfStudentStandard);
                studentToUpdate.setStudentTemplateAge(stringOfStudentAge);

                operationOnStudent=Constants.UPDATE_IT;

                generateDialog(studentToUpdate,operationOnStudent,getOldIdOfStudent());
                returnStudentIntent.putExtra(Constants.UPDATEDSTUDENT, studentToUpdate);
                setResult(RESULT_OK, returnStudentIntent);

            }
            else {

                //Create an object of Student with the values that we got from the Interface.

                StudentTemplate studentToAdd = new StudentTemplate(stringOfStudentName,
                        stringOfStudentRoll, stringOfStudentStandard,
                        stringOfStudentAge);

                operationOnStudent = Constants.ADD_IT;

                generateDialog(studentToAdd,operationOnStudent,null);

                returnStudentIntent.putExtra(Constants.ADDEDSTUDENT, studentToAdd);
                setResult(RESULT_OK, returnStudentIntent);
                Toast.makeText(CreateStudentActivity.this, "Student Added", Toast.LENGTH_LONG).show();

            }
        }
    }

/** Generates Dialog box when we press Add Button and choice is given how to interact with database
 * Choice 1: via Service
 * Choice 2: via Intent Service
 * Choice 3: via AsyncTasks
 * param@ studentToHandle: Student model object
 * param@ operationOnStudent: To Add or Update the student.
 */
    private void generateDialog(final StudentTemplate studentToHandle, final String operationOnStudent, final String oldIdOfStudent) {


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
                        Intent forService = new Intent(CreateStudentActivity.this,
                                BackgroundService.class);
                        forService.putExtra(Constants.STUDENT_FOR_DB, studentToHandle);
                        forService.putExtra(Constants.OPERATION,operationOnStudent);
                        forService.putExtra(Constants.OLD_ID_OF_STUDENT,oldIdOfStudent);
                        startService(forService);
                        //finish();
                        break;

                    case useIntentService:
                        Intent forIntentService = new Intent(CreateStudentActivity.this,
                                BackgroundIntentService.class);
                        forIntentService.putExtra(Constants.STUDENT_FOR_DB, studentToHandle);
                        forIntentService.putExtra(Constants.OPERATION,operationOnStudent);
                        forIntentService.putExtra(Constants.OLD_ID_OF_STUDENT,oldIdOfStudent);
                        startService(forIntentService);
                        //finish();
                        break;

                    case useAsyncTasks:

                        BackgroundAsyncTasks backgroundAsyncTasks = new BackgroundAsyncTasks(CreateStudentActivity.this);
                        backgroundAsyncTasks.execute(studentToHandle,operationOnStudent,oldIdOfStudent);
                        finish();
                        break;
                }

            }
        });
        AlertDialog mAlert = builder.create();
        mAlert.show();

    }

    /**Inner broadcast receiver that receives the broadcast if the services have indeed added the elements
     * in the database.
     */
    public class StudentBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            finish();

            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            Toast.makeText(CreateStudentActivity.this,"Broadcast Received",Toast.LENGTH_SHORT).show();

        }
    }


}


