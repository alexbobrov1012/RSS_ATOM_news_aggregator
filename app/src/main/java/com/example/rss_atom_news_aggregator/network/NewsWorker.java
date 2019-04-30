package com.example.rss_atom_news_aggregator.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.rss_atom_news_aggregator.NewsApplication;

public class NewsWorker extends Worker {
    private static String TAG = "Worker";

    public NewsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public void onStopped() {
        Log.d(TAG, "Stopped..."+ this.getId().toString());
        super.onStopped();
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork..." + this.getId().toString());
        NewsApplication.appInstance.getRepository().serviceRoutine();
        return Result.success();
    }
}
