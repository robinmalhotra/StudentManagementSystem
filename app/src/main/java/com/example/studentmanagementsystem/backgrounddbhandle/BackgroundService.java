package com.example.studentmanagementsystem.backgrounddbhandle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;

import static com.example.studentmanagementsystem.util.Constants.FILTER_ACTION_KEY;

/**
 *
 */
public class BackgroundService extends Service {

    public BackgroundService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        StudentHelperDatabase databaseHelper = new StudentHelperDatabase(this);
        databaseHelper.getWritableDatabase();
        String oldIdofStudent = new String();

        if(intent.hasExtra(Constants.OLD_ID_OF_STUDENT)) {

            oldIdofStudent = intent.getStringExtra(Constants.OLD_ID_OF_STUDENT);
        }

        String operationOnStudent = intent.getStringExtra(Constants.OPERATION);

        StudentTemplate studentForDb = intent.getParcelableExtra(Constants.STUDENT_FOR_DB);


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
        String echoMessage = Constants.BROADCAST ;
        LocalBroadcastManager.getInstance(getApplicationContext()).
                sendBroadcast(intent.putExtra(Constants.BROADCAST_MESSAGE, echoMessage));

        stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
