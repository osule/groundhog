package com.r7.groundhog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler h = new Handler();
        TelephonyManager.UssdResponseCallback cb = new TelephonyManager.UssdResponseCallback() {
            @Override
            public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                Log.d("GroundHog", String.valueOf(response));
                super.onReceiveUssdResponse(telephonyManager, request, response);
            }

            @Override
            public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                Log.e("GroundHog", String.valueOf(failureCode));
                super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
            }
        };

        Log.d("GroundHog", "Dialing USSD number");

        TelephonyManager dt = getTelephonyManager(getSubscriptionId());
        if (dt != null) {
            if (checkSelfPermission("android.permission.CALL_PHONE") == PERMISSION_GRANTED) {
                dt.sendUssdRequest("*556#", cb, h);
            } else {
                Log.d("GroundHog", "No permission to CALL_PHONE");
            }
        } else {
            Log.d("Groundhog", "No telephony manager for selected subscription");
        }
    }

    int getSubscriptionId() {
        SubscriptionManager sm = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        if (checkSelfPermission("android.permission.READ_PHONE_STATE") == PERMISSION_GRANTED) {
            for (SubscriptionInfo smInfo : sm.getActiveSubscriptionInfoList()) {
                if (String.valueOf(smInfo.getDisplayName())
                        .toLowerCase()
                        .equals(Subscription.SELECTED_SUBSCRIPTION_NAME)) {
                    return smInfo.getSubscriptionId();
                }
            }
        } else {
            Log.d("GroundHog", "No permission to READ_PHONE_STATE");
        }

        return -1;
    }

    TelephonyManager getTelephonyManager(int subscriptionId) {
        if (subscriptionId == -1) {
            return null;
        }

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.createForSubscriptionId(subscriptionId);
    }
}
