package com.example.android.newsapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

        Bundle queryBundle = new Bundle();
        getSupportLoaderManager().initLoader(NEWS_APP_LOADER, null, MainActivity.this);
    //loadNewsData();
    }

   /* public void loadNewsData() {
        showNewsDataView();
        NetworkTask task = new NetworkTask();
        task.execute();
    }*/

    private void showNewsDataView() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);
        mNewsAdapter = new NewsAdapter(cursor, this);
        mRecyclerView.setAdapter(mNewsAdapter);
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
            mNewsAdapter.swapCursor(null);
            LoaderManager loaderManager = getSupportLoaderManager();
            // Get our Loader by calling getLoader and passing the ID we specified
            Loader<Void> Loader = loaderManager.getLoader(NEWS_APP_LOADER);
            // COMPLETED (23) If the Loader was null, initialize it. Else, restart it.
            if (Loader == null) {
                loaderManager.initLoader(NEWS_APP_LOADER, null, this);
            } else {
                loaderManager.restartLoader(NEWS_APP_LOADER, null, this).forceLoad();
            }
       // loadNewsData();
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
                ArrayList<NewsItem> result = null;
                URL  url = NetworkUtils.buildUrl("the-next-web", "latest");
                SQLiteDatabase sqldb = new DBHelper(MainActivity.this).getWritableDatabase();
                try {
                    DatabaseUtils.deleteAll(sqldb);
                    String json = NetworkUtils.getResponseFromHttpUrl(url);
                    result = NetworkUtils.parseJson(json);
                    DatabaseUtils.bulkInsert(sqldb, result);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    sqldb.close();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        progressBar.setVisibility(View.GONE);
            showNewsDataView();
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);
            mNewsAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }


    /*class NetworkTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<NewsItem> doInBackground(URL... urls) {
            ArrayList<NewsItem> result = null;
            URL  url = NetworkUtils.buildUrl("the-next-web", "latest");
            try {
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                result = NetworkUtils.parseJson(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }



        @Override
        protected void onPostExecute(ArrayList<NewsItem> items) {
            super.onPostExecute(items);
            progressBar.setVisibility(View.GONE);
            if(items == null) {
              mToast= Toast.makeText(MainActivity.this, "No items", Toast.LENGTH_LONG );
                mToast.show();
            } else {
                    showNewsDataView();
                    mNewsAdapter.setNewsData(items);
            }
        }

    }*/

}
