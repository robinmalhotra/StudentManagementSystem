package com.example.studentmanagementsystem.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.communicator.Communicator;
import com.example.studentmanagementsystem.fragment.StudentAddFragment;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;



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
    }

    @Override
    public void communicateAdd(Bundle bundle) {

    }

    @Override
    public void communicateUpdate(Bundle bundle) {

    }

}

