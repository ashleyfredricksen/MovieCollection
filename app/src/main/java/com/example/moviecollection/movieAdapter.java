package com.example.moviecollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class movieAdapter extends FirebaseRecyclerAdapter<MovieInfo, movieAdapter.movieViewHolder> {
//    public ArrayList<MovieInfo> movieInfoArrayList;
//    public ArrayList<MovieInfo> movieInfoArrayListFiltered;
//    Context context;

    public movieAdapter(FirebaseRecyclerOptions<MovieInfo> options) {
        super(options);
    }

//    @Override
//    public int getItemCount() {
//        return movieInfoArrayList.size();
//    }
//
//    public long getItemId(int position) {
//        return position;
//    }

    public void onBindViewHolder(@NonNull movieViewHolder holder, int position, @NonNull MovieInfo model) {
//        final MovieInfo movieInfo = movieInfoArrayList.get(position);
        holder.title.setText(model.getMovieTitle());//model.getMovieTitle());
        Picasso.get().load(model.getMovieURL()).into(holder.poster);
    }

    public movieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_rv_item, parent, false);
        return new movieAdapter.movieViewHolder(view);
    }

    class movieViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView poster;

        public movieViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.titleMovie);
            poster = view.findViewById(R.id.movieImg);
        }
    }

}
