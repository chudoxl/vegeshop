package com.github.chudoxl.vegeshop.tools.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
public interface IWaresDao {
    @Query("SELECT * FROM wares ORDER BY name ASC")
    Single<List<DbWare>> getAll();

    @Query("SELECT * FROM wares WHERE country_code = :countryCode ORDER BY name ASC")
    Single<List<DbWare>> getAllByCountry(int countryCode);

    @Query("SELECT * FROM wares WHERE id = :wareId LIMIT 1")
    Single<DbWare> getById(long wareId);
}
