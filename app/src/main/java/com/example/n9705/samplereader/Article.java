package com.example.n9705.samplereader;

import java.io.Serializable;

public class Article implements Serializable {

    private String Title;
    private String Time;
    private String Author;
    private String Text;

    Article(String Title, String Author, String Text){
        super();
        this.Title=Title;
        this.Author=Author;
        this.Text=Text;
    }
    
    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String Author) {
        this.Author = Author;
    }

    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
    }
}
