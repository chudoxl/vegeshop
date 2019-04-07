package com.github.chudoxl.vegeshop.wares;

import androidx.annotation.IntDef;

@IntDef({Country.BLR, Country.RUS})
public @interface Country {
    int BLR = 1;
    int RUS = 2;
}
