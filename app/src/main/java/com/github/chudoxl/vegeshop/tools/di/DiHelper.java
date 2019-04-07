package com.github.chudoxl.vegeshop.tools.di;

import android.app.Activity;

import com.github.chudoxl.vegeshop.tools.di.components.IAppComponent;

import androidx.fragment.app.Fragment;

public final class DiHelper {
    private DiHelper() { }

    public static IAppComponent injector(Activity activity) {
        return ((IDaggerComponentProvider)activity.getApplication()).getAppComponent();
    }

    public static IAppComponent injector(Fragment fragment) {
        return ((IDaggerComponentProvider)fragment.getActivity().getApplication()).getAppComponent();
    }
}
