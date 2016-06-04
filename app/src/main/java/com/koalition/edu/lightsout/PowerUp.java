package com.koalition.edu.lightsout;

/**
 * Created by Kingston on 3/15/2016.
 */
public class PowerUp {
    public static final String TABLE_NAME = "powerup";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PRICE = "price";

    private int id;
    private String title;
    private int price;

    public PowerUp() {
    }

    public PowerUp(int id, String title, int price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
