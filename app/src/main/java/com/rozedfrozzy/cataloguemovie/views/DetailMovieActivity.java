package com.rozedfrozzy.cataloguemovie.views;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rozedfrozzy.cataloguemovie.R;
import com.rozedfrozzy.cataloguemovie.db.DatabaseContract;
import com.rozedfrozzy.cataloguemovie.db.FavouriteHelper;
import com.rozedfrozzy.cataloguemovie.model.ResultMovieItems;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.rozedfrozzy.cataloguemovie.db.DatabaseContract.CONTENT_URI;

public class DetailMovieActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView imgDetail;
    private TextView detailTitle, detailReleaseDate, detailOverview;
    private FloatingActionButton fabFavourite;
    private ResultMovieItems detailItem;
    private Context mContext;

    private FavouriteHelper favouriteHelper;

    public static final String EXTRA_DETAIL = "extra_detail";
    private String imgUrl = "http://image.tmdb.org/t/p/w780";
    private Boolean isFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        mContext = this;
        detailItem = (ResultMovieItems) getIntent().getSerializableExtra(EXTRA_DETAIL);

        favouriteHelper = new FavouriteHelper(this);
        favouriteHelper.open();

        imgDetail = findViewById(R.id.img_detail_movie);
        detailTitle = findViewById(R.id.tv_detail_title);
        detailReleaseDate = findViewById(R.id.tv_detail_release);
        detailOverview = findViewById(R.id.tv_detail_overview);
        fabFavourite = findViewById(R.id.fab_favourite);

        Uri uri = Uri.parse(CONTENT_URI+"/"+detailItem.getId());

        if (uri != null){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            Log.d("cursor", String.valueOf(cursor));

            if (cursor != null){
                Log.d("Uri detail", String.valueOf(uri));
                if (cursor.moveToFirst()){
                    detailItem = new ResultMovieItems(cursor);
                    isFavourite = true;
                    setFavourite(isFavourite);
                }

                cursor.close();
            }
        }

        injectData(detailItem);

        fabFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavourite){
                    removeFavourite();
                    isFavourite = false;
                } else {
                    saveFavourite();
                    isFavourite = true;
                }

                setFavourite(isFavourite);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (favouriteHelper != null){
            favouriteHelper.close();
        }
    }

    private void injectData(ResultMovieItems detailItem) {
        String imgUrl2 = detailItem.getBackdropPath();
        Glide.with(mContext)
                .load(imgUrl+imgUrl2)
                .into(imgDetail);
        detailTitle.setText(detailItem.getTitle());
        detailOverview.setText(detailItem.getOverview());

        String inputReleaseDate = detailItem.getReleaseDate();
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("EEEE, d MMM yyyy");

        try {
            Date inputDate = input.parse(inputReleaseDate);
            detailReleaseDate.setText(output.format(inputDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setFavourite(Boolean isFavourite) {
        if (isFavourite){
            fabFavourite.setImageResource(R.drawable.ic_favorite_24dp);
        } else {
            fabFavourite.setImageResource(R.drawable.ic_favorite_border_24dp);
        }

    }

    private void saveFavourite() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.FavouriteColumns.MOVIE_ID, detailItem.getId());
        contentValues.put(DatabaseContract.FavouriteColumns.POSTER, detailItem.getPosterPath());
        contentValues.put(DatabaseContract.FavouriteColumns.TITLE, detailItem.getTitle());
        contentValues.put(DatabaseContract.FavouriteColumns.OVERVIEW, detailItem.getOverview());
        contentValues.put(DatabaseContract.FavouriteColumns.RELEASE_DATE, detailItem.getReleaseDate());
        contentValues.put(DatabaseContract.FavouriteColumns.BACKDROP, detailItem.getBackdropPath());

        getContentResolver().insert(CONTENT_URI, contentValues);
        Toast.makeText(this, R.string.save_favourite, Toast.LENGTH_SHORT).show();
    }

    private void removeFavourite() {
        getContentResolver().delete(
                Uri.parse(CONTENT_URI + "/" + detailItem.getId()),
                null,
                null
        );
        Toast.makeText(this, R.string.remove_favourite, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
