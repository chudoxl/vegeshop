package com.github.chudoxl.vegeshop.tools.db;

import androidx.room.Embedded;
import androidx.room.Relation;

public class BasketItem {
    @Embedded
    private DbBasketItem bi;
    
    @Relation(parentColumn = "ware_id", entityColumn = "id")
    private DbWare ware;

    public DbBasketItem getBi() {
        return bi;
    }

    public void setBi(DbBasketItem bi) {
        this.bi = bi;
    }

    public DbWare getWare() {
        return ware;
    }

    public void setWare(DbWare ware) {
        this.ware = ware;
    }
}
