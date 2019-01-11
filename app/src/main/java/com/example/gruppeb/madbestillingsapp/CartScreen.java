package com.example.gruppeb.madbestillingsapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gruppeb.madbestillingsapp.Domain.IDAO;
import com.example.gruppeb.madbestillingsapp.Domain.CartDAO;
import com.example.gruppeb.madbestillingsapp.Domain.Order;

import java.util.ArrayList;
import java.util.Map;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;


public class CartScreen extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbarCart;
    //ImageView mMainImage;
    ListView mListView;
    Button mOrderCart;
    TextView mDeleteAll;
    ArrayList<Map<String, String>> orderMap;
    Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOrder = new Order();
        if (mOrder.getCount(this) == 0) {
            setEmptyView();
        } else {
            setContentView(R.layout.activity_cart_screen);
            setTitle(getString(R.string.action_cart));

            findViewAndClickListener();

            populateItemList();

            SharedPreferences sp = getSharedPreferences("first_time_guide_cart", MODE_PRIVATE);
            if (!sp.getBoolean("first", false)) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("first", true);
                editor.apply();
                playGuide_cart();
            }

        }

    }

    private void playGuide_cart() {
        new MaterialTapTargetPrompt.Builder(CartScreen.this)
                //.setIcon(R.drawable.ic_shopping_cart_white_24dp)
                .setTarget(R.id.button_order)
                .setPrimaryText(getString(R.string.intro_guide_last_step_title))
                .setSecondaryText(getString(R.string.intro_guide_last_step_desc))
                .setBackgroundColour(getResources().getColor(R.color.colorPrimary))
                .setPromptBackground(new RectanglePromptBackground())
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                            // User has pressed the prompt target
                        }
                    }
                })
                .show();
    }

    //@Override
    public void onClick(View v) {
        if (v == mDeleteAll) {
            mOrder.clearOrder(this);
            setEmptyView();
        } else if (v == mOrderCart) {
            IDAO dao = new CartDAO(mOrder.getOrder(this), mOrder.getBreadType(this));
            dao.executeAction();
            mOrder.clearOrder(this);
            orderComplete();
        }
    }


    //https://developer.android.com/training/appbar/actions#java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateItemList() {
        orderMap = mOrder.getMap(this);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, orderMap, R.layout.order_list, new String[]{"title", "breadtype"}, new int[]{R.id.cart_list_title, R.id.cart_list_breadtype});

        mListView.setAdapter(simpleAdapter);
    }


    private void findViewAndClickListener() {
        //mMainImage = findViewById(R.id.cart_mainimage);
        //mMainImage.setOnClickListener(this);
        mListView = findViewById(R.id.cart_list);
        mDeleteAll = findViewById(R.id.cart_delete_all);
        mDeleteAll.setOnClickListener(this);
        mOrderCart = findViewById(R.id.button_order);
        mOrderCart.setOnClickListener(this);

        createToolBar();
    }

    private void setEmptyView() {
        setContentView(R.layout.activity_cart_screen_empty);
        com.airbnb.lottie.LottieAnimationView animation = findViewById(R.id.cart_empty_animation);
        animation.setOnClickListener(v -> Toast.makeText(this, "Du har ikke bestilt noget", Toast.LENGTH_SHORT).show());
        createToolBar();
    }

    private void orderComplete() {
        setContentView(R.layout.activity_cart_screen_empty);
        com.airbnb.lottie.LottieAnimationView animation = findViewById(R.id.cart_empty_animation);
        animation.loop(false);
        animation.setAnimation(R.raw.success);
        final Handler handler = new Handler();
        handler.postDelayed(() -> setEmptyView(), 1300);
    }

    private void createToolBar() {
        mToolbarCart = findViewById(R.id.my_CartToolbar);
        setSupportActionBar(mToolbarCart);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

}
