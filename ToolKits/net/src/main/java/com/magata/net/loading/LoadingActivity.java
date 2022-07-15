package com.magata.net.loading;

import android.app.Activity;
import android.os.Bundle;

import com.magata.utility.Utility;

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutResId = Utility.getRContent(this.getPackageName(), "layout", "activity_loading");
        setContentView(layoutResId);
        LoadingManager.loading = this;
    }

    public void onBackPressed() {
    }
}