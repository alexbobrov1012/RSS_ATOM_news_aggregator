package com.example.rss_atom_news_aggregator;

import android.util.Log;
import android.util.Xml;

import com.example.rss_atom_news_aggregator.Room.News;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NewsParser {
    private static final String TAG = "ParserRSS";

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            Log.d(TAG, "in parse");
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        List newsItems = new ArrayList();
        Log.d(TAG, "in readFeed");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().
                    equals("item")) {
                newsItems.add(readNews(parser));
            }
        }
        return newsItems;
    }

    private News readNews(XmlPullParser parser) throws IOException, XmlPullParserException {
        String title = null;
        String date = null;
        String contents = null;
        String link = null;
        Log.d(TAG, "in readNews");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readText(parser, name);
            } else if(name.equals("pubDate")) {
                date = readText(parser, name);
            } else if(name.equals("description")) {
                contents = readText(parser, name);
            } else if(name.equals("link")) {
                link = readText(parser, name);
            }
            else {
                skip(parser);
            }
        }
        return new News(date,title,contents,link);
    }

    private String readText(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, name);
        Log.d(TAG, "in readText");
        String result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, name);
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.d(TAG, "in skip");
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            Log.d(TAG, "in skip.." + parser.getName());
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
// https://news.yandex.ru/movies.rss