package com.example.android.newsapp;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.android.newsapp.databinding.ListItemBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class NewsAdapter extends ArrayAdapter<News> {
    NewsAdapter(Activity context, List<News> articles) {
        super(context, 0, articles);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        ListItemBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(getContext()),
                    R.layout.list_item, parent, false);
            convertView = binding.getRoot();
        } else {
            binding = (ListItemBinding) convertView.getTag();
        }


        // Get the object located at this position in the list
        News article = getItem(position);

        if (article != null) {
            binding.section.setText(article.getSection());
//                       Change date format
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            try {
                Date result1 = df1.parse(article.getDate());
                SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm, dd MMM yyyy", Locale.getDefault());
                binding.date.setText(dateFormatter.format(result1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            binding.title.setText(article.getPlace());
            binding.trailText.setText(article.getTrail());
            binding.author.setText(article.getAuthor());
        }

        convertView.setTag(binding);


        return convertView;
    }
}

