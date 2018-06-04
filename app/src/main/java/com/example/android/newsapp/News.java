package com.example.android.newsapp;

class News {
    private String place;
    private String date;
    private String trail;
    private String url;
    private String author;
    private String section;

    News(String section, String place, String date, String trail, String url, String author) {
        this.section = section;
        this.place = place;
        this.date = date;
        this.trail = trail;
        this.url = url;
        this.author = author;
    }

    public String getPlace() {
        return place;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getTrail() {
        return trail;
    }

    public String getAuthor() {
        return author;
    }

    public String getSection() {
        return section;
    }
}
