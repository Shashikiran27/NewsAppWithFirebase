package com.example.android.newsapp;

/**
 * Created by 2rite on 7/28/2017.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.android.newsapp.Adapters.NewsRecyclerAdapter;
import com.example.android.newsapp.Utils.DBHelper;
import com.example.android.newsapp.Utils.DatabaseUtils;
import com.example.android.newsapp.Utils.RefreshTasks;
import com.example.android.newsapp.Utils.ScheduleUtilities;



public class MainActivity extends AppCompatActivity implements NewsRecyclerAdapter.NewsAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Void>{


    private RecyclerView myRecyclerView;
    private NewsRecyclerAdapter myNewsAdapter;
    private static final int NEWS_LOADER = 1;
    private Cursor cursor;
    private SQLiteDatabase db;
    private ProgressDialog myProgressDialog;
    private SharedPreferences mysharedPreferences;
    public static final String myPREFS = "Prefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_news);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setHasFixedSize(true);
        mysharedPreferences = getSharedPreferences(myPREFS, Context.MODE_PRIVATE);

        //call news API and insert results into db

        if(!mysharedPreferences.contains("HasRan")){
            load();
            SharedPreferences.Editor editor = mysharedPreferences.edit();
            editor.putBoolean("HasRan", true);
            editor.commit();
        }
        ScheduleUtilities.scheduleRefresh(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) db.close();
        if (cursor != null) cursor.close();
    }
    @Override
    protected void onStart() {
        super.onStart();
        //fetch from db
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);
        myNewsAdapter = new NewsRecyclerAdapter(this, cursor);
        myRecyclerView.setAdapter(myNewsAdapter);
    }
    //AsynctaskLoader
    @Override
    public Loader<Void> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Void>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                myProgressDialog = new ProgressDialog(MainActivity.this);
                myProgressDialog.setMessage("Loading articles...");
                myProgressDialog.setIndeterminate(false);
                myProgressDialog.show();
            }
            @Override
            public Void loadInBackground()
            {
               //delete and insert into db
                RefreshTasks.refreshArticles(MainActivity.this);
                return null;
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data)
    {
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);
        myNewsAdapter = new NewsRecyclerAdapter(this, cursor);
        myRecyclerView.setAdapter(myNewsAdapter);
        myNewsAdapter.notifyDataSetChanged();
        myProgressDialog.dismiss();
    }
    @Override
    public void onLoaderReset(Loader<Void> loader)
    {
    }

    @Override
    public void onClick(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //fetch data from db
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh)
        {
            load();
        }

        return super.onOptionsItemSelected(item);
    }

    public void load()
    {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();

    }
}

