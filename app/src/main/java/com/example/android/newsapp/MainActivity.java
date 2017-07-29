package com.example.android.newsapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.newsapp.models.DBHelper;
import com.example.android.newsapp.models.DatabaseUtils;
import com.example.android.newsapp.models.NewsItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NewsAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<Void> {

    Toast mToast;
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    private ProgressBar progressBar;
    final int NEWS_APP_LOADER = 22;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_news);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);
        mNewsAdapter = new NewsAdapter(cursor, this);
        mRecyclerView.setAdapter(mNewsAdapter);

        /**Gets shared preferences file, and get a boolean value.
         * if the variable does not exist, create a variable and set its default value to true
         * **/

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirst = prefs.getBoolean("first", true);

        //If the boolean is true meaning it is the first instance since installation, so start the loader
        if (isFirst) {
            getSupportLoaderManager().initLoader(NEWS_APP_LOADER, null, MainActivity.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first", false);
            editor.commit();
        }

        //Schedule a job
        Dispatcher.schedule(this);


    }


    private void showNewsDataView() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!db.isOpen()) {
            db = new DBHelper(MainActivity.this).getReadableDatabase();
            cursor = DatabaseUtils.getAll(db);
            mNewsAdapter.swapCursor(cursor);
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!db.isOpen()) {
            db = new DBHelper(MainActivity.this).getReadableDatabase();
            cursor = DatabaseUtils.getAll(db);
            mNewsAdapter.swapCursor(cursor);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
        cursor.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();
        if (itemNumber == R.id.search) {
            LoaderManager loaderManager = getSupportLoaderManager();
            // Get our Loader by calling getLoader and passing the ID we specified
            Loader<Void> Loader = loaderManager.getLoader(NEWS_APP_LOADER);
            //  If the Loader was null, initialize it. Else, restart it.
            if (Loader == null) {
                loaderManager.initLoader(NEWS_APP_LOADER, null, this);
            } else {
                loaderManager.restartLoader(NEWS_APP_LOADER, null, this).forceLoad();
            }

        }
        return true;
    }

    @Override
    public void onListItemClick(int position) {
    String data = mNewsAdapter.getNewsURL(position);
        Uri webpage = Uri.parse(data);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /*When the loader is initialized or started, return an Asynctaskloader
    that loads data in the database
    **/
    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(this) {

            @Override
            protected void onStartLoading() {
                progressBar.setVisibility(View.VISIBLE);
                //  Force a load
                forceLoad();
            }

            @Override
            public Void loadInBackground() {
               NetworkUtils.reloadDatabase(MainActivity.this);//Reload the database with new information
                return null;
            }
        };
    }
/*when the loader is finished refresh the view
* */
    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        progressBar.setVisibility(View.GONE);
            showNewsDataView();
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);//get a cursor containg the information
            mNewsAdapter.swapCursor(cursor);//Change the cursor to reflect new data

    }

    //We have to override this method or else we get complaints
    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

}
