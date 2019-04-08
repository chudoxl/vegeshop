package com.github.chudoxl.vegeshop.calc;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.github.chudoxl.vegeshop.R;
import com.github.chudoxl.vegeshop.calc.mvp.CalcPresenter;
import com.github.chudoxl.vegeshop.calc.mvp.ICalcView;
import com.github.chudoxl.vegeshop.tools.di.DiHelper;
import com.github.chudoxl.vegeshop.tools.helpers.IClickListener;
import com.github.chudoxl.vegeshop.tools.moxy.MvpAppCompatDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalculationFragment extends MvpAppCompatDialogFragment implements ICalcView {

    private final static String PARAM_WARE_ID = "PARAM_WARE_ID";
    private RecyclerView rvButtons;
    private TextView tvWareName;
    private TextView tvCalculation;
    private TextView tvAmount;
    private MaterialButton btnAdd;
    private MaterialButton btnCancel;

    public static CalculationFragment newInstance(long wareId) {
        Bundle args = new Bundle();
        args.putLong(PARAM_WARE_ID, wareId);
        CalculationFragment fragment = new CalculationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter
    CalcPresenter presenter;

    @ProvidePresenter
    CalcPresenter providesPresenter() {
        long wareId = -1L;
        Bundle args = getArguments();
        if (args != null) {
            wareId = args.getLong(PARAM_WARE_ID);
        }
        CalcPresenter p = new CalcPresenter(wareId);
        DiHelper.injector(this).inject(p);
        return p;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calc, container, false);
        tvWareName = v.findViewById(R.id.tvWareName);
        tvCalculation = v.findViewById(R.id.tvCalculation);
        tvAmount = v.findViewById(R.id.tvAmount);
        btnAdd = v.findViewById(R.id.btnAdd);
        btnCancel = v.findViewById(R.id.btnCancel);
        rvButtons = v.findViewById(R.id.rvButtons);

        Context context = getContext();
        Objects.requireNonNull(context);
        Drawable drwDivider = ContextCompat.getDrawable(context, R.drawable.rv_calc_buttons_divider);
        Objects.requireNonNull(drwDivider);
        DividerItemDecoration hDivider = new DividerItemDecoration(context, GridLayoutManager.HORIZONTAL);
        hDivider.setDrawable(drwDivider);
        DividerItemDecoration vDivider = new DividerItemDecoration(context, GridLayoutManager.VERTICAL);
        vDivider.setDrawable(drwDivider);

        rvButtons.addItemDecoration(hDivider);
        rvButtons.addItemDecoration(vDivider);
        rvButtons.setHasFixedSize(true);
        rvButtons.setAdapter(new RvAdapter(btn -> {
            if (btn.equals("X")){
                presenter.onClearClick();
                return;
            }
            presenter.onPress(btn);
        }));

        btnAdd.setOnClickListener(v1 -> presenter.onAddClick());
        btnCancel.setOnClickListener(v1 -> presenter.onCancelClick());

        return v;
    }

    @Override
    public void showWareName(String wareName) {
        tvWareName.setText(wareName);
    }

    @Override
    public void showAmount(String amount) {
        tvAmount.setText(amount);
    }

    @Override
    public void showCalculation(String calc) {
        tvCalculation.setText(calc);
    }

    @Override
    public void lock(boolean locked) {
        rvButtons.setEnabled(!locked);
        btnAdd.setEnabled(!locked);
        btnCancel.setEnabled(!locked);
    }

    @Override
    public void hide() {
        dismiss();
    }

    private static class RvAdapter extends RecyclerView.Adapter<RvAdapter.VH>{

        private String[] items = {"1", "2", "3", "4", "5", "6", "7", "8", "9", ",", "0", "X"};
        private final IClickListener<String> clickListener;

        RvAdapter(IClickListener<String> clickListener){
            this.clickListener = clickListener;
        }


        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvi_calc_btn, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.bind(items[position]);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        private class VH extends RecyclerView.ViewHolder{
            private final MaterialButton mb;

            VH(@NonNull View v) {
                super(v);
                mb = (MaterialButton) v;
            }

            void bind(final String s) {
                mb.setText(s);
                mb.setOnClickListener(v -> clickListener.onClick(s));
            }
        }
    }
}
