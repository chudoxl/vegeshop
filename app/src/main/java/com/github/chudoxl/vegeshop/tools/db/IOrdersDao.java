package com.github.chudoxl.vegeshop.tools.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
public interface IOrdersDao {
    @Query("SELECT COUNT(*) FROM orders")
    Single<Integer> getCount();

    @Insert
    Single<Long> insert(DbOrder order);
}
