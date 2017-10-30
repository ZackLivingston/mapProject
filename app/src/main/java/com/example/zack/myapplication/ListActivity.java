package com.example.zack.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    public void changeText(char id, Art piece) {
        //String tS = "R.id.textView5"+id;
        //String aS = "R.id.textView4"+id;
        //String dS = "R.id.textView6"+id;
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        finish();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_scroll);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final double[] userLoc = (double[]) extras.getDoubleArray("loc");
            LatLng cl = new LatLng(43.639575, -79.396863);
            LatLng hhm = new LatLng(43.663880, -79.395228);
            LatLng relief = new LatLng(43.683913, -79.402811);
            String hhmImg = "/drawable/harthousemask.jpg";
            String clImg = "/drawable/canoelanding.jpg";
            String reliefImg = "/drawable/relief.jpg";
            final Art[] pieces = new Art[3];

            Art hhmPiece = new Art("Hart House Mask", "Evan Grant Penny, 1990", hhm, "Concrete.\nCommissioned by Hart House.", hhmImg);
            Art clPiece = new Art("Canoe Landing", "Douglas Coupland, 2009", cl, "Painted Steel.\nCommissioned by Concord Adex Developments.", clImg);
            Art reliefPiece = new Art("Relief", "Augusts Kopmanis", relief, "Stone.\nCommissioned by St. John's Latvian Lutheran Church.", reliefImg);
            pieces[0] = clPiece;
            pieces[1] = hhmPiece;
            pieces[2] = reliefPiece;
            Art[] piecesAZ = pieces.clone();
            final List list = new List(pieces);
            final List listAZ = new List(piecesAZ);
            final LinearLayout subScroll = (LinearLayout) findViewById(R.id.subScroll);
            subScroll.removeAllViews();
            for (int i=0;i<list.array.length;i++){
                subScroll.addView(pieceBuild(list.array[i],userLoc, i));
            }

            ToggleButton tb = (ToggleButton) findViewById(R.id.toggleSort);
            tb.setChecked(true);
            tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) { // Sort by distance away
                        list.insertionSort(userLoc);
                        subScroll.removeAllViews();
                        for (int i=0;i<list.array.length;i++){
                            subScroll.addView(pieceBuild(list.array[i],userLoc, i));
                        }
                    } else { // A-Z sort
                        final LinearLayout subScroll = (LinearLayout) findViewById(R.id.subScroll);
                        subScroll.removeAllViews();
                        for (int i=0;i<listAZ.array.length;i++){
                            subScroll.addView(pieceBuild(listAZ.array[i],userLoc, i));
                        }
                    }
                }
            });
        } // if
        else {
            System.out.println("balls.");
        }
    } // onCreate



    public void click(Art listItem, double[] userLoc) {
        Intent passArt = new Intent(getApplicationContext(),ArtActivity.class);
        Bundle extras = new Bundle();
        extras.putString("title", listItem.title);
        extras.putString("artist", listItem.artist);
        extras.putString("description", listItem.description);
        extras.putDoubleArray("location", listItem.location);
        extras.putDoubleArray("userLoc", userLoc);
        extras.putString("picture", listItem.picture);
        passArt.putExtras(extras);
        startActivityForResult(passArt, 2);
        //startActivity(passArt);
    }



    public View pieceBuild(final Art piece, final double[] userLoc, final int j) {

        LinearLayout outer = new LinearLayout(this);
        LinearLayout.LayoutParams oParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        oParams.topMargin = 10;
        oParams.bottomMargin = 10;
        outer.setLayoutParams(oParams);
        outer.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout innerOne = new LinearLayout(this);
        LinearLayout.LayoutParams i1Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                0.25f);
        innerOne.setLayoutParams(i1Params);
        innerOne.setOrientation(LinearLayout.VERTICAL);

        LinearLayout innerTwo = new LinearLayout(this);
        LinearLayout.LayoutParams i2Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f);
        innerTwo.setLayoutParams(i2Params);
        innerTwo.setOrientation(LinearLayout.VERTICAL);




        final Button button = new Button(this);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f);
        titleParams.bottomMargin = 0-10;
        button.setLayoutParams(titleParams);
        button.setId(j+1);
        button.setText(piece.title);
        button.setGravity(Gravity.LEFT);
        button.setPadding(5, 0, 0, 5);
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setTextSize(28);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(piece, userLoc);
            }
        });

        final TextView maker = new TextView(this);
        LinearLayout.LayoutParams makerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f);
        makerParams.topMargin = 0-10;
        maker.setPadding(5, 0, 0, 0);
        maker.setTextColor(Color.parseColor("#000000"));
        maker.setText(piece.artist);
        maker.setTextSize(16);
        maker.setLayoutParams(makerParams);

        final TextView n = new TextView(this);
        LinearLayout.LayoutParams nParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nParams.setMargins(0, 1, 0, 1);
        n.setLayoutParams(nParams);
        //n.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        n.setTextSize(9);
        n.setGravity(Gravity.CENTER_HORIZONTAL);
        n.setText("N");

        ImageView arrow = new ImageView(this);
        LinearLayout.LayoutParams aParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        arrow.setLayoutParams(aParams);
        arrow.setImageResource(R.drawable.arrow);
        arrow.setRotation((float) piece.angleTo(userLoc));

        final TextView dist = new TextView(this);
        LinearLayout.LayoutParams dParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dParams.setMargins(0, 2, 0, 2);
        dist.setLayoutParams(dParams);
        dist.setGravity(Gravity.CENTER_HORIZONTAL);
        dist.setText(String.format("%.1f", piece.distanceTo(userLoc)) + " Km");
        dist.setTextColor(Color.parseColor("#000000"));
        dist.setTextSize(12);

        innerOne.addView(button);
        innerOne.addView(maker);
        innerTwo.addView(n);
        innerTwo.addView(arrow);
        innerTwo.addView(dist);
        outer.addView(innerOne);
        outer.addView(innerTwo);
        return outer;
    } // buildPiece()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                System.out.println("list-level REQUEST OK");
                String result=data.getStringExtra("result");
                double[] location = data.getExtras().getDoubleArray("location");
                double[] userLoc = data.getExtras().getDoubleArray("userLoc");

                Intent passArt = new Intent();
                passArt.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle extras = new Bundle();
                passArt.putExtra("result", location);
                passArt.putExtra("location", location);
                passArt.putExtra("userLoc", userLoc);
                setResult(Activity.RESULT_OK, passArt);
                finish();

            } // if result is OK
        } //if requestCode == 2
    } // onActivityResult()

}
