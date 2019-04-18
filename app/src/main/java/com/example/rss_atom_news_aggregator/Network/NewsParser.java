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

public abstract class NewsParser {
    protected static final String LOG_TAG = "Parser";

    protected String newsTag;

    protected String newsTagTitle;

    protected List<String> newsTagsDate;

    protected List<String> newsTagsContents;

    protected String newsTagLink;

    public NewsParser() {
        newsTagsContents = new ArrayList<>();
        newsTagsDate = new ArrayList<>();
    }

    public final List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            Log.d(LOG_TAG, "in parse");
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        List newsItems = new ArrayList();
        Log.d(LOG_TAG, "in readFeed");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().
                    equals(newsTag)) {
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
        Log.d(LOG_TAG, "in readNews");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (compareTags(name, newsTagTitle)) {
                title = readTitle(parser);
            } else if(compareTags(name, newsTagsDate)) {
                date = readDate(parser);
            } else if(compareTags(name, newsTagsContents)) {
                contents = readContents(parser);
            } else if(compareTags(name, newsTagLink)) {
                String nul = readLink(parser);
                if (nul != null) {
                    link = nul;
                }
            }
            else {
                skip(parser);
            }
        }
        title = replaceHTMLEscapes(title);
        contents = replaceHTMLEscapes(contents);
        Log.d(LOG_TAG, "heh");
        return new News(date,title,contents,link);
    }

    private boolean compareTags(String name, String tag) {
        return name.equals(tag);
    }

    private boolean compareTags(String name, List<String> tags) {
        for (String tok : tags) {
            if (name.equals(tok)) {
                return true;
            }
        }
        return false;
    }

    private String replaceHTMLEscapes(String string) {
        string = string.replaceAll("&amp;", "&");
        string = string.replaceAll("&lt;", "<");
        string = string.replaceAll("&gt;", ">");
        string = string.replaceAll("&quot;", "\"");
        string = string.replaceAll("<br>", "\n");
        string = string.replaceAll("</br>", "");
        string = string.replaceAll("<p>", "\n    ");
        string = string.replaceAll("</p>", "");
        return string;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.d(LOG_TAG, "in skip");
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            Log.d(LOG_TAG, "in skip.." + parser.getName());
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

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.d(LOG_TAG, "in readText");
        String result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
        }
        parser.nextTag();
        return result;
    }

    public String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readText(parser);
    }

    public String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readText(parser);
    }

    public String readContents(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readText(parser);
    }

    public String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readText(parser);
    }

}
