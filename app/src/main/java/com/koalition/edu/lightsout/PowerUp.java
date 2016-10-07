package com.koalition.edu.lightsout;

/**
 * Created by Kingston on 3/15/2016.
 */
public class PowerUp {
    public static final String TABLE_NAME = "powerup";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_ICONTITLE = "icon";
    public static final String COLUMN_DESCRIPTION = "description";

    private int id;
    private String title;
    private int price;
    private int category;
    private String iconTite;
    private String description;

    public PowerUp() {
    }

    public PowerUp(int id, String title, int price, int category, String iconTite, String description) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.category = category;
        this.iconTite = iconTite;
        this.description = description;
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getIconTite() {
        return iconTite;
    }

    public void setIconTite(String iconTite) {
        this.iconTite = iconTite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
