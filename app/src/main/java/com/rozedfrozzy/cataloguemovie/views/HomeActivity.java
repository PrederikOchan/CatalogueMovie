package com.rozedfrozzy.cataloguemovie.views;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.rozedfrozzy.cataloguemovie.R;
import com.rozedfrozzy.cataloguemovie.views.fragments.FavouriteFragment;
import com.rozedfrozzy.cataloguemovie.views.fragments.NowPlayingFragment;
import com.rozedfrozzy.cataloguemovie.views.fragments.UpcomingFragment;
import com.rozedfrozzy.cataloguemovie.views.settings.SettingActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    CircleImageView profileCircleImageView;
    String profileImageUrl = "https://vignette.wikia.nocookie.net/bandori/images/0/01/Stamp_021001.png/revision/latest/scale-to-width-down/185?cb=20170707063011";

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager pager;
    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.pager);
        pager.setAdapter(sectionPagerAdapter);
        pager.addOnPageChangeListener(listener);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        profileCircleImageView = navigationView.getHeaderView(0).findViewById(R.id.img_profile);
        Glide.with(HomeActivity.this)
                .load(profileImageUrl)
                .into(profileCircleImageView);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.nav_now_playing:
                pager.setCurrentItem(0);
                break;

            case R.id.nav_upcoming:
                pager.setCurrentItem(1);
                break;
            case R.id.nav_favourite:
                pager.setCurrentItem(2);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return new NowPlayingFragment();
                case 1:
                    return new UpcomingFragment();
                case 2:
                    return new FavouriteFragment();

            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position){

            switch (position){
                case 0:
                    return getResources().getText(R.string.now_playing_tab);
                case 1:
                    return getResources().getText(R.string.upcoming_tab);
                case 2:
                    return getResources().getText(R.string.favourite_tab);
            }

            return null;
        }

    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0){
                navigationView.getMenu().getItem(0).setChecked(true);
            } else if (position == 1){
                navigationView.getMenu().getItem(1).setChecked(true);
            } else if (position == 2){
                navigationView.getMenu().getItem(2).setChecked(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
                searchIntent.putExtra("title", query);
                startActivity(searchIntent);

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);

                return true;
            default:
                return true;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawer.removeDrawerListener(toggle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
