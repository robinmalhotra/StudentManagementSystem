package com.example.studentmanagementsystem.backgrounddbhandle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;



public class BackgroundService extends Service {

    public BackgroundService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        StudentHelperDatabase databaseHelper = new StudentHelperDatabase(this);
        databaseHelper.getWritableDatabase();
        String oldIdofStudent = new String();

        if(intent.hasExtra("oldIdOfStudent")) {
            oldIdofStudent = intent.getStringExtra("oldIdOfStudent");
            Log.d("yyyyyy", "onStartCommand: " + oldIdofStudent);
        }

        String operationOnStudent = intent.getStringExtra("operation");

        StudentTemplate studentForDb = intent.getParcelableExtra("studentForDb");


        switch (operationOnStudent) {
            case "addIt":

                databaseHelper.addStudentinDb(studentForDb);

                break;
            case "updateIt":

                databaseHelper.updateStudentInDb(studentForDb,oldIdofStudent);
                break;
            case "deleteIt":

                databaseHelper.deleteStudentInDb(studentForDb);
                break;
        }

        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
