package com.github.chudoxl.vegeshop.tools.di.modules;

import android.content.Context;

import com.github.chudoxl.vegeshop.tools.db.TheDb;
import com.github.chudoxl.vegeshop.tools.db.TheDbFiller;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context appContext;

    public AppModule(Context appContext){
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context providesAppContext(){
        return appContext;
    }

    @Provides
    @Singleton
    TheDb providesTheDb(){
        return Room.databaseBuilder(appContext.getApplicationContext(), TheDb.class, "vegeshop.db")
                .addCallback(new TheDbFiller())
                .build();
    }
}
