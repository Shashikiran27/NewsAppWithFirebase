package com.example.android.newsapp.Utils;

/**
 * Created by 2rite on 7/28/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONException;
import com.example.android.newsapp.Model.NewsItems;
import com.example.android.newsapp.NetworkUtils;

public class RefreshTasks
{
    public static final String ACTION_REFRESH = "refresh";
    public static void refreshArticles(Context context)
    {
        ArrayList<NewsItems> results = null;
        URL url = NetworkUtils.buildUrl();
        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        try
        {
            //delete before inserting
            DatabaseUtils.deleteAll(db);
            String json = NetworkUtils.getResponseFromHttpUrl(url);
            results = NetworkUtils.parseJSON(json);
            //insert articles
            DatabaseUtils.bulkInsert(db, results);
        }
        catch (IOException e)
        {
            e.printStackTrace();

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        db.close();
    }
}
