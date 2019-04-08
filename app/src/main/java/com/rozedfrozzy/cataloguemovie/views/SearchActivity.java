package com.rozedfrozzy.cataloguemovie.views;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.rozedfrozzy.cataloguemovie.R;
import com.rozedfrozzy.cataloguemovie.model.ResultMovieItems;
import com.rozedfrozzy.cataloguemovie.views.adapter.SearchMovieListAdapter;
import com.rozedfrozzy.cataloguemovie.loader.SearchMovieDataLoader;
import com.rozedfrozzy.cataloguemovie.views.settings.SettingActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<ResultMovieItems>>{
    private RecyclerView itemList;
    private ProgressBar progressBar;
    private SearchMovieListAdapter adapter;

    static final String EXTRA_TITLE = "extra_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureRecyclerList();

        progressBar = findViewById(R.id.search_progress_bar);

        String title = getIntent().getStringExtra("title");
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TITLE, title);

        getSupportLoaderManager().initLoader(0, bundle, SearchActivity.this);
        setTitle(title);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String title = query;

                if (TextUtils.isEmpty(title)) return false;

                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_TITLE, title);
                getSupportLoaderManager().restartLoader(0, bundle, SearchActivity.this);

                searchView.clearFocus();
                setTitle(title);
                progressBar.setVisibility(View.VISIBLE);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void configureRecyclerList() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        itemList = findViewById(R.id.item_list);
        itemList.setLayoutManager(mLayoutManager);
        itemList.setItemAnimator(new DefaultItemAnimator());
        itemList.setHasFixedSize(true);
        itemList.setItemViewCacheSize(50);
        itemList.setDrawingCacheEnabled(true);
        itemList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        itemList.addItemDecoration(new DividerItemDecoration(itemList.getContext(), DividerItemDecoration.VERTICAL));

    }

    @Override
    public Loader<ArrayList<ResultMovieItems>> onCreateLoader(int id, Bundle args) {
        String movieTitle = "";
        if (args != null){
            movieTitle = args.getString(EXTRA_TITLE);
        }
        return new SearchMovieDataLoader(this, movieTitle);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ResultMovieItems>> loader, ArrayList<ResultMovieItems> data) {
        adapter = new SearchMovieListAdapter(data, SearchActivity.this);
        itemList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ResultMovieItems>> loader) {
        adapter.clearData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);

                return true;
            default:
                return true;
        }
    }
}
