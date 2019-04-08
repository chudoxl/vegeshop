package com.github.chudoxl.vegeshop.tools.db;

import java.math.BigDecimal;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "orders")
public class DbOrder {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "num")
    private long num;

    @TypeConverters({BigDecimalConverter.class})
    @ColumnInfo(name = "sum")
    private BigDecimal sum;

    public DbOrder(long num, BigDecimal sum) {
        this.num = num;
        this.sum = sum;
    }

    public long getNum() {
        return num;
    }

    public BigDecimal getSum() {
        return sum;
    }
}
