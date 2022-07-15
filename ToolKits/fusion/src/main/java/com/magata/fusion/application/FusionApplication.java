package com.magata.fusion.application;

import android.app.Application;
import android.content.Context;
import com.magata.fusion.ApplicationLifeCycleUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FusionApplication extends Application {
    private ApplicationLifeCycleUtil lifeCycleUtil = new ApplicationLifeCycleUtil(this);

    private Class mutiClass = null;

    @Override
    public void onCreate() {
        super.onCreate();
        lifeCycleUtil.handleOnCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        try {
            mutiClass = Class.forName("android.support.multidex.MultiDex");
            Method installMethod = mutiClass.getMethod("install", Context.class);
            installMethod.invoke(null, this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        lifeCycleUtil.handleAttachBaseContext(base);
    }
}
