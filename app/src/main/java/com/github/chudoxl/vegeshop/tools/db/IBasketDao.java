package com.github.chudoxl.vegeshop.tools.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface IBasketDao {
    @Query("SELECT COUNT(*) FROM basket")
    Single<Integer> getCount();

    @Transaction
    @Query("SELECT * FROM basket ORDER BY pos ASC")
    Observable<List<BasketItem>> getAll();

    @Insert
    Single<Long> insert(DbBasketItem order);
}
