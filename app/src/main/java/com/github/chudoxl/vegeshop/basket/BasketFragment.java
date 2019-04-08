package com.github.chudoxl.vegeshop.basket;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chudoxl.vegeshop.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class BasketFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

}