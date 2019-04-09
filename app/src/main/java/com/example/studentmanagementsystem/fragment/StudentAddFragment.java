package com.example.studentmanagementsystem.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.activity.CreateStudentActivity;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundAsyncTasks;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundIntentService;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundService;
import com.example.studentmanagementsystem.broadcastreceiver.StudentBroadcastReceiver;
import com.example.studentmanagementsystem.communicator.Communicator;
import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.dialog.GenerateDialog;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;
import com.example.studentmanagementsystem.validate.Validate;

import java.util.ArrayList;
import java.util.Objects;

public class StudentAddFragment extends Fragment {
    private String oldIdOfStudent;
    private StudentBroadcastReceiver studentBroadcastReceiver;
    private Button mAddStudentButton;
    private EditText etStudentName, etStudentRoll, etStudentStandard, etStudentAge;
    private TextView tvStudentDetails;
    private Context mContext;
    private Communicator mCommunicator;
    private ArrayList<StudentTemplate> mStudentList = new ArrayList<StudentTemplate>();
    private GenerateDialog generateDialog;


    @Override
    public void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter(Constants.FILTER_ACTION_KEY);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(studentBroadcastReceiver,intentFilter);

    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(studentBroadcastReceiver);

    }


    public StudentAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_add_student,container,false);
        init(view);
        return view;
    }

    /**
     * Initialise components.
     * @param view fragment view from XML.
     */
    public void init(View view) {
        generateDialog = new GenerateDialog(mContext);
        studentBroadcastReceiver = new StudentBroadcastReceiver(mContext);
         etStudentName = view.findViewById(R.id.etStudentNameText);
         etStudentRoll = view.findViewById(R.id.etStudentRollNumberText);
         etStudentStandard = view.findViewById(R.id.etStudentStandardText);
         etStudentAge = view.findViewById(R.id.etStudentAgeText);
         mAddStudentButton = view.findViewById(R.id.btnSaveStudent);
         tvStudentDetails = view.findViewById(R.id.tvAddStudentDetails);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        try {
            mCommunicator=(Communicator) mContext;
        }catch (ClassCastException e) {
            throw new ClassCastException(getString(R.string.error));
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCommunicator = null;
    }

    /**
     *method to update details of student
     *@param bundle - to pass data
     */
    public void addOrUpdateStudentInCreateStudentFragment(Bundle bundle){

        if(Objects.equals(bundle.getString(Constants.CODE_TO_ADD_STUDENT), Constants.UPDATE_IT)) {

            StudentTemplate studentTemplate = bundle.getParcelable(Constants.THISSTUDENT);
            assert studentTemplate != null;

            //Set the Edittext fields as per the student that needs to be updated.
            etStudentName.setText(studentTemplate.getStudentTemplateName());
            etStudentRoll.setText(studentTemplate.getStudentTemplateRoll());
            etStudentStandard.setText(studentTemplate.getStudentTemplateStandard());
            etStudentAge.setText(studentTemplate.getStudentTemplateAge());

            mStudentList=bundle.getParcelableArrayList(Constants.STUDENT_LIST_FROM_MAIN);

            oldIdOfStudent = studentTemplate.getStudentTemplateRoll();

            editMode();

        }else if(bundle.getString(Constants.CODE_TO_ADD_STUDENT).equals(Constants.ADD_IT)){

            mStudentList=bundle.getParcelableArrayList(Constants.STUDENT_LIST_FROM_MAIN);

            onClickButton();
        }
    }

    /**
     * For the Activity that only shows the Student Details.
      * @param student selected from the previous fragment's on touch.
     */
    public void viewMode(StudentTemplate student){

        etStudentName.setText(student.getStudentTemplateName());
        etStudentRoll.setText(student.getStudentTemplateRoll());
        etStudentStandard.setText(student.getStudentTemplateStandard());
        etStudentAge.setText(student.getStudentTemplateAge());

        mAddStudentButton.setVisibility(View.GONE);
        etStudentName.setEnabled(false);
        etStudentRoll.setEnabled(false);
        etStudentStandard.setEnabled(false);
        etStudentAge.setEnabled(false);
        etStudentName.setFocusable(false);
        etStudentRoll.setFocusable(false);
        etStudentStandard.setFocusable(false);
        etStudentAge.setFocusable(false);
    }

    /**
     * method to edit details of student
     */
    @SuppressLint("SetTextI18n")
    public void editMode() {
        getActivity().setTitle("Edit Student Details");
        tvStudentDetails.setText(Constants.UPDATE_STUDENT_DETAILS);
        etStudentRoll.setEnabled(false);
        mAddStudentButton.setText(Constants.UPDATE_STUDENT_DETAILS);
        mAddStudentButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //to check if the the entered name is in valid format
                if (!Validate.isValidName(etStudentName.getText().toString().trim())) {
                    etStudentName.requestFocus();
                    etStudentName.setError("Enter Valid Name");
                }
                //to check if the the entered roll number is in valid format
                else if (!Validate.isValidRollNo(etStudentRoll.getText().toString().trim())) {
                    etStudentRoll.requestFocus();
                    etStudentRoll.setError("Enter Valid Roll");
                }


                else{
                    String name = etStudentName.getText().toString().trim();
                    String roll = etStudentRoll.getText().toString().trim();
                    String standard = etStudentStandard.getText().toString().trim();
                    String age = etStudentAge.getText().toString().trim();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.CODE_TO_ADD_STUDENT, Constants.UPDATE_IT);
                    bundle.putString(Constants.NAME, name);
                    bundle.putString(Constants.ROLL_NO, roll);
                    bundle.putString(Constants.STANDARD, standard);
                    bundle.putString(Constants.AGE, age);

                    generateDialog(bundle,Constants.UPDATE_IT, oldIdOfStudent);
                    clearDetails();
                }
            }
        });
    }

    /** Generates Dialog box when we press Add Button and choice is given how to interact with database
     * Choice 1: via Service
     * Choice 2: via Intent Service
     * Choice 3: via AsyncTasks
     * param@ studentToHandle: Student model object
     * param@ operationOnStudent: To Add or Update the student.
     */
    private void generateDialog(final Bundle sendBundle,final String operationOnStudent, final String oldIdOfStudent) {

        final StudentTemplate studentToHandle = new StudentTemplate();

        studentToHandle.setStudentTemplateName(sendBundle.getString(Constants.NAME));
        studentToHandle.setStudentTemplateStandard(sendBundle.getString(Constants.STANDARD));
        studentToHandle.setStudentTemplateRoll(sendBundle.getString(Constants.ROLL_NO));
        studentToHandle.setStudentTemplateAge(sendBundle.getString(Constants.AGE));

        final String[] items = {Constants.SERVICE,
                Constants.INTENTSERVICE,
                Constants.ASYNC};
        final int useService = 0, useIntentService = 1, useAsyncTasks = 2;

        //Alert Dialog that has context of this activity.
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.userchoice_dbhandle_alert);
        //Sets the items of the Dialog.
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (which) {
                    case useService:
                        Intent forService = new Intent(mContext,
                                BackgroundService.class);
                        forService.putExtra(Constants.STUDENT_FOR_DB, studentToHandle);
                        forService.putExtra(Constants.OPERATION,operationOnStudent);
                        forService.putExtra(Constants.OLD_ID_OF_STUDENT,oldIdOfStudent);
                        mContext.startService(forService);

                        break;

                    case useIntentService:
                        Intent forIntentService = new Intent(mContext,
                                BackgroundIntentService.class);
                        forIntentService.putExtra(Constants.STUDENT_FOR_DB, studentToHandle);
                        forIntentService.putExtra(Constants.OPERATION,operationOnStudent);
                        forIntentService.putExtra(Constants.OLD_ID_OF_STUDENT,oldIdOfStudent);
                        mContext.startService(forIntentService);

                        break;

                    case useAsyncTasks:

                        BackgroundAsyncTasks backgroundAsyncTasks = new BackgroundAsyncTasks(mContext);
                        backgroundAsyncTasks.execute(studentToHandle,operationOnStudent,oldIdOfStudent);

                        break;
                }
                mCommunicator.communicateAdd(sendBundle);

            }
        });

        AlertDialog mAlert = builder.create();
        mAlert.show();

    }


    /**
     * When the add student button is clicked for a new student to be added.
     */
    public void onClickButton() {

        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.add_student_details));
        tvStudentDetails.setText(Constants.ADD_STUDENT_DETAILS);
        mAddStudentButton.findViewById(R.id.btnSaveStudent);
        etStudentRoll.setEnabled(true);
        mAddStudentButton.setText(getString(R.string.addstudenttext));
        mAddStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Validate.isValidName(etStudentName.getText().toString().trim())) {
                    etStudentName.requestFocus();
                    Toast.makeText(mContext, getString(R.string.error),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (!Validate.isValidRollNo(etStudentRoll.getText().toString().trim())) {
                    etStudentRoll.requestFocus();
                    Toast.makeText(mContext, Constants.INVALIDROLL,
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (!Validate.isUniqueRollNo(etStudentRoll.getText().toString().trim(),mStudentList)) {
                    etStudentRoll.requestFocus();
                    Toast.makeText(getContext(), Constants.ROLLNOTUNIQUE,
                            Toast.LENGTH_LONG).show();
                    return;
                }


                String name = etStudentName.getText().toString().trim();
                String roll = etStudentRoll.getText().toString().trim();
                String standard = etStudentStandard.getText().toString().trim();
                String age = etStudentAge.getText().toString().trim();

                Bundle bundle= new Bundle();
                bundle.putString(Constants.CODE_TO_ADD_STUDENT,Constants.ADD_IT);
                bundle.putString(Constants.NAME,name);
                bundle.putString(Constants.ROLL_NO,roll);
                bundle.putString(Constants.STANDARD,standard);
                bundle.putString(Constants.AGE,age);

                etStudentName.getText().clear();
                etStudentRoll.getText().clear();
                etStudentStandard.getText().clear();
                etStudentAge.getText().clear();

                generateDialog(bundle,Constants.ADD_IT,roll);
            }
        });
    }

    /**
     * To clear the details when we swipe to the next fragment
     */
    public void clearDetails() {
        etStudentName.getText().clear();
        etStudentRoll.getText().clear();
        etStudentStandard.getText().clear();
        etStudentAge.getText().clear();
    }

}



