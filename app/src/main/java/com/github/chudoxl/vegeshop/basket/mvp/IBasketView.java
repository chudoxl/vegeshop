package com.github.chudoxl.vegeshop.basket.mvp;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.github.chudoxl.vegeshop.basket.BasketItemVI;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface IBasketView extends MvpView {
    void showLoading(boolean loading);
    void showBasketItems(List<BasketItemVI> biList);
    void showOrderNum(String num);
    void showOrderSum(String sum);
    void showOrderEnabled(boolean locked);
}