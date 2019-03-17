package com.example.studentmanagementsystem.backgrounddbhandle;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;


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


    }
}
