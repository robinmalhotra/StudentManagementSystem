package com.example.studentmanagementsystem.backgrounddbhandle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;

import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;

public class BackgroundAsyncTasks extends AsyncTask<Object,Void,Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private SQLiteDatabase db;
    String oldIdofStudent;

    public BackgroundAsyncTasks(Context context) {
        this.context=context;
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }


    @Override
    protected Void doInBackground(Object... objects) {

        StudentTemplate studentForDb = (StudentTemplate) objects[0];
        String operationOnStudent = (String) objects[1];

        if(objects[2]!=null){
            oldIdofStudent = (String) objects[2];
        }


        StudentHelperDatabase dbHelper=new StudentHelperDatabase(context);
        switch (operationOnStudent){
            case Constants.ADD_IT:

                db=dbHelper.getWritableDatabase();
                dbHelper.addStudentinDb(studentForDb);
                db.close();
                break;
            case Constants.UPDATE_IT:

                db=dbHelper.getWritableDatabase();
                dbHelper.updateStudentInDb(studentForDb,oldIdofStudent);
                db.close();
                break;
            case Constants.DELETE_IT:
                db=dbHelper.getWritableDatabase();
                dbHelper.deleteStudentInDb(studentForDb);
                db.close();

            default:
                break;
        }
        return null;
    }
}

