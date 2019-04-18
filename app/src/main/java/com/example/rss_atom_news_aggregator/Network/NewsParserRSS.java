package com.example.rss_atom_news_aggregator.Network;

import android.util.Log;
import android.util.Xml;

import com.example.rss_atom_news_aggregator.Room.News;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NewsParserRSS extends NewsParser{
    public NewsParserRSS() {
        newsTag = "item";
        newsTagTitle = "title";
        newsTagsDate.add("pubDate");
        newsTagsContents.add("description");
        newsTagLink = "link";
    }
}
// https://news.yandex.ru/movies.rss