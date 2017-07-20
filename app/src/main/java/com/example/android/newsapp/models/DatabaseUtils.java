package com.example.android.newsapp.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.example.android.newsapp.models.Contract.TABLE_NEWS.*;

/**
 * Created by kenny on 7/20/2017.
 * Copied from Mark's Github. Cos I like the code. Sue me
 */

public class DatabaseUtils {

    //Gets all the
    public static Cursor getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME_DATE + " DESC"
        );
        return cursor;
    }

    public static void bulkInsert(SQLiteDatabase db, ArrayList<NewsItem> articles) {
        db.beginTransaction();
        try {
            for (NewsItem a : articles) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_NAME_TITLE, a.getTitle());
                cv.put(COLUMN_NAME_AUTHOR, a.getAuthor());
                cv.put(COLUMN_NAME_DESCRIPTION, a.getDescription());
                cv.put(COLUMN_NAME_DATE, a.getDate());
                cv.put(COLUMN_NAME_URL_TO_IMAGE, a.getUrlToImage());
                cv.put(COLUMN_NAME_URL, a.getUrl());
                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

}
