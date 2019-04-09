package com.example.studentmanagementsystem.backgrounddbhandle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;

import static com.example.studentmanagementsystem.util.Constants.FILTER_ACTION_KEY;

public class BackgroundAsyncTasks extends AsyncTask<Object,Void,Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private SQLiteDatabase db;
    String oldIdofStudent;


    public BackgroundAsyncTasks(Context context) {
        this.context=context;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
        Toast.makeText(context, "Async Post Execute", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected Void doInBackground(Object... objects) {

        StudentTemplate studentForDb = (StudentTemplate) objects[0];
        String operationOnStudent = (String) objects[1];

        StudentHelperDatabase dbHelper=new StudentHelperDatabase(context);

        if(objects[2]!=null){
            oldIdofStudent = (String) objects[2];
        }



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

