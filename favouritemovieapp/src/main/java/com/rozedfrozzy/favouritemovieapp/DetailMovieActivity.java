package com.rozedfrozzy.favouritemovieapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rozedfrozzy.favouritemovieapp.model.ResultMovieItems;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String EXTRA_DETAIL = "extra_detail";
    private String imgUrl = "http://image.tmdb.org/t/p/w780";

    private ImageView imgDetail;
    private TextView detailTitle, detailReleaseDate, detailOverview;
    private ResultMovieItems detailItem;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        detailItem = (ResultMovieItems) getIntent().getSerializableExtra(EXTRA_DETAIL);

        imgDetail = findViewById(R.id.img_detail_movie);
        detailTitle = findViewById(R.id.tv_detail_title);
        detailReleaseDate = findViewById(R.id.tv_detail_release);
        detailOverview = findViewById(R.id.tv_detail_overview);

        injectData(detailItem);
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
}
