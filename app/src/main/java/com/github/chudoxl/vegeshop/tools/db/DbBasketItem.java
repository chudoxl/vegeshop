package com.github.chudoxl.vegeshop.tools.db;

import java.math.BigDecimal;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "basket",
        foreignKeys = @ForeignKey(entity = DbWare.class, parentColumns = "id", childColumns = "ware_id"),
        indices = {@Index(value = {"ware_id"})})
public class DbBasketItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pos")
    private long pos;

    @ColumnInfo(name = "ware_id")
    private long wareId;

    @TypeConverters({BigDecimalConverter.class})
    @ColumnInfo(name = "amount")
    private BigDecimal amount;

    public DbBasketItem(long pos, long wareId, BigDecimal amount) {
        this.pos = pos;
        this.wareId = wareId;
        this.amount = amount;
    }

    public long getPos() {
        return pos;
    }

    public long getWareId() {
        return wareId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
