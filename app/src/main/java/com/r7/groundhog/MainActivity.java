package com.r7.groundhog;

import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class MainActivity extends AppCompatActivity {
    public static final int RequestPermissionCode = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isGrantedPermissions()) {
            Log.d("GroundHog", "Requesting permissions");
            requestPermissions(new String[]{
                    CALL_PHONE, READ_PHONE_STATE
            }, RequestPermissionCode);
        } else {
            Log.d("GroundHog", "Permission granted");
            performService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode: {
                if (grantResults.length > 0
                        && grantResults[0] == PERMISSION_GRANTED
                        && grantResults[1] == PERMISSION_GRANTED) {
                    performService();
                } else {
                    Log.d("GroundHog", "Permission denied");
                }
                return;
            }
        }
    }

    boolean isGrantedPermissions() {
        Log.d("GroundHog", "Check permissions grant");
        return (
                checkSelfPermission(CALL_PHONE) == PERMISSION_GRANTED &&
                        checkSelfPermission(READ_PHONE_STATE) == PERMISSION_GRANTED
        );
    }

    void performService() {
        SubscriptionManager sm = getSystemService(SubscriptionManager.class);
        TelephonyManager tm = getSystemService(TelephonyManager.class);
        Subscription s = new Subscription(sm);
        TelephonyRunner tr = new TelephonyRunner(s, tm);
        tr.performUssdRequest("*556#");
    }
}
