package com.example.saki99.retrofit;

/**
 * Created by Saki99 on 8.2.2018..
 */

public class Podatak {

    private String title;
    private String body;

    public Podatak() { }

    public Podatak( String title, String body ) {

        this.title = title;
        this.body = body;

    }

    public void setTitle( String title ) { this.title = title; }
    public String getTitle() { return this.title; }

    public void setBody( String body ) { this.body = body; }
    public String getBody() { return this.body; }
}
