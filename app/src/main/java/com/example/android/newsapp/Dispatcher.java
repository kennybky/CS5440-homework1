package com.example.android.newsapp;

/**
 * Created by kenny on 7/20/2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class Dispatcher {

    private static boolean init;

    //synchronized to ensure concurrency
    synchronized public static void schedule(@NonNull final Context context){

        //If job is already scheduled return
        if(init) {
            return;
        }
        //Instantiates an instance of Driver
        Driver driver = new GooglePlayDriver(context);

        //Instantiates a new instance of FirebaseJobDispatcher
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        //Sets constraints for the job and builds it
        Job constraintRefreshJob = dispatcher.newJobBuilder()
                .setService(RefreshJobService.class)//Sets the service that performs the task
                .setTag("news_refresh")
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0,60))//Refresh every 60 seconds
                .setReplaceCurrent(true)
                .build();

        //Schedules the job and sets init to true to indicate the job is already scheduled
        dispatcher.schedule(constraintRefreshJob);
        init = true;

    }

}
