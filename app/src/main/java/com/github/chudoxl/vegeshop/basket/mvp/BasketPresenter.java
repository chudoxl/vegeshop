package com.github.chudoxl.vegeshop.basket.mvp;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.github.chudoxl.vegeshop.R;
import com.github.chudoxl.vegeshop.basket.BasketItemVI;
import com.github.chudoxl.vegeshop.tools.db.BasketItem;
import com.github.chudoxl.vegeshop.tools.db.DbOrder;
import com.github.chudoxl.vegeshop.tools.db.DbWare;
import com.github.chudoxl.vegeshop.tools.db.TheDb;
import com.github.chudoxl.vegeshop.tools.eventBus.BasketItemAddedEvent;
import com.github.chudoxl.vegeshop.tools.moxy.BasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class BasketPresenter extends BasePresenter<IBasketView> {

    private final static String TAG = BasketPresenter.class.getSimpleName();

    @Inject
    Context context;
    @Inject
    EventBus eventBus;
    @Inject
    TheDb theDb;

//    private List<BasketItemVI> biList = Collections.emptyList();
    private BigDecimal orderSum = BigDecimal.ZERO;

    @Override
    protected void onFirstViewAttach() {
        eventBus.register(this);
        showSum();
        loadBasketItems();
        loadOrderNum();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (eventBus != null)
            eventBus.unregister(this);
    }

    private void loadBasketItems(){
        getViewState().showLoading(true);
        getViewState().showOrderEnabled(false);
        Disposable d = theDb.basketDao().getAll()
                .subscribeOn(Schedulers.io())
                .flatMap(biList -> Single.fromCallable(() -> convert(biList)).subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<BasketItemVI>>() {
                    @Override
                    public void onSuccess(List<BasketItemVI> viList) {
                        getViewState().showLoading(false);
                        getViewState().showBasketItems(viList);
                        getViewState().showOrderEnabled(viList.size() > 0);
                        showSum();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showLoading(false);
                        Log.e(TAG, "Error loading basket items", e);
                    }
                });
        disposeOnDestroy(d);
    }

    private void loadOrderNum(){
        Disposable d = theDb.ordersDao().getCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer count) {
                        getViewState().showOrderNum(context.getString(R.string.order_num_template, count+1));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error loading orders count", e);
                    }
                });
        disposeOnDestroy(d);
    }

    private List<BasketItemVI> convert(List<BasketItem> biList) {
        List<BasketItemVI> viList = new ArrayList<>();
        orderSum = BigDecimal.ZERO;
        for (BasketItem bi : biList) {
            BigDecimal wareSum = getBasketItemSum(bi);
            orderSum = orderSum.add(wareSum);
            String sum = context.getString(R.string.sum_template, wareSum);
            String price = context.getString(R.string.price_template, bi.getWare().getPrice().toPlainString());
            String calc = context.getString(R.string.calc_template_bi, bi.getBi().getAmount().toPlainString(), bi.getWare().getPrice().toPlainString());
            viList.add(new BasketItemVI(bi.getBi().getPos(), bi.getWare().getName(), sum, price, calc));
        }
        return viList;
    }

    public void onMakeOrderClick() {
        getViewState().showOrderEnabled(false);
        Disposable d = theDb.ordersDao().insert(new DbOrder(0L, orderSum))
                .subscribeOn(Schedulers.io())
                .flatMap(x -> Single.fromCallable(() -> theDb.basketDao().clear()).subscribeOn(Schedulers.io()))
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        loadOrderNum();
                        loadBasketItems();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error making order", e);
                    }
                });
        disposeOnDestroy(d);
    }

    public void onClearClick() {
        getViewState().showOrderEnabled(false);
        Disposable d = Single.fromCallable(() -> theDb.basketDao().clear())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer numDeleted) {
                        loadOrderNum();
                        loadBasketItems();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error clearing basket", e);
                    }
                });

        disposeOnDestroy(d);
    }

    @Subscribe
    public void onWareAdded(BasketItemAddedEvent event){
        loadBasketItems();
    }

    private void showSum(){
        getViewState().showOrderSum(context.getString(R.string.sum_template, orderSum.toPlainString()));
    }

    private BigDecimal getBasketItemSum(BasketItem bi){
        DbWare ware = bi.getWare();
        if (ware == null)
            return BigDecimal.ZERO;
        BigDecimal price = ware.getPrice();
        return price.multiply(bi.getBi().getAmount()).setScale(price.scale(), RoundingMode.FLOOR);
    }

}
