package com.r7.groundhog;

import android.annotation.SuppressLint;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import java.util.List;


class Subscription implements ISubscription {
    private static final String LOG_TAG = Subscription.class.getSimpleName();
    private SubscriptionManager sm;
    private static final String SELECTED_SUBSCRIPTION_NAME = "mtn-ng";

    Subscription(SubscriptionManager sm) {
        this.sm = sm;
    }

    @SuppressLint("MissingPermission")
    private List<SubscriptionInfo> activeSubscriptionInfo() {
        Log.d(LOG_TAG, "Get active subscription info list");
        return sm.getActiveSubscriptionInfoList();
    }

    public int identity() {
        Log.d(LOG_TAG, "Get subscription identity");

        for (SubscriptionInfo smInfo : this.activeSubscriptionInfo()) {
            Log.d(LOG_TAG, String.valueOf(smInfo.getDisplayName()));
            if (String.valueOf(smInfo.getDisplayName())
                    .toLowerCase()
                    .equals(SELECTED_SUBSCRIPTION_NAME)) {
                return smInfo.getSubscriptionId();
            }
        }
        return -1;
    }
}
