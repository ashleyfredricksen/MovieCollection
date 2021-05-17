package com.example.moviecollection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class movieAdapter extends FirebaseRecyclerAdapter<MovieInfo, movieAdapter.movieViewHolder> {
    public movieAdapter(@NonNull FirebaseRecyclerOptions<MovieInfo> options) {
        super(options);
    }

    protected void onBindViewHolder(@NonNull movieViewHolder holder, int position, @NonNull MovieInfo model) {
        holder.title.setText(model.getMovieTitle());
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
