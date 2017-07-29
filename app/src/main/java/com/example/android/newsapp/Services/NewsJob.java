package com.example.android.newsapp.Services;

/**
 * Created by 2rite on 7/28/2017.
 */

import android.os.AsyncTask;
import android.widget.Toast;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.example.android.newsapp.Utils.RefreshTasks;




public class NewsJob extends JobService
{
    AsyncTask myBackgroundTask;
    @Override
    public boolean onStartJob(final JobParameters job)
    {
        myBackgroundTask = new AsyncTask()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params)
            {
                RefreshTasks.refreshArticles(NewsJob.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o)
            {
                jobFinished(job, false);
                super.onPostExecute(o);
                Toast.makeText(getApplicationContext(), "Articles added", Toast.LENGTH_LONG).show();
            }
        };

        myBackgroundTask.execute();
        return true;
    }
    @Override
    public boolean onStopJob(JobParameters job)
    {
        if (myBackgroundTask != null) myBackgroundTask.cancel(false);
        return true;
    }
}
