package com.example.rss_atom_news_aggregator.Interface;

public class NewsListModel {
    private String title;
    private String date;
    private String contents;

    public NewsListModel(String title, String date, String contents) {
        this.title = title;
        this.date = date;
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getContents() {
        return contents;
    }
}
