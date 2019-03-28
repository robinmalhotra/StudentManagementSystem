package com.example.studentmanagementsystem.backgrounddbhandle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;

import static com.example.studentmanagementsystem.util.Constants.FILTER_ACTION_KEY;


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
        }

        String operationOnStudent = intent.getStringExtra("operation");

        StudentTemplate studentForDb = intent.getParcelableExtra("studentForDb");


        switch (operationOnStudent) {
            case Constants.ADD_IT:

                databaseHelper.addStudentinDb(studentForDb);

                break;
            case Constants.UPDATE_IT:

                databaseHelper.updateStudentInDb(studentForDb,oldIdofStudent);
                break;
            case Constants.DELETE_IT:

                databaseHelper.deleteStudentInDb(studentForDb);
                break;
        }

        intent.setAction(FILTER_ACTION_KEY);
        String echoMessage = "Broadcast Receiver" ;
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent.putExtra("broadcastMessage", echoMessage));


        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
