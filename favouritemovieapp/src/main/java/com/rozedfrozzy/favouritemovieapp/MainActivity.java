package com.rozedfrozzy.favouritemovieapp;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rozedfrozzy.favouritemovieapp.adapter.FavouriteListAdapter;

import static com.rozedfrozzy.favouritemovieapp.db.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

    private RecyclerView itemList;
    private FavouriteListAdapter adapter;

    private Cursor list;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        itemList = findViewById(R.id.favourite_recycler);

        configureRecyclerView();

        new LoadFavouriteAsync().execute();
    }

    private void configureRecyclerView() {
        adapter = new FavouriteListAdapter(context, list);
        itemList.setLayoutManager(new LinearLayoutManager(context));
        itemList.setAdapter(adapter);
    }

    private class LoadFavouriteAsync extends AsyncTask<Void, Void, Cursor> {

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
}
