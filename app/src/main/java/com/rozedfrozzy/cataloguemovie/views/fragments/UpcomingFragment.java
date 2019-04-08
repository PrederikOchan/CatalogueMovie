package com.rozedfrozzy.cataloguemovie.views.fragments;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.rozedfrozzy.cataloguemovie.R;
import com.rozedfrozzy.cataloguemovie.model.ResultMovieItems;
import com.rozedfrozzy.cataloguemovie.views.adapter.UpcomingListAdapter;
import com.rozedfrozzy.cataloguemovie.loader.UpcomingDataLoader;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<ResultMovieItems>>{
    private RecyclerView itemList;
    private ProgressBar progressBar;
    private UpcomingListAdapter adapter;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ConstraintLayout rvUpcoming = (ConstraintLayout) inflater.inflate(R.layout.fragment_upcoming, container, false);
        configureRecyclerView(rvUpcoming);
        progressBar = rvUpcoming.findViewById(R.id.upcoming_progress_bar);

        getLoaderManager().initLoader(0, null, UpcomingFragment.this);

        return rvUpcoming;
    }

    private void configureRecyclerView(ConstraintLayout rvUpcoming) {
        itemList = rvUpcoming.findViewById(R.id.upcoming_recycler);
        itemList.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public Loader<ArrayList<ResultMovieItems>> onCreateLoader(int id, Bundle args) {
        return new UpcomingDataLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ResultMovieItems>> loader, ArrayList<ResultMovieItems> data) {
        adapter = new UpcomingListAdapter(data, getContext());
        itemList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ResultMovieItems>> loader) {
        adapter.clearData();
        progressBar.setVisibility(View.VISIBLE);
    }
}
