package com.computop.android.sdk.example;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.android.utils.extensions.MathExtensions;
import com.common.android.utils.extensions.ViewExtensions;
import com.computop.android.sdk.Computop;
import com.computop.android.sdk.ComputopError;
import com.computop.android.sdk.InternalError;
import com.computop.android.sdk.Payment;
import com.computop.android.sdk.PaymentMethod;

import net.kibotu.android.recyclerviewpresenter.PresenterAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.dtx12.android_animations_actions.actions.Actions.play;
import static com.dtx12.android_animations_actions.actions.Actions.sizeTo;

/**
 * Created by jan.rabe on 26/09/16.
 */

public class LandingPage extends Fragment {

    public static final String TAG = LandingPage.class.getSimpleName();

    @BindView(R.id.list)
    RecyclerView recyclerView;
    private PresenterAdapter<Article> adapter;

    @BindView(R.id.basketView)
    LinearLayout basketView;

    @BindView(R.id.payOptions)
    LinearLayout payOptions;

    @BindView(R.id.basketText)
    TextView basketText;

    private Disposable disposable;

    @BindView(R.id.paymentMethodsList)
    RecyclerView paymentMethodsList;

    private ArrayList<Article> articles = new ArrayList<>(Arrays.asList(
            new Article().setName("Buildmaster 2013").setColor("Color: Black").setPrice("20").setImage(R.drawable.black),
            new Article().setName("Golden Screws 42").setColor("Color: Gold").setPrice("40").setImage(R.drawable.gold),
            new Article().setName("Silver Screws T3").setColor("Color: Silver").setPrice("10").setImage(R.drawable.silver)
    ));

    private ArrayList<Article> basket = new ArrayList<>();

    private Unbinder unbinder;
    private Computop computop;
    private PresenterAdapter<PaymentMethod> paymentMethodAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_landing_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        initComputop();

        initArticles();
        initPaymentMethods();

        ViewExtensions.setOneTimeGlobalLayoutListener(view, () -> {
            play(sizeTo(view.getWidth(), MathExtensions.dpToPx(0), 0), basketView);
        });

        payOptions.setOnClickListener(v -> {
            if (basket.size() > 0) {
                play(sizeTo(getView().getWidth(), MathExtensions.dpToPx(300), 0.5f), basketView);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (basket.size() > 0) {
            play(sizeTo(MathExtensions.dpToPx(newConfig.screenWidthDp), basketView.getHeight(), 0.1f), basketView);
        }
    }

    private void initComputop() {
        computop = Computop.with(getActivity());
    }

    private void initArticles() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new PresenterAdapter<>();
        recyclerView.setAdapter(adapter);

        for (int i = 0; i < articles.size(); i++) {
            adapter.add(articles.get(i), ArticlePresenter.class);
        }
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(this::onItemClicked);
    }

    private void initPaymentMethods() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        paymentMethodsList.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(paymentMethodsList.getContext(), LinearLayoutManager.VERTICAL);
        paymentMethodsList.addItemDecoration(dividerItemDecoration);

        paymentMethodAdapter = new PresenterAdapter<>();
        paymentMethodsList.setAdapter(paymentMethodAdapter);

        paymentMethodAdapter.setOnItemClickListener((method, method2, method3) -> payWithPaymentOption(method));

        //load payment methods
        computop.requestPaymentMethods().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((paymentMethods, throwable) -> {
                    if (throwable != null) {
                        Log.e(TAG, "PaymentMethods error: ", throwable);
                        return;
                    }

                    //bind them to recyclerView
                    for (int i = 0; i < paymentMethods.size(); i++) {
                        paymentMethodAdapter.add(paymentMethods.get(i), PaymentMethodPresenter.class);
                    }
                    paymentMethodAdapter.notifyDataSetChanged();
                });
    }

    private void onItemClicked(Article article, View view, int position) {
        basket.add(article);

        if (basket.size() > 0) {
            play(sizeTo(getView().getWidth(), MathExtensions.dpToPx(50), 0.5f), basketView);
            basketText.setText(basket.size() + " Items in the Basket, click to buy now!");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    void payWithPaymentOption(@NonNull PaymentMethod method) {

        setPayment(method.getPayment());

        //set payment method that has the right payment data, and checkout
        disposable = computop
                .withPaymentMethod(method)
                .setWebViewListener(() -> {
                    Log.i(TAG, "[WebViewClient] onPageFinishedLoading");
                })
                .checkout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o ->
                {
                    Log.v(TAG, "Payment received");
                    Log.v(TAG, o.toString());

                }, throwable -> {

                    if (throwable instanceof ComputopError) {
                        Log.e(TAG, "Computop Error " + (throwable));
                        showErrorAlert((ComputopError) throwable);
                    } else if (throwable instanceof InternalError) {
                        Log.e(TAG, "Internal Error boolean isPaymentCanceled(): " + ((InternalError) throwable).isPaymentCanceled());
                    } else {
                        Log.e(TAG, "Payment error: ", throwable);
                    }
                });
    }

    /**
     * Fill payment data with key, value
     * @param payment
     * @return
     */
    Payment setPayment(Payment payment) {
        int price = 0;

        for (Article article : basket) {
            price = price + Integer.parseInt(article.price);
        }

        payment.setParamWithKey("TransID", "****");
        payment.setParamWithKey("Amount", String.valueOf(price * 100));
        payment.setParamWithKey("Currency", "EUR");
        payment.setParamWithKey("URLSuccess", "****");
        payment.setParamWithKey("URLNotify", "****");
        payment.setParamWithKey("URLFailure", "****");
        payment.setParamWithKey("RefNr", "****");
        payment.setParamWithKey("OrderDesc", "****");
        payment.setParamWithKey("AddrCity", "****");
        payment.setParamWithKey("FirstName", "****");
        payment.setParamWithKey("LastName", "****");
        payment.setParamWithKey("AddrZip", "****");
        payment.setParamWithKey("AddrStreet", "****");
        payment.setParamWithKey("AddrState", "****");
        payment.setParamWithKey("AddrCountryCode", "****");
        payment.setParamWithKey("Phone", "****");
        payment.setParamWithKey("LandingPage", "****");
        payment.setParamWithKey("eMail", "****");
        payment.setParamWithKey("ShopID", "1");
        payment.setParamWithKey("Subject", "****");

        return payment;
    }

    public void showErrorAlert(ComputopError error) {
        String code = error.getCode();
        String severity = error.getSeverity();
        String category = error.getCategory();
        String details = error.getDetails();

        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(
                "Code: " + code + "\n\n" +
                        "Severity: " + severity + "\n\n" +
                        "Category: " + category + "\n\n" +
                        "Abbreviation: " + details + "\n\n");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog.show();
    }
}
