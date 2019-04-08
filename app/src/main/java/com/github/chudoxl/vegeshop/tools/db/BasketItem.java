package com.github.chudoxl.vegeshop.tools.db;

import java.util.Collections;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class BasketItem {
    @Embedded
    private DbBasketItem bi;
    
    @Relation(parentColumn = "ware_id", entityColumn = "id")
    private List<DbWare> wares;

    public BasketItem(){}

    public BasketItem(DbBasketItem b, DbWare w){
        this.bi = b;
        setWare(w);
    }

    public DbBasketItem getBi() {
        return bi;
    }

    public void setBi(DbBasketItem bi) {
        this.bi = bi;
    }

    public List<DbWare> getWares() {
        return wares;
    }

    public void setWares(List<DbWare> wares) {
        this.wares = wares;
    }

    public DbWare getWare() {
        return wares == null || wares.size() < 1 ? null : wares.get(0);
    }

    public void setWare(DbWare ware) {
        this.wares = Collections.singletonList(ware);
    }
}
