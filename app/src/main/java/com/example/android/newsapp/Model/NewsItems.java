package com.example.android.newsapp.Model;

/**
 * Created by 2rite on 7/28/2017.
 */

public class NewsItems {
    private String title, author, description, publishedAt, url, urlToImage;

    public NewsItems(String title, String author, String description, String publishedAt, String url, String urlToImage){
        this.title = title;
        this.author = author;
        this.description = description;
        this.publishedAt = publishedAt;
        this.url = url;
        this.urlToImage = urlToImage;
    }

    public String getTitle()
    {

        return title;
    }

    public String getUrlToImage()
    {
        return urlToImage;
    }

    public String getUrl()
    {
        return url;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getDescription()
    {
        return description;
    }

    public String getPublishedAt()
    {
        return publishedAt;
    }
}

