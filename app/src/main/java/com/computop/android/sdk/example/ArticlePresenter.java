package com.computop.android.sdk.example;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.kibotu.android.recyclerviewpresenter.BaseViewHolder;
import net.kibotu.android.recyclerviewpresenter.Presenter;
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter;

import butterknife.BindView;

/**
 * Created by paul.sprotte on 11.11.16.
 */

public class ArticlePresenter extends Presenter<Article, ArticlePresenter.ViewHolder> {

    public ArticlePresenter(@NonNull PresenterAdapter<Article> presenterAdapter) {
        super(presenterAdapter);
    }



    @Override
    protected int getLayout() {
        return R.layout.cell_article;
    }

    @NonNull
    @Override
    protected ArticlePresenter.ViewHolder createViewHolder(@LayoutRes int layout, @NonNull ViewGroup viewGroup) {
        return new ArticlePresenter.ViewHolder(layout, viewGroup);
    }

    @Override
    public void bindViewHolder(@NonNull final ViewHolder viewHolder, @NonNull Article item, int position) {

        viewHolder.name.setText(item.name);
        viewHolder.color.setText(item.color);
        viewHolder.price.setText("Price: " + item.price + "$");
        viewHolder.image.setImageResource(item.image);

        viewHolder.buttonAdd.setOnClickListener(v -> {
            if (presenterAdapter.getOnItemClickListener() != null)
                presenterAdapter.getOnItemClickListener().onItemClick(item, viewHolder.buttonAdd, position);
        });
    }

    static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.color)
        TextView color;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.image)
        ImageView image;

        @BindView(R.id.add)
        Button buttonAdd;

        public ViewHolder(@LayoutRes int layout, @NonNull ViewGroup parent) {
            super(layout, parent);


        }
    }
}
