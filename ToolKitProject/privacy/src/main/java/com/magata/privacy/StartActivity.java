package com.magata.privacy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.magata.utility.Utility;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) == 0) {
//            SharedPreferences sp = getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
//            boolean privacyAgree = sp.getBoolean("PrivacyAgree", false);
//            if (privacyAgree) {
//                Intent mainIntent = new Intent(this, TimeYearsUnityActivity.class);
//                startActivity(mainIntent);
//            }else {
//                Intent mainIntent = new Intent(this, PrivacyActivity.class);
//                startActivity(mainIntent);
//            }
//            finish();
//        } else {
//            finish();
//        }

        SharedPreferences sp = getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
        boolean privacyAgree = sp.getBoolean("PrivacyAgree", false);
        if (privacyAgree) {
            try {
                int jumpResId = Utility.getRContent(getPackageName(), "string", "privacy_agree_jump");
                String jumpActivityName = getString(jumpResId);
                Intent intent = new Intent(this, Class.forName(jumpActivityName));
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                finish();
            }
        }else {
            Intent mainIntent = new Intent(this, PrivacyActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}
