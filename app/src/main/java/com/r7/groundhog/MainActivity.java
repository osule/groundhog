package com.r7.groundhog;

import android.content.Intent;
import android.os.Bundle;
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
            startService(new Intent(this, RecurringJobIntentService.class));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode: {
                if (grantResults.length > 0
                        && grantResults[0] == PERMISSION_GRANTED
                        && grantResults[1] == PERMISSION_GRANTED) {
                    startService(new Intent(this, RecurringJobIntentService.class));
                } else {
                    Log.d("GroundHog", "Permission denied");
                }
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

}
