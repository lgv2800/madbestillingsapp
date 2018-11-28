package com.example.gruppeb.madbestillingsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.example.gruppeb.madbestillingsapp.Domain.BreadType;
import com.example.gruppeb.madbestillingsapp.Domain.Order;
import com.example.gruppeb.madbestillingsapp.FoodFragments.*;

public class MainScreen extends AppCompatActivity implements View.OnClickListener, BreadType, NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    FloatingActionButton fab;
    ViewPager viewPager;
    private boolean isLight = false;
    private DrawerLayout drawer;

    private String roomNumberFromIntent;

    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Order logic
        order = new Order();

        //Add viewpager
        viewPager = findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Add toolbar
        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Add fragments here
        adapter.addFragment(new Page1(), getString(R.string.page1_food_title));
        adapter.addFragment(new Page2(), getString(R.string.page2_food_title));
        adapter.addFragment(new Page3(), getString(R.string.page3_food_title));
        adapter.addFragment(new Page4(), getString(R.string.page4_food_title));
        adapter.addFragment(new Page5(), getString(R.string.page5_food_title));
        viewPager.setAdapter(adapter);

        //Add tabs
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                roomNumberFromIntent = null;
            } else {
                roomNumberFromIntent = extras.getString("roomNumber");
            }
        } else {
            roomNumberFromIntent = (String) savedInstanceState.getSerializable("roomNumber");
        }

        updateView();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_myOrders:
                //TO BE handled
                Intent intent = new Intent(MainScreen.this, MyOrdersScreen.class);
                intent.putExtra("roomNumber", roomNumberFromIntent);
                startActivity(intent);
                break;
            case R.id.nav_2:
                //TO BE handled
                //getSupportFragmentManager().beginTransaction().replace(R.id.pager, new Option2Fragment()).commit();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void setBreadType(boolean a) {
        this.isLight = a;
    }

    //Code skeleton from http://www.gadgetsaint.com/android/create-viewpager-tabs-android/
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //https://developer.android.com/training/appbar/actions#java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopping_cart:
                // User chose the "Settings" item, show the app settings UI...
                Intent openCart = new Intent(this, CartScreen.class);
                openCart.putExtra("roomNumber", roomNumberFromIntent);
                startActivity(openCart);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            Snackbar.make(v, R.string.action_added, Snackbar.LENGTH_LONG)
                    .setActionTextColor(getColor(R.color.white))
                    .setAction(R.string.action_cart, v1 -> {
                        Intent openCart = new Intent(getApplicationContext(), CartScreen.class);
                        startActivity(openCart);
                    }).show();

            order.order(viewPager.getCurrentItem(), isLight, getApplication());

        }

    }

    private void updateView() {
        /**TODO
         * Opdater antal rette bestilt vha. sharedpreference
         */
        int count = order.getCount(this);
        /**
         * Opdater kurvantal - husk animation
         */

    }

}
