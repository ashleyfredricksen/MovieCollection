package com.example.moviecollection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewCollection extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    private RecyclerView recyclerView;
    movieAdapter adapter;
//    private ArrayList<MovieInfo> movieInfoArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collection);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user").child(currentUser.getUid());

        Intent intent = getIntent();

        recyclerView = findViewById(R.id.movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MovieInfo> options = new FirebaseRecyclerOptions.Builder<MovieInfo>().setQuery(databaseReference, MovieInfo.class).build();

        adapter = new movieAdapter(options);

        recyclerView.setAdapter(adapter);

//        imageView = findViewById(R.id.MoviePoster);
//        textView = findViewById(R.id.MovieTitle);

//        viewCollection();
    }

    // search movies
    private void firebaseSearch(String text) {
        // convert string user enters into lowercase
        String query = text.toLowerCase();

        Query firebaseSearchQuery = databaseReference.orderByChild("search").startAt(query).endAt(query + "\uf8ff");

//        FirebaseRecyclerAdapter<MovieInfo, movieAdapter.movieViewHolder> firebaseRecyclerAdapter =
//                new FirebaseRecyclerAdapter<MovieInfo, movieAdapter.movieViewHolder>() {
//                    @Override
//                    protected void onBindViewHolder(@NonNull movieAdapter.movieViewHolder holder, int position, @NonNull MovieInfo model) {
//                        holder.title.setText(model.getMovieTitle());//model.getMovieTitle());
//                        Picasso.get().load(model.getMovieURL()).into(holder.poster);
//                    }
//
//                    @NonNull
//                    @Override
//                    public movieAdapter.movieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        return null;
//                    }
//                };
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void viewCollection() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String title = snap.child("movieTitle").getValue(String.class);
                    String url = snap.child("movieURL").getValue(String.class);
                    textView.setText(title);
                    Picasso.get().load(url).into(imageView);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//     From GeekstoGeeks tutorial
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.actionSearch);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });
        return true;
    }


//  from GeekstoGeeks tutorial
//    private void filter(String text) {
//        ArrayList<MovieInfo> filteredList = new ArrayList<>();
//
//        for (MovieInfo item : movieInfoArrayList) {
//            if (item.getMovieTitle().toLowerCase().contains(text.toLowerCase())) {
//                filteredList.add(item);
//            }
//        }
//        if (filteredList.isEmpty()) {
//            Toast.makeText(this, "No Movies Found...", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            adapter.filterList(filteredList);
//        }
//    }
}