package com.github.chudoxl.vegeshop.calc.mvp;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ICalcView extends MvpView {
    void showWareName(String wareName);
    void showAmount(String amount);
    void showCalculation(String calc);
    void lock(boolean locked);
    void hide();
}
