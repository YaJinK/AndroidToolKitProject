package com.magata.net.handler;

public interface INetResultHandler {
    void onSuccess(String data);
    void onFailure(String info);
}
