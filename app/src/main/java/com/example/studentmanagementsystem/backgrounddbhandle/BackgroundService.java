package com.example.studentmanagementsystem.backgrounddbhandle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;

import java.util.Objects;


public class BackgroundService extends Service {

    public BackgroundService() {
        Log.d("yyyyyy", "BackgroundService: Constructor");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("yyyyyy", "onStartCommand: started");


        StudentHelperDatabase databaseHelper = new StudentHelperDatabase(this);
        databaseHelper.getWritableDatabase();

        String operationOnStudent = intent.getStringExtra("operation");

        StudentTemplate studentForDb = intent.getParcelableExtra("studentForDb");
        Log.d("yyyyyy", "onStartCommand: of service" + studentForDb.getStudentTemplateRoll());


        if(operationOnStudent.equals("addIt"))
        {
            databaseHelper.addStudentinDb(studentForDb);

        }
        else if(operationOnStudent.equals("updateIt"))
        {
            databaseHelper.updateStudentInDb(studentForDb);
        }

        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
