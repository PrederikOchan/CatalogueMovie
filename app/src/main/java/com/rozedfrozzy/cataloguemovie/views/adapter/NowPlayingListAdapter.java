package com.rozedfrozzy.cataloguemovie.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rozedfrozzy.cataloguemovie.views.DetailMovieActivity;
import com.rozedfrozzy.cataloguemovie.R;
import com.rozedfrozzy.cataloguemovie.model.ResultMovieItems;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NowPlayingListAdapter extends RecyclerView.Adapter<NowPlayingListAdapter.ItemViewHolder> {
    private ArrayList<ResultMovieItems> mData;
    private Context mContext;
    private String imgUrl = "http://image.tmdb.org/t/p/w342";

    public NowPlayingListAdapter(ArrayList<ResultMovieItems> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_movie_items, parent, false);
        return new ItemViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final ResultMovieItems nowPlayingItemModel = mData.get(position);
        holder.setModel(nowPlayingItemModel);
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mContext, DetailMovieActivity.class);
                detailIntent.putExtra(DetailMovieActivity.EXTRA_DETAIL, nowPlayingItemModel);
                mContext.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mData != null){
            return mData.size();
        }
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        TextView overview;
        TextView releaseDate;
        Button btnDetail;
        ResultMovieItems items;

        public ItemViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.img_home_item_poster);
            title = itemView.findViewById(R.id.item_home_title);
            overview = itemView.findViewById(R.id.item_home_overview);
            releaseDate = itemView.findViewById(R.id.item_home_release_date);
            btnDetail = itemView.findViewById(R.id.btn_detail);
        }

        public void setModel(ResultMovieItems items){
            this.items = items;

            String imgUrl2 = items.getPosterPath();
            Glide.with(mContext)
                    .load(imgUrl+imgUrl2)
                    .into(poster);

            this.title.setText(items.getTitle());
            this.overview.setText(items.getOverview());

            String inputReleaseDate = items.getReleaseDate();
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("EEEE, d MMM yyyy");

            try {
                Date inputDate = input.parse(inputReleaseDate);
                this.releaseDate.setText(output.format(inputDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearData(){
        mData.clear();
        notifyDataSetChanged();
    }
}
