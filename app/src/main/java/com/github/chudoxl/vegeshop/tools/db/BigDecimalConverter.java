package com.github.chudoxl.vegeshop.tools.db;

import java.math.BigDecimal;

import androidx.room.TypeConverter;

final class BigDecimalConverter {

    @TypeConverter
    public BigDecimal fromString(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    @TypeConverter
    public String toString(BigDecimal value) {
        return value == null? null : value.toPlainString();
    }
}
