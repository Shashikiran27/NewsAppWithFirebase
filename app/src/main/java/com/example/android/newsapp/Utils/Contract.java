package com.example.android.newsapp.Utils;

/**
 * Created by 2rite on 7/28/2017.
 */

import android.provider.BaseColumns;



public class Contract {
    public static class TABLE_NEWS implements BaseColumns {
        public static final String TABLE_NAME = "news_items";

        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PUBLISHED_AT = "published_at";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_THUMBNAIL = "thumbnail";
    }
}

