package com.magata.privacy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.magata.utility.Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrivacyActivity extends Activity {

    private String TAG = "Log Privacy";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutResId = Utility.getRContent(this.getPackageName(), "layout", "privacy_layout");
        setContentView(layoutResId);

        // 获取标题字符串
        int titleResId = Utility.getRContent(this.getPackageName(), "string", "privacy_entry_title");
        String title = getString(titleResId);
        SpannableStringBuilder titleSpannableString = new SpannableStringBuilder(title);

        // 获取内容字符串
        int contentResId = Utility.getRContent(this.getPackageName(), "string", "privacy_entry_content");
        String content = getString(contentResId);
        SpannableStringBuilder contentSpannableString = new SpannableStringBuilder(content);

        String patternStr = "\\[href=(\\d+)\\]";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String hrefBeginStr = matcher.group();
            Log.e(TAG, "onCreate: " + hrefBeginStr);

            int hrefBeginIndex = content.indexOf(hrefBeginStr);
            contentSpannableString = contentSpannableString.delete(hrefBeginIndex, hrefBeginIndex + hrefBeginStr.length());
            content = content.replaceFirst(patternStr, "");
            String suffix = "[/href]";
            String patternSuffix = "\\[/href\\]";

            int hrefEndIndex = content.indexOf(suffix);
            contentSpannableString = contentSpannableString.delete(hrefEndIndex, hrefEndIndex + suffix.length());
            content = content.replaceFirst(patternSuffix, "");
            Pattern indexPattern = Pattern.compile("\\d+");

            Matcher indexMatcher = indexPattern.matcher(hrefBeginStr);
            indexMatcher.find();
            final int contentIndex = Integer.parseInt(indexMatcher.group());
            // 用户协议超链接设置
            contentSpannableString.setSpan(new UnderlineSpan(), hrefBeginIndex, hrefEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            contentSpannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(PrivacyActivity.this, PrivacyContentActivity.class);

                    Bundle bundle=new Bundle();

                    int titleResId = Utility.getRContent(PrivacyActivity.this.getPackageName(), "string", "privacy_title_arr" + contentIndex);
                    String title = getString(titleResId);
                    bundle.putString("title", title);

                    int contentResId = Utility.getRContent(PrivacyActivity.this.getPackageName(), "string", "privacy_content_arr" + contentIndex);
                    String content = getString(contentResId);
                    bundle.putString("contentHtml", content);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }, hrefBeginIndex, hrefEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            contentSpannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#2440B3")), hrefBeginIndex, hrefEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        updatePageContent(titleSpannableString, contentSpannableString, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jumpResId = Utility.getRContent(PrivacyActivity.this.getPackageName(), "string", "privacy_agree_jump");
                String jumpActivityName = getString(jumpResId);
                try {
                    SharedPreferences sp = getSharedPreferences(PrivacyActivity.this.getPackageName(), MODE_PRIVATE);
                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean("PrivacyAgree", true);
                    e.commit();
                    Intent intent = new Intent(PrivacyActivity.this, Class.forName(jumpActivityName));
                    startActivity(intent);
                    finish();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

//        if (contentArray.length > 0)
//            showPrivacyDialog(0);
    }
//
//    public void showPrivacyDialog(final int index){
//        View.OnClickListener callback = null;
//        final PrivacyActivity mySelf = this;
//        if (index == contentArray.length - 1)
//        {
//            callback = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SharedPreferences sp = getSharedPreferences(mySelf.getPackageName(), MODE_PRIVATE);
//                    SharedPreferences.Editor e = sp.edit();
//                    e.putBoolean("PrivacyAgree", true);
//                    e.commit();
////                    Intent mainIntent = new Intent(mySelf, TimeYearsUnityActivity.class);
////                    startActivity(mainIntent);
////                    finish();
//                }
//            };
//        }else{
//            callback = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showPrivacyDialog(index + 1);
//                }
//            };
//        }
//        //updatePageContent(titleArray[index], contentArray[index], callback);
//    }

    public void updatePageContent(SpannableStringBuilder title, SpannableStringBuilder content, final View.OnClickListener confirmCallback){
        int titleResId = Utility.getRContent(this.getPackageName(), "id", "title");
        TextView titleText = findViewById(titleResId);
        titleText.setText(title);
        titleText.setMovementMethod(LinkMovementMethod.getInstance());

        int contentResId = Utility.getRContent(this.getPackageName(), "id", "content");
        TextView contentText = findViewById(contentResId);
        contentText.setText(content);
        contentText.setMovementMethod(LinkMovementMethod.getInstance());

        int cancelBtnResId = Utility.getRContent(this.getPackageName(), "id", "cancel");
        Button cancelBtn = findViewById(cancelBtnResId);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        int confirmBtnResId = Utility.getRContent(this.getPackageName(), "id", "confirm");
        Button confirmBtn = findViewById(confirmBtnResId);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCallback.onClick(v);
            }
        });
    }
}
