package com.example.android.newsapp;

/**
 * Created by kenny on 7/20/2017.
 */

import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class RefreshJobService extends JobService  {
    AsyncTaskLoader mBackgroundTask;


    //Called when the job starts
    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTaskLoader(this) {

            @Override
            protected void onStartLoading() {
                Toast.makeText(RefreshJobService.this, "News refreshed", Toast.LENGTH_SHORT).show();
                forceLoad();//Forces a load
            }
            @Override
            public  Void loadInBackground() {
                NetworkUtils.reloadDatabase(RefreshJobService.this);
                return null;
            }
        };


        mBackgroundTask.startLoading();//Starts the asynctaskloader
        jobFinished(job, false);//Called to provide indication job has completed. Set to false to indicated it doesn't need rescheduling

        return true;
    }

    //If the job is stopped, cancel the AsyncTaskLoader
    @Override
    public boolean onStopJob(JobParameters job) {

        if (mBackgroundTask != null) mBackgroundTask.cancelLoad();

        return true;
    }
}
