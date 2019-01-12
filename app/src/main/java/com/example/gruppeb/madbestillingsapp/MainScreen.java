package com.example.gruppeb.madbestillingsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.crashlytics.android.Crashlytics;
import com.example.gruppeb.madbestillingsapp.Domain.BreadType;
import com.example.gruppeb.madbestillingsapp.Domain.Order;
import com.example.gruppeb.madbestillingsapp.FoodFragments.*;

import io.fabric.sdk.android.Fabric;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

public class MainScreen extends AppCompatActivity implements View.OnClickListener, BreadType, NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    FloatingActionButton fab;
    ViewPager viewPager;
    private boolean isLight = false;
    private DrawerLayout drawer;
    private TextView badgeCount;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main_screen);

        //Order logic
        order = new Order();
        IntroGuide intro = new IntroGuide();

        //Add viewpager
        viewPager = findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Add toolbar
        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        drawer = findViewById(R.id.drawer_layout);

        //https://stackoverflow.com/a/38418531/8968120
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView roomNumberNavbar = headerView.findViewById(R.id.nav_header_roomNumber);
        roomNumberNavbar.setText(getString(R.string.drawermenu_header_roomNumber) + Order.ROOM_NUMBER);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences sp = getSharedPreferences("first_time", MODE_PRIVATE);
        if (!sp.getBoolean("first_prompt", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first_prompt", true);
            editor.apply();
            intro.playGuide(this, MainScreen.this);
        }

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

        updateView();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_myOrders:
                //TO BE handled
                Intent openMyOrdersScreenIntent = new Intent(MainScreen.this, MyOrdersScreen.class);
                startActivity(openMyOrdersScreenIntent);
                break;
            case R.id.nav_mySettings:
                //TO BE handled
                Intent openSettingsScreenIntent = new Intent(MainScreen.this, SettingsScreen.class);
                startActivity(openSettingsScreenIntent);
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

        MenuItem menuItem = menu.findItem(R.id.shopping_cart);
        View view = menuItem.getActionView();
        badgeCount = view.findViewById(R.id.cart_badge);
        badgeCount.setText(Integer.toString(0));
        updateView();
        view.setOnClickListener(v -> onOptionsItemSelected(menuItem));
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
            updateView();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView() {
        int count = order.getCount(this);
        if (badgeCount != null) {
            if (count == 0) {
                badgeCount.setVisibility(View.GONE);
            } else {
                badgeCount.setText(Integer.toString(count));
                badgeCount.setVisibility(View.VISIBLE);
            }
        }
    }

}
