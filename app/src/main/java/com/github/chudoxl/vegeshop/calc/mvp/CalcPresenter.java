package com.github.chudoxl.vegeshop.calc.mvp;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.github.chudoxl.vegeshop.R;
import com.github.chudoxl.vegeshop.tools.db.DbBasketItem;
import com.github.chudoxl.vegeshop.tools.db.DbWare;
import com.github.chudoxl.vegeshop.tools.db.TheDb;
import com.github.chudoxl.vegeshop.tools.eventBus.BasketItemAddedEvent;
import com.github.chudoxl.vegeshop.tools.moxy.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class CalcPresenter extends BasePresenter<ICalcView> {

    private final static String TAG = CalcPresenter.class.getSimpleName();

    @Inject
    Context context;
    @Inject
    EventBus eventBus;
    @Inject
    TheDb theDb;

    private final DecimalFormat dfParser;
    private final long wareId;
    private DbWare ware = null;
    private BigDecimal amount = BigDecimal.ZERO;
    private String amountString = "";

    public CalcPresenter(long wareId) {
        this.wareId = wareId;

        DecimalFormatSymbols dfSymbols = new DecimalFormatSymbols(Locale.getDefault());
        dfSymbols.setDecimalSeparator(',');
        dfParser = (DecimalFormat) DecimalFormat.getInstance();
        dfParser.setParseBigDecimal(true);
        dfParser.setDecimalFormatSymbols(dfSymbols);
    }

    @Override
    protected void onFirstViewAttach() {
        showValues();
        Disposable d = theDb.wareDao().getById(wareId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<DbWare>() {
                    @Override
                    public void onSuccess(DbWare dbWare) {
                        ware = dbWare;
                        getViewState().showWareName(dbWare.getName());
                        showValues();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error loading ware", e);
                    }
                });
        disposeOnDestroy(d);
    }

    public void onPress(String btn) {
        String newAmount = amountString + btn;

        if (newAmount.length() > 11)
            return;

        String[] splits = newAmount.split(",");
        if (splits.length > 2)
            return;

        if (splits.length == 2 && splits[1].length() > 2) { //decimals
            return;
        }

        try {
            amount = (BigDecimal) dfParser.parse(newAmount);
            amountString = newAmount;
        } catch (ParseException e) {
            amount = BigDecimal.ZERO;
            amountString = "";
        }

        showValues();
    }

    public void onClearClick() {
        amount = BigDecimal.ZERO;
        amountString = "";
        showValues();
    }

    public void onAddClick() {
        if (ware == null || amount.compareTo(BigDecimal.ZERO) == 0)
            return;

        getViewState().lock(true);

        DbBasketItem bi = new DbBasketItem(0L, wareId, amount);
        Disposable d = theDb.basketDao().insert(bi)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Long>() {
                    @Override
                    public void onSuccess(Long id) {
                        eventBus.post(new BasketItemAddedEvent());
                        getViewState().hide();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error adding ware to basket", e);
                        getViewState().lock(false);
                    }
                });
        disposeOnDestroy(d);
    }

    public void onCancelClick() {
        getViewState().hide();
    }

    private void showValues(){
        BigDecimal price = ware == null ? BigDecimal.ZERO : ware.getPrice();
        BigDecimal sum = price.multiply(amount).setScale(price.scale(), RoundingMode.FLOOR);

        getViewState().showAmount(amountString);
        getViewState().showCalculation(context.getString(R.string.calc_template, sum.toPlainString(), amount.toPlainString()));
    }
}
