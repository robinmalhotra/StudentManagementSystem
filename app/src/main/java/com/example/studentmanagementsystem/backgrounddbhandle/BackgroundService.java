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
 * Background class to handle the database through IntentService.
 */

public class BackgroundService extends Service {

    HandleBackground handleBackground = new HandleBackground();

    public BackgroundService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handleBackground.handleDb(intent,this);

        stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
