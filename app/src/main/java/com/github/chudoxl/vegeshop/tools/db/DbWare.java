package com.github.chudoxl.vegeshop.tools.db;

import java.math.BigDecimal;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "wares")
public class DbWare {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    @ColumnInfo(name = "image_url")
    @NonNull
    private String imageUrl;

    @ColumnInfo(name = "country_code")
    private int countryCode;

    @TypeConverters({BigDecimalConverter.class})
    @ColumnInfo(name = "price")
    @NonNull
    private BigDecimal price;


    public DbWare(long id, @NonNull String name, @NonNull String imageUrl, int countryCode, @NonNull BigDecimal price) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.countryCode = countryCode;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }

    public int getCountryCode() {
        return countryCode;
    }

    @NonNull
    public BigDecimal getPrice() {
        return price;
    }
}
