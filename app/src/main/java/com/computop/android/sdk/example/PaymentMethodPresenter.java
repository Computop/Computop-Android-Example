package com.computop.android.sdk.example;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.computop.android.sdk.PaymentMethod;

import net.kibotu.android.recyclerviewpresenter.BaseViewHolder;
import net.kibotu.android.recyclerviewpresenter.Presenter;
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter;

import butterknife.BindView;

/**
 * Created by armando.shkurti on 27/03/17.
 */

class PaymentMethodPresenter extends Presenter<PaymentMethod, PaymentMethodPresenter.ViewHolder> {

    public PaymentMethodPresenter(@NonNull PresenterAdapter<PaymentMethod> presenterAdapter) {
        super(presenterAdapter);
    }


    @Override
    protected int getLayout() {
        return R.layout.cell_payment;
    }

    @NonNull
    @Override
    protected PaymentMethodPresenter.ViewHolder createViewHolder(@LayoutRes int layout, @NonNull ViewGroup viewGroup) {
        return new PaymentMethodPresenter.ViewHolder(layout, viewGroup);
    }

    @Override
    public void bindViewHolder(@NonNull final ViewHolder viewHolder, @NonNull PaymentMethod item, int position) {

        viewHolder.paymentName.setText(item.getLocalizedDescription());
        viewHolder.paymentImage.setImageResource(item.getImage());

        viewHolder.itemView.setOnClickListener(v -> {
            if (presenterAdapter.getOnItemClickListener() != null)
                presenterAdapter.getOnItemClickListener().onItemClick(item, viewHolder.paymentName, position);
        });
    }

    static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.paymentName)
        TextView paymentName;

        @BindView(R.id.paymentImage)
        ImageView paymentImage;

        public ViewHolder(@LayoutRes int layout, @NonNull ViewGroup parent) {
            super(layout, parent);


        }
    }
}
