package com.rozedfrozzy.cataloguemovie.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rozedfrozzy.cataloguemovie.model.ResultMovieItems;
import com.rozedfrozzy.cataloguemovie.views.DetailMovieActivity;
import com.rozedfrozzy.cataloguemovie.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SearchMovieListAdapter extends RecyclerView.Adapter<SearchMovieListAdapter.ItemViewHolder> {
    private ArrayList<ResultMovieItems> mData;
    private Context mContext;
    private String imgUrl = "http://image.tmdb.org/t/p/w185";

    public SearchMovieListAdapter(ArrayList<ResultMovieItems> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_items, parent, false);
        return new ItemViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final ResultMovieItems movieItemModel = mData.get(position);
        holder.setModel(movieItemModel);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mContext, DetailMovieActivity.class);
                detailIntent.putExtra(DetailMovieActivity.EXTRA_DETAIL, movieItemModel);
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
        LinearLayout itemLayout;
        ResultMovieItems items;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.layout_movie_items);
            poster = itemView.findViewById(R.id.img_item_movie);
            title = itemView.findViewById(R.id.tv_item_title);
            overview = itemView.findViewById(R.id.tv_item_overview);
            releaseDate = itemView.findViewById(R.id.tv_item_release);
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
