package com.example.android.newsapp;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by kenny on 6/17/2017.
 */

public class NetworkUtils {

    public static final String TAG ="My Tag";

    public static final String BASE_URL = "https://newsapi.org/v1/articles";

    public static final String PARAM_SOURCE = "source";
    public static final String PARAM_SORT ="sortBy";
    public static final String PARAM_KEY = "apiKey";
    public static final String API_KEY = "e12e72b020064032a687304c8073ca69";


    public static URL buildUrl(String source, String sortBy) {
        URL url = null;

        if (sortBy == null)
            sortBy= "";

        Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(PARAM_SOURCE, source)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .appendQueryParameter(PARAM_KEY, API_KEY).build();
        try {
            String urlString = uri.toString();
            Log.d(TAG, "Url: " + urlString);
            url = new URL(urlString);
        }catch(MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner input = new Scanner(in);
            input.useDelimiter("\\A");

            boolean hasInput = input.hasNext();
            if (hasInput) {
                return input.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}