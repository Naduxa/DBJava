package com.company.Entities;

/**
 * Created by User on 21.11.2015.
 */
public class Product {
    private int id;
    private String title;
    public Product(int id, String title){
        this.id = id;
        this.title = title;
    }

    public int getID(){
        return id;
    }

    public String getTitle(){
        return title;
    }
}
