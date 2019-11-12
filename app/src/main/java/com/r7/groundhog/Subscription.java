package com.r7.groundhog;

import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import java.util.List;


class Subscription implements ISubscription {
    public static final String SELECTED_SUBSCRIPTION_NAME = "mtn-ng";
    private SubscriptionManager sm;

    Subscription(SubscriptionManager sm) {
        this.sm = sm;
    }

    public List<SubscriptionInfo> activeSubscriptionInfo() {
        Log.d("GroundHog", "Get active subscription info list");
        return sm.getActiveSubscriptionInfoList();
    }

    public int identity() {
        Log.d("GroundHog", "Get subscription identity");

        for (SubscriptionInfo smInfo : this.activeSubscriptionInfo()) {
            Log.d("GroundHog", String.valueOf(smInfo.getDisplayName()));
            if (String.valueOf(smInfo.getDisplayName())
                    .toLowerCase()
                    .equals(SELECTED_SUBSCRIPTION_NAME)) {
                return smInfo.getSubscriptionId();
            }
        }
        return -1;
    }
}
