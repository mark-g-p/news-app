package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String[] urls;
    NewsLoader(Context context, String... urls) {
        super(context);
        this.urls = urls;
    }

    @Override
    public List<News> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (urls.length < 1 || urls[0] == null) {
            return null;
        }
        // Perform the HTTP request for news data and process the response.
        return QueryUtils.extractArticles(urls[0]);
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
