package com.example.studentmanagementsystem.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundAsyncTasks;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundIntentService;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundService;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;

/** Generates Dialog box when we press Delete option in the Dialog already on the screen.
 * Choice 1: via Service
 * Choice 2: via Intent Service
 * Choice 3: via AsyncTasks
 * param@ studentToHandle: Student model object
 * param@ operationOnStudent: To save the delete Operation in the Background db Handlers.
 */

public class GenerateDialog {

    Context mContext;


    public GenerateDialog(Context mContext) {
        this.mContext = mContext;
    }


    public void generateDialogOnTouch(final StudentTemplate studentToHandle, final String operationOnStudent) {

        final String[] items = {Constants.SERVICE,
                Constants.INTENTSERVICE,
                Constants.ASYNC};
        final int useService = 0, useIntentService = 1, useAsyncTasks = 2;

        //Alert Dialog that has context of this activity.
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.userchoice_dbhandle_alert);
        //Sets the items of the Dialog.
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (which) {
                    case useService:

                        Intent forService = new Intent(mContext,
                                BackgroundService.class);
                        forService.putExtra(Constants.STUDENT_FOR_DB, studentToHandle);
                        forService.putExtra(Constants.OPERATION,operationOnStudent);
                        mContext.startService(forService);
                        break;


                    case useIntentService:

                        Intent forIntentService = new Intent(mContext,
                                BackgroundIntentService.class);
                        forIntentService.putExtra(Constants.STUDENT_FOR_DB, studentToHandle);
                        forIntentService.putExtra(Constants.OPERATION,operationOnStudent);
                        mContext.startService(forIntentService);

                        break;

                    case useAsyncTasks:
                        BackgroundAsyncTasks backgroundAsyncTasks = new BackgroundAsyncTasks(mContext);
                        backgroundAsyncTasks.execute(studentToHandle,operationOnStudent,null);

                        break;
                    default:
                        break;
                }


            }
        });
        AlertDialog mAlert = builder.create();
        mAlert.show();

    }



}
