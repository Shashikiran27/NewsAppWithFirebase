package com.example.android.newsapp.Utils;

/**
 * Created by 2rite on 7/28/2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.android.newsapp.Services.NewsJob;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

public class ScheduleUtilities
{
    private static final int INTERVAL = 60;
    private static final String NEWS_JOB_TAG = "news_job_tag";

    private static boolean simpleInitialized;

    //Job dispatcher will execute code in NewsJob service
    synchronized public static void scheduleRefresh(@NonNull final Context context){
        if(simpleInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintRefreshJob = dispatcher.newJobBuilder()
                .setService(NewsJob.class)
                .setTag(NEWS_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)

                //execute NewsJob every minute every 2 minutes max
                .setTrigger(Trigger.executionWindow(INTERVAL, INTERVAL*2))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(constraintRefreshJob);
        simpleInitialized = true;
    }
}
