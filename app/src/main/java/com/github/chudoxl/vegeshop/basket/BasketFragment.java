package com.github.chudoxl.vegeshop.basket;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.github.chudoxl.vegeshop.R;
import com.github.chudoxl.vegeshop.basket.mvp.BasketPresenter;
import com.github.chudoxl.vegeshop.basket.mvp.IBasketView;
import com.github.chudoxl.vegeshop.tools.di.DiHelper;
import com.github.chudoxl.vegeshop.tools.moxy.MvpAppCompatFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class BasketFragment extends MvpAppCompatFragment implements IBasketView {

    @InjectPresenter
    BasketPresenter presenter;
    private RecyclerView rvBasketItems;
    private TextView tvTotalSum;
    private TextView tvOrderNum;
    private TextView tvEmpty;
    private MaterialButton btnMakeOrder;
    private MaterialButton btnClearBasket;

    @ProvidePresenter
    BasketPresenter providesPresenter() {
        BasketPresenter p = new BasketPresenter();
        DiHelper.injector(this).inject(p);
        return p;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        rvBasketItems = view.findViewById(R.id.rvBasketItems);
        rvBasketItems.setHasFixedSize(true);
        rvBasketItems.setAdapter(new RvAdapter());

        btnClearBasket = view.findViewById(R.id.btnClearBasket);
        btnClearBasket.setOnClickListener(v -> presenter.onClearClick());

        btnMakeOrder = view.findViewById(R.id.btnMakeOrder);
        btnMakeOrder.setOnClickListener(v -> presenter.onMakeOrderClick());

        tvTotalSum = view.findViewById(R.id.tvTotalSum);
        tvOrderNum = view.findViewById(R.id.tvOrderNum);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        return view;
    }

    @Override
    public void showLoading(boolean loading) {

    }

    @Override
    public void showBasketItems(List<BasketItemVI> viList) {
        RvAdapter adapter = (RvAdapter) rvBasketItems.getAdapter();
        if (adapter == null)
            return;
        adapter.update(viList);

        boolean empty = viList.size() < 1;
        rvBasketItems.setVisibility(empty ? View.INVISIBLE : View.VISIBLE);
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        btnClearBasket.setVisibility(empty ? View.INVISIBLE : View.VISIBLE);
        tvOrderNum.setVisibility(empty ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void showOrderNum(String num) {
        tvOrderNum.setText(num);
    }

    @Override
    public void showOrderSum(String sum) {
        tvTotalSum.setText(sum);
    }

    @Override
    public void showOrderEnabled(boolean enabled) {
        btnMakeOrder.setEnabled(enabled);
    }

    private static class RvAdapter extends RecyclerView.Adapter<RvAdapter.VH>{

        private List<BasketItemVI> items = Collections.emptyList();
        void update(List<BasketItemVI> newItems){
            DiffUtil.DiffResult dr = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return items.size();
                }

                @Override
                public int getNewListSize() {
                    return newItems.size();
                }

                @Override
                public boolean areItemsTheSame(int oldPos, int newPos) {
                    return items.get(oldPos).getId() == newItems.get(newPos).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldPos, int newPos) {
                    return areItemsTheSame(oldPos, newPos);
                }
            });
            items = newItems;
            dr.dispatchUpdatesTo(this);
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvi_basket_item, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class VH extends RecyclerView.ViewHolder{
            private final TextView tvName;
            private final TextView tvSum;
            private final TextView tvPrice;
            private final TextView tvCalculation;

            VH(@NonNull View v) {
                super(v);
                tvName = v.findViewById(R.id.tvName);
                tvSum = v.findViewById(R.id.tvSum);
                tvPrice = v.findViewById(R.id.tvPrice);
                tvCalculation = v.findViewById(R.id.tvCalculation);
            }

            void bind(BasketItemVI vi) {
                tvName.setText(vi.getWareName());
                tvSum.setText(vi.getSum());
                tvPrice.setText(vi.getPrice());
                tvCalculation.setText(vi.getCalc());
            }
        }
    }
}