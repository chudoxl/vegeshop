package com.github.chudoxl.vegeshop.wares;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chudoxl.vegeshop.R;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class WaresFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wares, container, false);
        TabLayout tabsWareType = v.findViewById(R.id.tabsWareType);
        ViewPager vpWareTypes = v.findViewById(R.id.vpWareTypes);

        SimplePageAdapter pageAdapter = new SimplePageAdapter(getChildFragmentManager());
        pageAdapter.addFragment(WareListFragment.newInstance(null), "Все");
        pageAdapter.addFragment(WareListFragment.newInstance(Country.BLR), "Беларусь");
        pageAdapter.addFragment(WareListFragment.newInstance(Country.RUS), "Россия");

        vpWareTypes.setAdapter(pageAdapter);
        tabsWareType.setupWithViewPager(vpWareTypes);

        return v;
    }
}
