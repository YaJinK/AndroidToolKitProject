package com.magata.privacy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.magata.utility.Utility;

public class PrivacyContentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutResId = Utility.getRContent(this.getPackageName(), "layout", "privacy_content_dialog");
        setContentView(layoutResId);

        int backResId = Utility.getRContent(this.getPackageName(), "id", "back");

        TextView back = findViewById(backResId);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = this.getIntent().getExtras();

        int titleResId = Utility.getRContent(this.getPackageName(), "id", "title");
        TextView titleText = findViewById(titleResId);
        titleText.setText(bundle.getString("title"));

        int contentResId = Utility.getRContent(this.getPackageName(), "id", "webpage");
        WebView content = findViewById(contentResId);
        content.setBackgroundColor(Color.parseColor("#F1F1F1"));
        content.loadUrl("file:///android_asset/" + bundle.getString("contentHtml"));
    }
}
