package com.example.studentmanagementsystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundAsyncTasks;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundIntentService;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundService;
import com.example.studentmanagementsystem.broadcastreceiver.StudentBroadcastReceiver;
import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;
import com.example.studentmanagementsystem.validator.Validator;

import java.util.ArrayList;


public class CreateStudentActivity extends AppCompatActivity {
    private Intent holdIntent;
    private StudentHelperDatabase studentHelperDatabase;
    private String oldIdOfStudent;
    private EditText etNameInput, etRollInput, etStandardInput, etAgeInput;
    private TextView changeText, showText;
    private Button changeButton, updateButton;

    private StudentBroadcastReceiver studentBroadcastReceiver = new StudentBroadcastReceiver(this);

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);

        init();
        setUpEditMode();
        setUpViewMode();

    }

    /**
     * Initialise components
     */
    private void init() {
        holdIntent = getIntent();

        studentHelperDatabase = new StudentHelperDatabase(this);

        etNameInput = findViewById(R.id.etStudentNameText);
        etRollInput = findViewById(R.id.etStudentRollNumberText);
        etStandardInput = findViewById(R.id.etStudentStandardText);
        etAgeInput = findViewById(R.id.etStudentAgeText);


        changeButton = findViewById(R.id.btnSaveStudent);
        changeButton.setVisibility(TextView.VISIBLE);
        updateButton = findViewById(R.id.btnUpdateStudent);
        updateButton.setVisibility(TextView.INVISIBLE);
    }

    /**
     * Set the Activity to for Edit mode.
     */
    private void setUpEditMode(){
        if (getIntent().hasExtra(Constants.THISISEDIT)) {

            StudentTemplate catchStudent = holdIntent.getParcelableExtra(Constants.THISSTUDENT);

            etNameInput.setText(catchStudent.getStudentTemplateName());
            etNameInput.setEnabled(true);

            etRollInput.setText(catchStudent.getStudentTemplateRoll());
            etRollInput.setEnabled(true);

            etStandardInput.setText(catchStudent.getStudentTemplateStandard());
            etStandardInput.setEnabled(true);

            etAgeInput.setText(catchStudent.getStudentTemplateAge());
            etAgeInput.setEnabled(true);

            changeText =  findViewById(R.id.tvAddStudentDetails);
            changeText.setVisibility(TextView.INVISIBLE);

            showText =  findViewById(R.id.tvStudentDetails);
            showText.setVisibility(TextView.VISIBLE);

            changeButton.setText(Constants.UPDATE_STUDENT_DETAILS);

            changeButton = findViewById(R.id.btnSaveStudent);
            changeButton.setVisibility(TextView.VISIBLE);

            updateButton = findViewById(R.id.btnUpdateStudent);
            updateButton.setVisibility(TextView.INVISIBLE);

        }
    }

    /**
     * Setup the activity for View Mode.
     */
    private void setUpViewMode() {
        if (getIntent().hasExtra(Constants.THISISVIEW)) {
            StudentTemplate catchStudent = holdIntent.getParcelableExtra(Constants.THISSTUDENT);

            etNameInput.setText(catchStudent.getStudentTemplateName());
            etNameInput.setEnabled(false);

            etRollInput.setText(catchStudent.getStudentTemplateRoll());
            etRollInput.setEnabled(false);

            etStandardInput.setText(catchStudent.getStudentTemplateStandard());
            etStandardInput.setEnabled(false);

            etAgeInput.setText(catchStudent.getStudentTemplateAge());
            etAgeInput.setEnabled(false);

            changeText = findViewById(R.id.tvAddStudentDetails);
            changeText.setVisibility(TextView.INVISIBLE);

            showText = findViewById(R.id.tvStudentDetails);
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
    public void updateStudentButtonClickListener(View view) {
        etNameInput.setEnabled(true);
        etNameInput.setFocusable(true);
        etRollInput.setEnabled(true);
        etRollInput.setFocusable(true);
        etStandardInput.setEnabled(true);
        etStandardInput.setFocusable(true);
        etAgeInput.setEnabled(true);
        etAgeInput.setFocusable(true);
        StudentTemplate holdStudent = getIntent().getParcelableExtra(Constants.THISSTUDENT);
        oldIdOfStudent = holdStudent.getStudentTemplateRoll();
        changeButton.setVisibility(TextView.VISIBLE);
        updateButton.setVisibility(TextView.INVISIBLE);
    }

    /**Creates the student object that is sent to the main activity for either adding or updating.
     *
     * @param view
     */
    public void addStudentButton(View view) {

        studentHelperDatabase = new StudentHelperDatabase(this);
        updateButton.setVisibility(TextView.INVISIBLE);
        changeButton.setVisibility(TextView.VISIBLE);

        String operationOnStudent;

        etNameInput.setEnabled(true);
        etNameInput.setFocusable(true);
        etRollInput.setEnabled(true);
        etRollInput.setFocusable(true);
        etStandardInput.setEnabled(true);
        etStandardInput.setFocusable(true);
        etAgeInput.setEnabled(true);
        etAgeInput.setFocusable(true);

        String stringOfStudentName = etNameInput.getText().toString();
        String stringOfStudentStandard = etStandardInput.getText().toString();
        String stringOfStudentRoll = etRollInput.getText().toString();
        int intRoll = Integer.parseInt(stringOfStudentRoll);
        String stringOfStudentAge = etAgeInput.getText().toString();

        boolean rollmatch=false;

        if(holdIntent.hasExtra(Constants.ROLLSLIST)){
            ArrayList<Integer> thisRollsList=holdIntent.getIntegerArrayListExtra(Constants.ROLLSLIST);
            for(Integer thisRoll:thisRollsList) {
                if(thisRoll==intRoll){
                    rollmatch=true;
                }
            }
        }

        if (!Validator.isValidName(stringOfStudentName.trim())) {
            etNameInput.requestFocus();
            etNameInput.setError(getString(R.string.namevalidstring));
        }
        //to check if the the entered roll number is in valid format
        else if (!Validator.isValidRollNo(stringOfStudentRoll.trim())) {
            etRollInput.requestFocus();
            etRollInput.setError(getString(R.string.rollvalidstring));
        }

        else if (rollmatch) {
            etRollInput.requestFocus();
            etRollInput.setError(getString(R.string.uniquerollstring));
        }

        else {

            Intent checkIntent = getIntent();
            Intent returnStudentIntent = new Intent();


            if (checkIntent.hasExtra(Constants.THISISVIEW) || checkIntent.hasExtra(Constants.THISISEDIT)) {

                StudentTemplate studentToUpdate = holdIntent.getParcelableExtra(Constants.THISSTUDENT);

                oldIdOfStudent = studentToUpdate.getStudentTemplateRoll();
                studentToUpdate.setStudentTemplateName(stringOfStudentName);
                studentToUpdate.setStudentTemplateRoll(stringOfStudentRoll);
                studentToUpdate.setStudentTemplateStandard(stringOfStudentStandard);
                studentToUpdate.setStudentTemplateAge(stringOfStudentAge);

                operationOnStudent=Constants.UPDATE_IT;

                generateDialog(studentToUpdate,operationOnStudent,oldIdOfStudent);
                returnStudentIntent.putExtra(Constants.UPDATEDSTUDENT, studentToUpdate);
                setResult(RESULT_OK, returnStudentIntent);

            }
            else {

                StudentTemplate studentToAdd = new StudentTemplate(stringOfStudentName,
                        stringOfStudentRoll, stringOfStudentStandard,
                        stringOfStudentAge);

                operationOnStudent = Constants.ADD_IT;

                generateDialog(studentToAdd,operationOnStudent,null);

                returnStudentIntent.putExtra(Constants.ADDEDSTUDENT, studentToAdd);
                setResult(RESULT_OK, returnStudentIntent);

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

                        break;

                    case useIntentService:
                        Intent forIntentService = new Intent(CreateStudentActivity.this,
                                BackgroundIntentService.class);
                        forIntentService.putExtra(Constants.STUDENT_FOR_DB, studentToHandle);
                        forIntentService.putExtra(Constants.OPERATION,operationOnStudent);
                        forIntentService.putExtra(Constants.OLD_ID_OF_STUDENT,oldIdOfStudent);
                        startService(forIntentService);

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




}


