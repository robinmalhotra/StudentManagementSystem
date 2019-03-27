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
import com.example.studentmanagementsystem.communicator.Communicator;
import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;
import com.example.studentmanagementsystem.validate.Validate;

import java.util.ArrayList;

public class StudentAddFragment extends Fragment {
    private String oldIdOfStudent;
    private StudentBroadcastReceiver studentBroadcastReceiver = new StudentBroadcastReceiver();
    private Button mAddStudentButton;
    private EditText etStudentName, etStudentRoll, etStudentStandard, etStudentAge;
    private Context mContext;
    private Communicator mCommunicator;
    private ArrayList<StudentTemplate> mStudentList = new ArrayList<StudentTemplate>();


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
        // Inflate the layout for this fragment
        init(view);
        return view;
    }


    public void init(View view) {
        Log.d("yyyyyy", "init: ");
         etStudentName = view.findViewById(R.id.etStudentNameText);
         etStudentRoll = view.findViewById(R.id.etStudentRollNumberText);
         etStudentStandard = view.findViewById(R.id.etStudentStandardText);
         etStudentAge = view.findViewById(R.id.etStudentAgeText);
         mAddStudentButton = view.findViewById(R.id.btnSaveStudent);
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
    public void updateStudent(Bundle bundle){
        Log.d("yyyyyy", "updateStudent: ");
        if(bundle.getString(Constants.CODE_TO_ADD_STUDENT).equals(Constants.UPDATE_IT)) {
            etStudentName.setText(bundle.getString(Constants.NAME));
            etStudentRoll.setText(bundle.getString(Constants.ROLL_NO));
            etStudentStandard.setText(bundle.getString(Constants.STANDARD));
            etStudentAge.setText(bundle.getString(Constants.AGE));
            editMode();

        }else if(bundle.getString(Constants.CODE_TO_ADD_STUDENT).equals(Constants.ADD_IT)){

            mStudentList=bundle.getParcelableArrayList(Constants.STUDENT_LIST_FROM_MAIN);

            onClickButton();
        }
    }
    // For the Activity that only shows the Student Details.
    public void viewMode(StudentTemplate student){
        Log.d("yyyyyy", "viewMode: ");

        //StudentTemplate student = bundleData.getParcelable(Constants.STUDENT_LIST_FROM_MAIN);
        Log.d("yyyyyy", "viewMode: " + student.getStudentTemplateName());
        etStudentName.setText(student.getStudentTemplateName());
        etStudentRoll.setText(student.getStudentTemplateRoll());
        etStudentStandard.setText(student.getStudentTemplateStandard());
        etStudentAge.setText(student.getStudentTemplateAge());

        mAddStudentButton.setVisibility(View.GONE);
        etStudentName.setEnabled(false);
        etStudentRoll.setEnabled(false);
        etStudentStandard.setEnabled(false);
        etStudentAge.setEnabled(false);
    }

    //method to edit details of student
    @SuppressLint("SetTextI18n")
    public void editMode() {
        getActivity().setTitle(R.string.editstudenttitle);

        mAddStudentButton.findViewById(R.id.btnSaveStudent);
        mAddStudentButton.setText("Update Student");
        //to return name and roll number through bundle to StudentList Fragment and update data using preferred operation
        mAddStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etStudentName.getText().toString().trim();
                String roll = etStudentRoll.getText().toString().trim();
                String standard = etStudentStandard.getText().toString().trim();
                String age = etStudentAge.getText().toString().trim();
                Bundle bundle= new Bundle();
                bundle.putString(Constants.CODE_TO_ADD_STUDENT,Constants.UPDATE_IT);
                bundle.putString(Constants.NAME,name);
                bundle.putString(Constants.ROLL_NO,roll);
                bundle.putString(Constants.STANDARD,standard);
                bundle.putString(Constants.AGE,age);
                generateDialog(bundle,Constants.UPDATE_IT,roll);
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

        final String[] items = {getString(R.string.alert_service),
                getString(R.string.alert_intentservice),
                getString(R.string.alert_async)};
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
                        forService.putExtra("studentForDb", studentToHandle);
                        forService.putExtra("operation",operationOnStudent);
                        forService.putExtra("oldIdOfStudent",oldIdOfStudent);
                        mContext.startService(forService);
                        //finish();
                        break;

                    case useIntentService:
                        Intent forIntentService = new Intent(mContext,
                                BackgroundIntentService.class);
                        forIntentService.putExtra("studentForDb", studentToHandle);
                        forIntentService.putExtra("operation",operationOnStudent);
                        forIntentService.putExtra("oldIdOfStudent",oldIdOfStudent);
                        mContext.startService(forIntentService);
                        //finish();
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

    public void onClickButton() {
        mAddStudentButton.findViewById(R.id.btnSaveStudent);

        mAddStudentButton.setText(getString(R.string.addstudenttext));
        mAddStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to check if the the entered name is in valid format
                if (!Validate.isValidName(etStudentName.getText().toString().trim())) {
                    Toast.makeText(mContext, "Invalid Name",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //to check if the the entered roll number is in valid format
                if (!Validate.isValidRollNo(etStudentRoll.getText().toString().trim())) {
                    Toast.makeText(mContext, "Invalid Roll No.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //to check if the the entered roll number is unique or not
                if (!Validate.isUniqueRollNo(etStudentRoll.getText().toString().trim(),mStudentList)) {
                    Toast.makeText(getContext(), "Roll no. Not Unique",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //to return name and roll number through bundle to StudentList Fragment and save data using preferred operation
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

    }



    /**Inner broadcast receiver that receives the broadcast if the services have indeed added the elements
     * in the database.
     */
    class StudentBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            Toast.makeText(context,"Broadcast Received",Toast.LENGTH_SHORT).show();

        }
    }





