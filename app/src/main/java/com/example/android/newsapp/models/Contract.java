package com.example.android.newsapp.models;

/**
 * Created by kenny on 7/19/2017.
 */

import android.provider.BaseColumns;

//Creates a contract fro the database to store important information about the database
public class Contract {

    public static class TABLE_NEWS implements BaseColumns{
        public static final String TABLE_NAME = "newsitems";

        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DATE = "date";

        //The category this Item belongs to
        public static final String COLUMN_NAME_AUTHOR = "author";

        //The status of this task, whether it is done or not
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_URL_TO_IMAGE = "urltoimage";
        public static final String COLUMN_NAME_TITLE = "title";

    }
}
