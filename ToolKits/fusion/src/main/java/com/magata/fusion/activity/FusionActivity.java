package com.magata.fusion.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.magata.fusion.ActivityLifeCycleUtil;

public class FusionActivity extends Activity {
    private ActivityLifeCycleUtil lifeCycleUtil = new ActivityLifeCycleUtil(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp =getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }

        lifeCycleUtil = new ActivityLifeCycleUtil(this);
        if (null != lifeCycleUtil)
            lifeCycleUtil.handleOnCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != lifeCycleUtil)
            lifeCycleUtil.handleOnDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != lifeCycleUtil)
            lifeCycleUtil.handleOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != lifeCycleUtil)
            lifeCycleUtil.handleOnResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != lifeCycleUtil)
            lifeCycleUtil.handleOnStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (null != lifeCycleUtil)
            lifeCycleUtil.handleOnRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != lifeCycleUtil)
            lifeCycleUtil.handleOnStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != lifeCycleUtil)
            lifeCycleUtil.handleOnNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != lifeCycleUtil)
            lifeCycleUtil.handleOnActivityResult(requestCode, resultCode, data);
    }
}
