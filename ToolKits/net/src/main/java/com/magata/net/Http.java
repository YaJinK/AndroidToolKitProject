package com.magata.net;

import android.util.Log;

import com.magata.net.handler.INetResultHandler;
import com.magata.net.loading.LoadingManager;
import com.magata.net.xxtea.XXTEA;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http {

    private static void HandleFailure(Call call, IOException e, INetResultHandler netResultHandler) {
        LoadingManager.hide();
        JSONObject json = new JSONObject();
        try {
            if (e instanceof SocketTimeoutException) {
                //判断超时异常
                json.put("message", "请求超时");
            } else {
                json.put("message", "请求异常, 请检查网络是否正常");
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        e.printStackTrace();
        netResultHandler.onFailure(json.toString());
    }

    private static void HandleSuccessNoEncry(Call call, Response response, INetResultHandler netResultHandler) {
        LoadingManager.hide();
        if(response.isSuccessful()){
            String result = null;
            try {
                result = response.body().string();
                netResultHandler.onSuccess(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void HandleSuccessEncry(Call call, Response response, INetResultHandler netResultHandler) {
        LoadingManager.hide();
        if(response.isSuccessful()){
            String result = null;
            try {
                result = response.body().string();
                netResultHandler.onSuccess(XXTEA.DecryptBase64StringToString(result, XXTEA.EncryptKey));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void get(String url, INetResultHandler netResultHandler){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false);
        OkHttpClient client = builder.build();
        Request request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);
        LoadingManager.show();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                HandleFailure(call, e, netResultHandler);
            }

            @Override
            public void onResponse(Call call, Response response) {
                HandleSuccessNoEncry(call, response, netResultHandler);
            }
        });
    }

    public static String wrapJson(String json) {
        JSONObject result = new JSONObject();
        try {
            result.put("data", new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void post(String url, String data, INetResultHandler netResultHandler, boolean encry){
        data = wrapJson(data);
        RequestBody body = null;
        byte[] array = null;
        if (encry) {
            String base64 = XXTEA.EncryptToBase64String(data, XXTEA.EncryptKey);
            Log.d("Net", base64);
            try {
                array = base64.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                array = data.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        body = RequestBody.create(array);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false);
        OkHttpClient client = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();

        Call call = client.newCall(request);
        LoadingManager.show();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                HandleFailure(call, e, netResultHandler);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (encry)
                    HandleSuccessEncry(call, response, netResultHandler);
                else
                    HandleSuccessNoEncry(call, response, netResultHandler);
            }
        });
    }
}
