package com.magata.net.loading;

import android.app.Activity;
import android.content.Intent;

public class LoadingManager {
    private static int count = 0;

    public static Activity invokedActivity = null;
    public static Activity loading = null;

    public static void show() {
        if (loading == null) {
            if (invokedActivity != null) {
                Intent intent = new Intent(invokedActivity, LoadingActivity.class);
                invokedActivity.startActivity(intent);
                invokedActivity = null;
                count = 0;
            }
        } else
            count += 1;
    }

    public static void hide() {
        if (loading != null) {
            count = count - 1;
            if (count <= 0) {
                loading.finish();
                count = 0;
            }
        }
    }
}
