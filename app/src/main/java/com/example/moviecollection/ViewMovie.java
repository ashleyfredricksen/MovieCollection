package com.example.moviecollection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ViewMovie extends AppCompatActivity {
    private String barcode;
    private String movieTitle;
    private TextView textView;
    private ImageView imageView;
    private Document doc, web;
    private Elements title, pic;
    private String imgURL;
    private ProgressDialog progressDialog;
    private Button add, view;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private MovieInfo movieInfo;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user").child(currentUser.getUid());

        movieInfo = new MovieInfo();

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        add = findViewById(R.id.addBtn);
        view = findViewById(R.id.viewBtn);

        add.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();

        Intent intent = getIntent();
        barcode = intent.getStringExtra(MainActivity.BARCODE_DATA);
//        getMovie();
    }

    public void addToCollection(View view) {
        movieInfo.setUserID(currentUser.getUid());
        movieInfo.setMovieTitle(movieTitle);
        movieInfo.setMovieURL(imgURL);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(barcode).setValue(movieInfo);
                Toast.makeText(ViewMovie.this, "Movie added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewMovie.this, "Failed to add movie" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void viewCollection(View view) {
        Intent intent = new Intent(ViewMovie.this, ViewCollection.class);
        startActivity(intent);
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display progress dialog
            progressDialog = new ProgressDialog(ViewMovie.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            movieTitle = "";
//            String url = "";
            try {
                doc = Jsoup.connect("https://www.upcdatabase.com/item/" + barcode + "").get();
                title = doc.select("#content > table > tbody > tr:nth-child(3) > td:nth-child(3)");
                movieTitle = title.text();

                web = Jsoup.connect("https://www.barcodeindex.com/upc/" + barcode + "").get();
                pic = web.select("body > main > section.pt-4 > div:nth-child(1) > div > div.col.s12.l5 > div > div > div > div.card-image.card-image-fit > img");
                imgURL = pic.attr("src");

                return movieTitle;
//                return imgURL;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return movieTitle;
//            return imgURL;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.d("data", s.toString());
                progressDialog.dismiss();

//                setContentView(R.layout.activity_view_movie);
                textView.setText(s);
                imageView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                Picasso.get().load(imgURL).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void getMovie() {
//
//        try {
//            //gets the title info
//            Document doc = Jsoup.connect("https://www.upcdatabase.com/item/" + barcode + "").get(); // connects to UPC website
//            Elements title = doc.select("#content > table > tbody > tr:nth-child(3) > td:nth-child(3)"); // gets the value of the Title of movie
//            TextView textView = findViewById(R.id.textView);
//            textView.setText(title.text()); // sets the title as the textView value
//
//            Document web = Jsoup.connect("https://www.barcodeindex.com/upc/" + barcode + "").get();
//            Elements pic = web.select("body > main > section.pt-4 > div:nth-child(1) > div > div.col.s12.l5 > div > div > div > div.card-image.card-image-fit > img");
//            String imgURL = pic.attr("src");
//            ImageView imageView = findViewById(R.id.imageView);
//            Picasso.get().load(imgURL).into(imageView);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}