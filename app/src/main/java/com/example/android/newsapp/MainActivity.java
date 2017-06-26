package com.example.android.newsapp;

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

import com.example.android.newsapp.models.NewsItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NewsAdapter.ListItemClickListener {

    Toast mToast;
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    private ProgressBar progressBar;

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
        mNewsAdapter = new NewsAdapter(this);
        mRecyclerView.setAdapter(mNewsAdapter);
        loadNewsData();
    }

    public void loadNewsData() {
        showNewsDataView();
        NetworkTask task = new NetworkTask();
        task.execute();
    }

    private void showNewsDataView() {
        mRecyclerView.setVisibility(View.VISIBLE);
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
            mNewsAdapter.setNewsData(null);
        loadNewsData();
        }
        return true;
    }

    @Override
    public void onListItemClick(int position) {
    ArrayList<NewsItem> data = mNewsAdapter.getNewsData();
        Uri webpage = Uri.parse(data.get(position).getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    class NetworkTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> {

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

    }

}
