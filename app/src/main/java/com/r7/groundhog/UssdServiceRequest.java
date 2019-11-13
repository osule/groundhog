package com.r7.groundhog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;


public class UssdServiceRequest extends ListenableWorker {
    private static final String LOG_TAG = UssdServiceRequest.class.getSimpleName();
    private Subscription s;
    private TelephonyManager tm;

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public UssdServiceRequest(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        SubscriptionManager sm = appContext.getSystemService(SubscriptionManager.class);
        s = new Subscription(sm);
        tm = appContext.getSystemService(TelephonyManager.class);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        ResolvableFuture<Result> f = ResolvableFuture.create();
        dialUssd("*556#", new ResponseCallback(f));
        return f;
    }

    @SuppressLint("MissingPermission")
    private void dialUssd(String ussdRequest, TelephonyManager.UssdResponseCallback cb) {
        Handler h = new Handler();
        Log.d(LOG_TAG, "Dialing USSD number " + ussdRequest);
        TelephonyManager requestTM = tm.createForSubscriptionId(s.identity());
        requestTM.sendUssdRequest(ussdRequest, cb, h);
    }

    class ResponseCallback extends TelephonyManager.UssdResponseCallback {
        ResolvableFuture<Result> f;

        ResponseCallback(ResolvableFuture<Result> f) {
            this.f = f;
        }

        public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
            Log.d(LOG_TAG, String.valueOf(response));
            f.set(ListenableWorker.Result.success());
            super.onReceiveUssdResponse(telephonyManager, request, response);
        }

        public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
            Log.e(LOG_TAG, String.valueOf(failureCode));
            f.set(ListenableWorker.Result.failure());
            super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
        }
    }
}
