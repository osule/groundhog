package com.r7.groundhog;

import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

public class TelephonyRunner implements ITelephonyManager {
    private TelephonyManager tm;
    private Handler h;
    private Subscription subscription;

    TelephonyRunner(Subscription subscription, TelephonyManager tm) {
        this.tm = tm;
        this.subscription = subscription;
        this.h = new Handler();
        this.tm = createForSubscription();
    }

    public TelephonyManager createForSubscription() {
        return tm.createForSubscriptionId(subscription.identity());
    }

    public void performUssdRequest(String ussdRequest) {
        TelephonyManager.UssdResponseCallback cb = new TelephonyManager.UssdResponseCallback() {
            public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                Log.d("GroundHog", String.valueOf(response));
                super.onReceiveUssdResponse(telephonyManager, request, response);
            }

            public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                Log.e("GroundHog", String.valueOf(failureCode));
                super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
            }
        };

        Log.d("GroundHog", "Dialing USSD number " + ussdRequest);

        tm.sendUssdRequest(ussdRequest, cb, h);
    }
}
