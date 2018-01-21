package test.com.venues.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import test.com.venues.Preferences.PreferencesManager;
import test.com.venues.R;
import test.com.venues.model.Venues;


public class VenuesAdapter extends RecyclerView.Adapter<VenuesAdapter.MovieViewHolder> {

    private List<Venues> venues;
    private int rowLayout;
    private Context context;
    private static MyClickListener myClickListener;

    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView data;
        TextView movieDescription;
        TextView rating;
        ImageView ratingImage, favoriteImage;


        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.subtitle);
            movieDescription = (TextView) v.findViewById(R.id.description);
            rating = (TextView) v.findViewById(R.id.rating);
            ratingImage = (ImageView) v.findViewById(R.id.rating_image);
            favoriteImage = (ImageView) v.findViewById(R.id.fav_img);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);

            //  mView.setBackgroundColor(itemView.getResources().getColor(R.color.red));

        }
    }

    public VenuesAdapter(List<Venues> venues, int rowLayout, Context context) {
        this.venues = venues;
        this.rowLayout = rowLayout;
        this.context = context;
        PreferencesManager.initializeInstance(context);
    }

    @Override
    public VenuesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.movieTitle.setText(venues.get(position).getAddress());
        holder.data.setText("City: " + venues.get(position).getCity());
        holder.movieDescription.setText("State: " + venues.get(position).getState());
        holder.rating.setText(venues.get(position).getRating().toString());
        if (!TextUtils.isEmpty(venues.get(position).getRatingcolor()))
            holder.ratingImage.setColorFilter(Color.parseColor("#" + venues.get(position).getRatingcolor()));

        if (venues.get(position).isFavorite()) {
            holder.favoriteImage.setImageResource(R.mipmap.favourite);
        } else {
            holder.favoriteImage.setImageResource(R.mipmap.unfavourite);
        }

        holder.favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < venues.size(); i++) {
                    venues.get(i).setFavorite(false);
                }
                venues.get(position).setFavorite(true);
                String id = venues.get(position).getId();
                PreferencesManager.getInstance().setFavedID(id);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return venues.size();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}