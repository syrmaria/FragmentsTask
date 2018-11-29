package com.syrovama.fragmentstask;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import java.util.Random;

/*
Creates random number and sends it in broadcast
*/

public class RandomService extends JobIntentService {
    private static final String TAG = "RandomService";
    private static final int JOB_ID = 1000;
    private Random random = new Random();
    public static final String ACTION = "com.syrovama.fragmentstask.numberAction";
    public static final String EXTRA_NUMBER = "NUMBER";

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, RandomService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "Handle work");
        int newNumber = random.nextInt();
        Intent broadcastIntent = new Intent(ACTION);
        broadcastIntent.putExtra(EXTRA_NUMBER, newNumber);
        sendBroadcast(broadcastIntent);
    }

    public static Intent newIntent(Context c) {
        return new Intent(c, RandomService.class);
    }
}
