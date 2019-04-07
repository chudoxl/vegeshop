package com.github.chudoxl.vegeshop;

import android.app.Application;

import com.github.chudoxl.vegeshop.tools.di.IDaggerComponentProvider;
import com.github.chudoxl.vegeshop.tools.di.components.DaggerIAppComponent;
import com.github.chudoxl.vegeshop.tools.di.components.IAppComponent;
import com.github.chudoxl.vegeshop.tools.di.modules.AppModule;

public class TheApp extends Application implements IDaggerComponentProvider {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IAppComponent getAppComponent() {
        return DaggerIAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
