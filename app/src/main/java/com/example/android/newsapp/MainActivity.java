/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.newsapp;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.example.android.newsapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * URL for news data from the Guardian dataset
     */
    private final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?q=%22SpaceX%22&show-fields=headline,trailText,byline&api-key=" + ApiKeys.KEY;
    private NewsAdapter adapter;
    private ActivityMainBinding binding;
    private boolean internetConnectionAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//      Check if there is an Internet connection. Try fetching news only if there is a connection.
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
            internetConnectionAvailable = (activeNetwork != null) && activeNetwork.isConnectedOrConnecting();
        }

        if (internetConnectionAvailable) {
            binding.list.setEmptyView(binding.emptyView);
            adapter = new NewsAdapter(this, new ArrayList<News>());
            binding.list.setAdapter(adapter);

            getLoaderManager().initLoader(1, null, this);

//        Open link in browser when list item is pressed
            binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    News news = (News) parent.getItemAtPosition(position);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
                    startActivity(browserIntent);
                }
            });
        } else {
            binding.loadingSpinner.setVisibility(View.GONE);
            binding.emptyView.setText(getString(R.string.problem_internet_connection));
        }
    }

    private void updateUi(List<News> articles) {
//        Hide ProgressBar
        binding.loadingSpinner.setVisibility(View.GONE);
//        Clean previous list from the adapter.
        adapter.clear();
        // Populate adapter with new data.
        if (articles != null && !articles.isEmpty()) {
            adapter.addAll(articles);
        } else if (internetConnectionAvailable) {
            binding.emptyView.setText(getString(R.string.problem_internet_connection));
        } else {
            binding.emptyView.setText(getString(R.string.no_news_loaded));
        }
    }

    @Override
    public void onLoadFinished
            (@NonNull Loader<List<News>> loader, List<News> articles) {
//      Show fetched data on the screen.
        updateUi(articles);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        updateUi(new ArrayList<News>());
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }
}
