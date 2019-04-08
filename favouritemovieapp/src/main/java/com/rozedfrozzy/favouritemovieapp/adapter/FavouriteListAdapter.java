package com.rozedfrozzy.favouritemovieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rozedfrozzy.favouritemovieapp.DetailMovieActivity;
import com.rozedfrozzy.favouritemovieapp.R;
import com.rozedfrozzy.favouritemovieapp.model.ResultMovieItems;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.ItemViewHolder>{

    private Context context;
    private Cursor list;

    private String imgUrl = "http://image.tmdb.org/t/p/w342";

    public FavouriteListAdapter(Context context, Cursor list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(Cursor items) {
        list = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_movie_items, parent, false);
        return new ItemViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final ResultMovieItems favouriteItems = getItem(position);
        holder.setModel(favouriteItems);
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(context, DetailMovieActivity.class);
                detailIntent.putExtra(DetailMovieActivity.EXTRA_DETAIL, favouriteItems);
                context.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.getCount();
    }

    private ResultMovieItems getItem(int position){
        if (!list.moveToPosition(position)){
            throw new IllegalStateException("Position Invalid");
        }
        return new ResultMovieItems(list);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        TextView overview;
        TextView releaseDate;
        Button btnDetail;

        public ItemViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.img_home_item_poster);
            title = itemView.findViewById(R.id.item_home_title);
            overview = itemView.findViewById(R.id.item_home_overview);
            releaseDate = itemView.findViewById(R.id.item_home_release_date);
            btnDetail = itemView.findViewById(R.id.btn_detail);
        }

        public void setModel(final ResultMovieItems favouriteItems) {
            String imgUrl2 = favouriteItems.getPosterPath();
            Glide.with(itemView.getContext())
                    .load(imgUrl+imgUrl2)
                    .into(poster);

            this.title.setText(favouriteItems.getTitle());
            this.overview.setText(favouriteItems.getOverview());

            String inputReleaseDate = favouriteItems.getReleaseDate();
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
}
