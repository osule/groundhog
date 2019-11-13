package com.r7.groundhog;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class RecurringJobIntentService extends IntentService {
    private static final String LOG_TAG = RecurringJobIntentService.class.getSimpleName();
    private static final long REPEAT_INTERVAL = 60; // violates MIN_REPEAT_INTERVAL

    public RecurringJobIntentService() {
        super("RecurringJobIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "Intent service started");
        scheduleRecurringUssdRequest();
    }

    void scheduleRecurringUssdRequest() {
        PeriodicWorkRequest ussdRequest = new PeriodicWorkRequest.Builder(
                UssdServiceRequest.class, REPEAT_INTERVAL, TimeUnit.SECONDS
        ).build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
                LOG_TAG, ExistingPeriodicWorkPolicy.KEEP, ussdRequest);
    }
}
