package com.vendoza.model;

public class Review {
    private String author;
    private String text;
    private int stars;
    private String time;

    public Review(String author, String text, int stars, String time) {
        this.author = author;
        this.text   = text;
        this.stars  = stars;
        this.time   = time;
    }

    public String getAuthor() { return author; }
    public String getText()   { return text; }
    public int getStars()     { return stars; }
    public String getTime()   { return time; }

    public String getStarString() {
        return "★".repeat(stars) + "☆".repeat(5 - stars);
    }
}