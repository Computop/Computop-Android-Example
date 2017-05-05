package com.computop.android.sdk.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final String TAG = MainActivity.class.getSimpleName();
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        showLandingPage();
    }

    private void showLandingPage() {
        toolbar.setTitle(getString(R.string.app_name));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LandingPage(), LandingPage.class.getCanonicalName())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
