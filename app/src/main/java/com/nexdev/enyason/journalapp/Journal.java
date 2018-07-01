package com.nexdev.enyason.journalapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by enyason on 6/27/18.
 */
@Entity(tableName = "Journal")
public class Journal implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private int isFavourite;
    private String title;

    private String description;

    private  String image;

    @ColumnInfo(name = "date")
    private Date date;


    public Journal(String title, String description,String image,int isFavourite, Date date) {

        this.title = title;
        this.description = description;
        this.date = date;

        this.image = image;
        this.isFavourite = isFavourite;
    }


    public int isFavourite() {
        return isFavourite;
    }

    public void setFavourite(int favourite) {
        isFavourite = favourite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
