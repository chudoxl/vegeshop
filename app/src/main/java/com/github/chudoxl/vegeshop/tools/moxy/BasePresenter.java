package com.github.chudoxl.vegeshop.tools.moxy;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends MvpView> extends MvpPresenter<V> {

    private final CompositeDisposable disposables = new CompositeDisposable();

    protected void disposeOnDestroy(Disposable subscription) {
        disposables.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}