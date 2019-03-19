package com.example.studentmanagementsystem.backgrounddbhandle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Vibrator;

import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;

public class BackgroundAsyncTasks extends AsyncTask<Object,Void,Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private SQLiteDatabase db;

    public BackgroundAsyncTasks(Context context) {
        this.context=context;
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }


    @Override
    protected Void doInBackground(Object... objects) {

        StudentTemplate studentForDb = (StudentTemplate) objects[0];
        String operationOnStudent = (String) objects[1];
        String oldIdofStudent = (String) objects[2];

        StudentHelperDatabase dbHelper=new StudentHelperDatabase(context);
        switch (operationOnStudent){
            case "addIt":

                db=dbHelper.getWritableDatabase();
                dbHelper.addStudentinDb(studentForDb);
                db.close();
                break;
            case "updateIt":

                db=dbHelper.getWritableDatabase();
                dbHelper.updateStudentInDb(studentForDb,oldIdofStudent);
                db.close();
                break;
            case "deleteIt":
                db=dbHelper.getWritableDatabase();
                dbHelper.deleteStudentInDb(studentForDb);
                db.close();

            default:
                break;
        }
        return null;
    }
}

