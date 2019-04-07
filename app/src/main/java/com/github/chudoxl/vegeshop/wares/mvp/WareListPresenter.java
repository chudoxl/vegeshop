package com.github.chudoxl.vegeshop.wares.mvp;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.github.chudoxl.vegeshop.tools.db.DbWare;
import com.github.chudoxl.vegeshop.tools.db.TheDb;
import com.github.chudoxl.vegeshop.tools.moxy.BasePresenter;
import com.github.chudoxl.vegeshop.wares.Country;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class WareListPresenter extends BasePresenter<IWareListView> {

    @Inject
    TheDb theDb;

    private final Integer countryCode;

    public WareListPresenter(@Country Integer countryCode){
        this.countryCode = countryCode;
    }

    @Override
    protected void onFirstViewAttach() {
        getViewState().showWaresLoading(true);
        Disposable d = getWares().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<DbWare>>() {
                    @Override
                    public void onSuccess(List<DbWare> wares) {
                        getViewState().showWaresLoading(false);
                        getViewState().showWares(wares);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(WareListPresenter.class.getSimpleName(), "Wares loading error", e);
                        getViewState().showWaresLoading(false);
                    }
                });
        disposeOnDestroy(d);
    }

    private Single<List<DbWare>> getWares(){
        if (countryCode == null)
            return theDb.wareDao().getAll();
        return theDb.wareDao().getAllByCountry(countryCode);
    }
}
