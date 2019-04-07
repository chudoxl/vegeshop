package com.github.chudoxl.vegeshop.wares;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.github.chudoxl.vegeshop.R;
import com.github.chudoxl.vegeshop.tools.db.DbWare;
import com.github.chudoxl.vegeshop.tools.di.DiHelper;
import com.github.chudoxl.vegeshop.tools.helpers.IClickListener;
import com.github.chudoxl.vegeshop.tools.moxy.MvpAppCompatFragment;
import com.github.chudoxl.vegeshop.wares.mvp.IWareListView;
import com.github.chudoxl.vegeshop.wares.mvp.WareListPresenter;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WareListFragment extends MvpAppCompatFragment implements IWareListView {

    private final static String PARAM_COUNTRY_CODE = "PARAM_COUNTRY_CODE";

    public static WareListFragment newInstance(@Country Integer countryCode) {
        WareListFragment fragment = new WareListFragment();
        if (countryCode != null) {
            Bundle args = new Bundle();
            args.putInt(PARAM_COUNTRY_CODE, countryCode);
            fragment.setArguments(args);
        }
        return fragment;
    }


    @InjectPresenter
    WareListPresenter presenter;

    @ProvidePresenter
    WareListPresenter providesPresenter() {
        Integer country = null;
        Bundle args = getArguments();
        if (args != null) {
            country = args.getInt(PARAM_COUNTRY_CODE);
        }
        WareListPresenter p = new WareListPresenter(country);
        DiHelper.injector(this).inject(p);
        return p;
    }


    private RecyclerView rvWares;
    private ProgressBar pgsLoading;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ware_list, container, false);

        Context context = container.getContext();
        Drawable drwDivider = ContextCompat.getDrawable(context, R.drawable.rv_wares_divider);
        Objects.requireNonNull(drwDivider);
        DividerItemDecoration hDivider = new DividerItemDecoration(context, GridLayoutManager.HORIZONTAL);
        hDivider.setDrawable(drwDivider);
        DividerItemDecoration vDivider = new DividerItemDecoration(context, GridLayoutManager.VERTICAL);
        vDivider.setDrawable(drwDivider);

        rvWares = v.findViewById(R.id.rvWares);
        rvWares.addItemDecoration(hDivider);
        rvWares.addItemDecoration(vDivider);
        rvWares.setHasFixedSize(true);
        rvWares.setAdapter(new WaresRvAdapter(item -> {

        }));


        pgsLoading = v.findViewById(R.id.pgsLoading);
        return v;
    }

    //IWareListView
    @Override
    public void showWaresLoading(boolean loading) {
        pgsLoading.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
        rvWares.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void showWares(List<DbWare> wares) {
        WaresRvAdapter rvAdapter = (WaresRvAdapter)rvWares.getAdapter();
        if (rvAdapter == null)
            return;
        rvAdapter.update(wares);
    }

    private static class WaresRvAdapter extends RecyclerView.Adapter<WaresRvAdapter.WareViewHolder>{

        private List<DbWare> wares = Collections.emptyList();
        private final IClickListener<DbWare> clickListener;

        WaresRvAdapter(IClickListener<DbWare> clickListener){
            this.clickListener = clickListener;
        }

        void update(List<DbWare> newWares){
            DiffUtil.DiffResult dr = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return wares.size();
                }

                @Override
                public int getNewListSize() {
                    return newWares.size();
                }

                @Override
                public boolean areItemsTheSame(int oldPos, int newPos) {
                    return wares.get(oldPos).getId() == newWares.get(newPos).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldPos, int newPos) {
                    return areItemsTheSame(oldPos, newPos);
                }
            });
            wares = newWares;
            dr.dispatchUpdatesTo(this);
        }

        @NonNull
        @Override
        public WareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvi_ware, parent, false);
            return new WareViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull WareViewHolder holder, int position) {
            holder.bind(wares.get(position));
        }

        @Override
        public int getItemCount() {
            return wares.size();
        }

        private class WareViewHolder extends RecyclerView.ViewHolder{
            private final ImageView ivWareImage;
            private final TextView tvWareName;
            private final View vBg;

            WareViewHolder(@NonNull View v) {
                super(v);
                ivWareImage = v.findViewById(R.id.ivWareImage);
                tvWareName = v.findViewById(R.id.tvWareName);
                vBg = v.findViewById(R.id.vBg);
            }

            void bind(final DbWare ware) {
                itemView.setOnClickListener(v -> clickListener.onClick(ware));
                tvWareName.setText(ware.getName());
                Picasso.get()
                        .load(ware.getImageUrl())
                        .into(ivWareImage);
                vBg.setBackgroundResource(ware.getCountryCode() == Country.BLR
                        ? R.drawable.bg_ware_blr
                        : R.drawable.bg_ware_rus);
            }
        }
    }
}
