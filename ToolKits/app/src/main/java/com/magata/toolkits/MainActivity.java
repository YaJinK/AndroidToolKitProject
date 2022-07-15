package com.magata.toolkits;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.magata.net.Http;
import com.magata.net.handler.INetResultHandler;
import com.magata.net.loading.LoadingManager;
import com.magata.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        Http.get("http://www.baidu.com", new INetResultHandler() {
//            @Override
//            public void onSuccess(String data) {
//                Log.d("MagataHttp", data);
//            }
//
//            @Override
//            public void onFailure(final String info) {
//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject json = new JSONObject(info);
//                            Toast.makeText(MainActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });

        String data = "{\"sid\":\"sst1mobi0a816d7c31de4a769dc8489123bd67c8174084\",\"gameId\":\"60\",\"form\":\"60\"}";
        LoadingManager.invokedActivity = this;
        Http.post("http://test.account.91fifa.com:81/sign/ucGetUserInfo", data, new INetResultHandler() {
            @Override
            public void onSuccess(String data) {
                Log.d("MagataHttp", data);
            }

            @Override
            public void onFailure(final String info) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(info);
                            Toast.makeText(MainActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, true);
    }
}