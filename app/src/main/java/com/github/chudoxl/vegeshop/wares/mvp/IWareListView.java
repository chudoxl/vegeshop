package com.github.chudoxl.vegeshop.wares.mvp;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.github.chudoxl.vegeshop.tools.db.DbWare;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface IWareListView extends MvpView {
    void showWaresLoading(boolean loading);
    void showWares(List<DbWare> wares);
}
