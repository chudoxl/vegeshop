package com.github.chudoxl.vegeshop.tools.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DbWare.class, DbBasketItem.class, DbOrder.class}, version = 1)
public abstract class TheDb extends RoomDatabase {
    public abstract IWaresDao wareDao();
    public abstract IBasketDao basketDao();
    public abstract IOrdersDao ordersDao();
}
