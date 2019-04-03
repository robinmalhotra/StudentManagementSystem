package com.example.studentmanagementsystem.broadcastreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;



/**Broadcast receiver that receives the broadcast if the services have indeed added the elements
 * in the database.
 */

public class StudentBroadcastReceiver extends BroadcastReceiver {

    private Context acitivityContext;

    public StudentBroadcastReceiver() {

        //required empty constructor.
    }

    public StudentBroadcastReceiver(Context acitivityContext) {
        this.acitivityContext = acitivityContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Activity activity = (Activity)acitivityContext;
        activity.finish();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
        Toast.makeText(acitivityContext,"Broadcast Received",Toast.LENGTH_SHORT).show();

    }

}