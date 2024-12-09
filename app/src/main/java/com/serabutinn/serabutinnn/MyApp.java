package com.serabutinn.serabutinnn;

import android.app.Application;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();



        // Register ActivityLifecycleCallbacks
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                Log.d("LifecycleLogger", "Created: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                Log.d("LifecycleLogger", "Started: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                Log.d("LifecycleLogger", "Resumed: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                Log.d("LifecycleLogger", "Paused: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                Log.d("LifecycleLogger", "Stopped: " + activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                Log.d("LifecycleLogger", "SaveInstanceState: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Log.d("LifecycleLogger", "Destroyed: " + activity.getLocalClassName());
            }
        });
    }
}
