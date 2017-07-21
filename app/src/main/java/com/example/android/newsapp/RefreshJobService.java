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

    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTaskLoader(this) {

            @Override
            protected void onStartLoading() {
                Toast.makeText(RefreshJobService.this, "News refreshed", Toast.LENGTH_SHORT).show();
                forceLoad();
            }
            @Override
            public  Void loadInBackground() {
                NetworkUtils.reloadDatabase(RefreshJobService.this);
                return null;
            }
        };


        mBackgroundTask.startLoading();
        jobFinished(job, false);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {

        if (mBackgroundTask != null) mBackgroundTask.cancelLoad();

        return true;
    }
}
