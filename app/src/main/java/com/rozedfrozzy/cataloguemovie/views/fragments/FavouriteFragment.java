package com.rozedfrozzy.cataloguemovie.views.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rozedfrozzy.cataloguemovie.R;
import com.rozedfrozzy.cataloguemovie.db.FavouriteHelper;
import com.rozedfrozzy.cataloguemovie.views.adapter.FavouriteListAdapter;

import static com.rozedfrozzy.cataloguemovie.db.DatabaseContract.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {

    private RecyclerView itemList;
    private FavouriteListAdapter adapter;

    private Cursor list;
    private FavouriteHelper favouriteHelper;

    private Context context;


    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ConstraintLayout rvFavourite = (ConstraintLayout) inflater.inflate(R.layout.fragment_favourite, container, false);
        context = rvFavourite.getContext();
        itemList = rvFavourite.findViewById(R.id.favourite_recycler);

        favouriteHelper = new FavouriteHelper(context);
        favouriteHelper.open();

        configureRecyclerView();

        new LoadFavouriteAsync().execute();

        return rvFavourite;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadFavouriteAsync().execute();
    }

    private void configureRecyclerView() {
        adapter = new FavouriteListAdapter(context, list);
        itemList.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemList.setAdapter(adapter);
    }

    private class LoadFavouriteAsync extends AsyncTask<Void, Void, Cursor>{

        @Override
        protected Cursor doInBackground(Void... voids) {
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            list = cursor;
            adapter.updateList(list);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (favouriteHelper != null){
            favouriteHelper.close();
        }
    }
}
