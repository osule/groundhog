package com.r7.groundhog;

import android.telephony.TelephonyManager;

public interface ITelephonyManager {
    TelephonyManager createForSubscription();

    void performUssdRequest(String ussdRequest);
}
