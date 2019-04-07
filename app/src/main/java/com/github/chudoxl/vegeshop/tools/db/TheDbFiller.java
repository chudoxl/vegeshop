package com.github.chudoxl.vegeshop.tools.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.github.chudoxl.vegeshop.wares.Country;

import java.math.BigDecimal;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class TheDbFiller extends RoomDatabase.Callback {
    private final static String BASE_URL = "https://raw.githubusercontent.com/chudoxl/vegeshop/master/images/";

    @Override
    public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                insert(db, 1, "Картофель", "potato.jpg", Country.BLR, new BigDecimal("1.11"));
                insert(db, 2, "Лук", "onion.jpg", Country.BLR, new BigDecimal("2.22"));
                insert(db, 3, "Чеснок", "garlic.jpg", Country.BLR, new BigDecimal("3.33"));
                insert(db, 4, "Морковь", "carrot.jpg", Country.RUS, new BigDecimal("4.44"));
                insert(db, 5, "Петрушка", "parsley.jpg", Country.RUS, new BigDecimal("5.55"));
                insert(db, 6, "Свекла", "beet.jpg", Country.RUS, new BigDecimal("6.66"));
                insert(db, 7, "Укроп", "dill.jpg", Country.RUS, new BigDecimal("7.77"));
            }
        });
    }

    private void insert(@NonNull SupportSQLiteDatabase db, long id, String name, String imageUrl, @Country int countryCode, BigDecimal price){
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("image_url", BASE_URL + imageUrl);
        values.put("country_code", countryCode);
        values.put("price", price.toPlainString());

        db.insert("wares", SQLiteDatabase.CONFLICT_REPLACE, values);
    }
}
