package com.github.chudoxl.vegeshop.tools.di.components;

import com.github.chudoxl.vegeshop.tools.di.modules.AppModule;
import com.github.chudoxl.vegeshop.wares.mvp.WareListPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface IAppComponent {
    void inject(WareListPresenter p);
}
