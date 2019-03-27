package com.example.studentmanagementsystem.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
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
import com.example.studentmanagementsystem.communicator.Communicator;
import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.fragment.StudentAddFragment;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;

import java.util.ArrayList;


public class CreateStudentActivity extends AppCompatActivity implements Communicator {
    private Bundle mBundle;
    private StudentAddFragment mStudentAddFragment;
    StudentTemplate studentTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_create_student);

    studentTemplate=getIntent().getParcelableExtra(Constants.THISSTUDENT);

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        mStudentAddFragment=new StudentAddFragment();

        fragmentTransaction.add(R.id.fragment_view,mStudentAddFragment,"");
        fragmentTransaction.commit();


    }
    @Override
    protected void onStart() {
        super.onStart();
        mStudentAddFragment.viewMode(studentTemplate);
        Log.d("yyyyyy", "onStart: "+studentTemplate.getStudentTemplateName());
    }

    @Override
    public void communicateAdd(Bundle bundle) {

    }

    @Override
    public void communicateUpdate(Bundle bundle) {

    }
}

