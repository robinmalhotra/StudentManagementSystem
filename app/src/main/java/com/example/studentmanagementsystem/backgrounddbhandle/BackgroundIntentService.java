package com.example.studentmanagementsystem.backgrounddbhandle;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.studentmanagementsystem.activity.CreateStudentActivity;
import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;

import static com.example.studentmanagementsystem.util.Constants.FILTER_ACTION_KEY;


public class BackgroundIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BackgroundIntentService(String name) {
        super(name);
    }

    public BackgroundIntentService() {
        super("nothing");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        StudentHelperDatabase databaseHelper = new StudentHelperDatabase(this);
        databaseHelper.getWritableDatabase();
        String oldIdofStudent = new String();






        if(intent.hasExtra("oldIdOfStudent")) {
            oldIdofStudent = intent.getStringExtra("oldIdOfStudent");
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
        intent.setAction(FILTER_ACTION_KEY);
        String echoMessage = "Broadcast Receiver" ;
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent.putExtra("broadcastMessage", echoMessage));



    }


}
