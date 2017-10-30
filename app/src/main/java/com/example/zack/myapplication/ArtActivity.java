package com.example.zack.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.example.zack.myapplication.R.layout.activity_art;

public class ArtActivity extends AppCompatActivity { //TODO landing page?

    //private SensorManager sensorManager;
    //private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_art);
        final Art piece;
        final double[] userLoc;


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            piece = new Art(extras.getString("title"), extras.getString("artist"), extras.getDoubleArray("location"), extras.getString("description"), extras.getString("picture"));
            userLoc = extras.getDoubleArray("userLoc");
            TextView title = (TextView) findViewById(R.id.title);
            TextView artist = (TextView) findViewById(R.id.artist);
            TextView description = (TextView) findViewById(R.id.description);
            TextView distance = (TextView) findViewById(R.id.distance);
            ImageView arrow = (ImageView) findViewById(R.id.arrow);
            ImageView picture = (ImageView) findViewById(R.id.picture);
            Button go = (Button) findViewById(R.id.go);
            title.setText(piece.title);
            artist.setText(piece.artist);
            description.setText(piece.description);

            if (piece.title == "Hart House Mask") {
                picture.setImageResource(R.drawable.harthousemask);
            }
            else if (piece.title == "Canoe Landing") {
                picture.setImageResource(R.drawable.canoelanding);
            }
            else if (piece.title == "Relief") {
                picture.setImageResource(R.drawable.relief);
            }

            picture.setImageBitmap(BitmapFactory.decodeFile(piece.picture));

            arrow.setRotation((float) piece.angleTo(userLoc));
            distance.setText(String.format("%.1f", piece.distanceTo(userLoc)) + " Km");

            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Hit Go");
                    Intent passArt = new Intent();
                    //passArt.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    passArt.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    Bundle extras = new Bundle();
                    passArt.putExtra("result", piece.location);
                    passArt.putExtra("location", piece.location);
                    passArt.putExtra("userLoc", userLoc);
                    setResult(Activity.RESULT_OK, passArt);
                    finish();
                }
            });
        }
    } // onCreate()




    @Override
    protected void onPause() {
        super.onPause();
        finish();
    } // onPause()

} //class
