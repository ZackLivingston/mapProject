package com.example.zack.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    double[] userLoc = {43.7106937, -79.4134916}; //TODO set to most recent location
    PolylineOptions route = new PolylineOptions();
    private GoogleMap mMap;
    Polyline drawnRoute = null;
    ArrayList<Art> pieces = new ArrayList<Art>();

    private FusedLocationProviderApi locationProvider = LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }



        Button button = (Button) findViewById(R.id.menuButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Hit Button");
                Intent passLoc = new Intent(getApplicationContext(), ListActivity.class);
                System.out.println("Pre-pass: " + userLoc.toString());
                passLoc.putExtra("loc", userLoc);
                startActivityForResult(passLoc, 2);
                //startActivity(passLoc);
                //setContentView(R.layout.activity_scroll);
            } // onClick

        });
    } // onMapReady

    public String getUrl(double[] userLoc, LatLng piece) {
        String toReturn = "https://maps.googleapis.com/maps/api/directions/json?origin=" + userLoc[0] + ',' + userLoc[1] + "&destination=" + piece.latitude + ',' + piece.longitude + "&mode=walking&key=AIzaSyBntgYz3AkKx-tQRPJGAompqzvC5byG2Eo";
        System.out.println(toReturn);
        return toReturn;
    }

    public void drawDirections(final double[] userLoc, LatLng piece) {
        if (drawnRoute != null) {
            drawnRoute.remove();
        }
        final String url = getUrl(userLoc, piece);
        final double[] point = new double[2];
        final RequestQueue rq = Volley.newRequestQueue(MapsActivity.this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            PolylineOptions route = new PolylineOptions();
                            route.add(new LatLng(userLoc[0], userLoc[1]));
                            System.out.println(url);
                            JSONArray steps = response.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
                            int len = steps.length();
                            for (int i = 0; i < len; i++) {
                                point[1] = steps.getJSONObject(i).getJSONObject("end_location").getDouble("lng");
                                point[0] = steps.getJSONObject(i).getJSONObject("end_location").getDouble("lat");
                                route.add(new LatLng(point[0], point[1]));
                            }
                            drawnRoute = mMap.addPolyline(route);
                        } // try
                        catch (JSONException e) {
                            e.printStackTrace();
                        } // catch
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error");
                rq.stop();
            }
        });
        rq.add(jor);
    } // drawDirections



    @Override
    public void onLocationChanged(Location location) {
        userLoc[0] = location.getLatitude();
        userLoc[1] = location.getLongitude();
        System.out.println("New User Location: " + userLoc[0] + ", " + userLoc[1]);
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        intent = getIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");
        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            System.out.println("extras != null");
            Art piece = new Art(extras.getString("title"), extras.getString("artist"), extras.getDoubleArray("location"), extras.getString("description"));
            LatLng loc = new LatLng(piece.location[0], piece.location[1]);
            drawDirections(extras.getDoubleArray("userLoc"), loc);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(extras.getDoubleArray("userLoc")[0], extras.getDoubleArray("userLoc")[1]), 15));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(this);
        LatLng cl = new LatLng(43.639575, -79.396863);
        LatLng hhm = new LatLng(43.663880, -79.395228);
        LatLng relief = new LatLng(43.683913, -79.402811);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        mMap.addMarker(new MarkerOptions().position(hhm).title("Hart House Mask").snippet("Tap for more info").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.addMarker(new MarkerOptions().position(relief).title("Relief").snippet("Tap for more info").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.addMarker(new MarkerOptions().position(cl).title("Canoe Landing").snippet("Tap for more info").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//        mMap.addMarker(new MarkerOptions().position(allenby).title("Allenby\nTap for more info"));
//        mMap.addMarker(new MarkerOptions().position(goml).title("GOML\nTap for more info"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(qp, 15));
//        mMap.addMarker(new MarkerOptions().position(qp).title("Queen's Park\nTap for more info"));
        String hhmImg = "/app/src/main/res/drawable/harthousemask.jpg";
        String clImg = "/app/src/main/res/drawable/canoelanding.jpg";
        String reliefImg = "/app/src/main/res/drawable/relief.jpg";

        Art hhmPiece = new Art("Hart House Mask", "Evan Grant Penny, 1990", hhm, "Concrete.\nCommissioned by Hart House.", hhmImg);
        Art clPiece = new Art("Canoe Landing", "Douglas Coupland, 2009", cl, "Painted Steel.\nCommissioned by Concord Adex Developments.", clImg);
        Art reliefPiece = new Art("Relief", "Augusts Kopmanis", relief, "Stone.\nCommissioned by St. John's Latvian Lutheran Church.", reliefImg);

        System.out.println(pieces);
        pieces.add(hhmPiece);
        pieces.add(clPiece);
        pieces.add(reliefPiece);
    }

    //@Override
    public void onInfoWindowClick(Marker marker) {
        /**if (drawnRoute != null) {
         drawnRoute.remove();
         } // if
         System.out.println("onInfoWindowClick: " + marker.toString());
         drawDirections(userLoc, marker.getPosition()); **/
        String pieceTitle = marker.getTitle();
        Art piece = null;
        for (Art elem:pieces) {
            if (pieceTitle.equals(elem.title)) {
                piece = elem;
            } // if
        } // for
        Intent passArt = new Intent(getApplicationContext(), ArtActivity.class);
        Bundle extras = new Bundle();
        extras.putString("title", piece.title);
        extras.putString("artist", piece.artist);
        extras.putString("description", piece.description);
        extras.putDoubleArray("location", piece.location);
        extras.putDoubleArray("userLoc", userLoc);
        extras.putString("picture", piece.picture);
        passArt.putExtras(extras);
        startActivityForResult(passArt, 1);

    } // infoWindowClick

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //TODO make this work from the list activity
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                double[] location = data.getExtras().getDoubleArray("location");

                Bundle extras = getIntent().getExtras();
                LatLng loc = new LatLng(location[0], location[1]);
                drawDirections(userLoc, loc);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLoc[0], userLoc[1]), 15));
            }
        }
        else if (requestCode == 2) {
            System.out.println("maps level requestCode = 2");
            if(resultCode == Activity.RESULT_OK){
                System.out.println("RESULT OK");
                String result=data.getStringExtra("result");
                double[] location = data.getExtras().getDoubleArray("location");

                Bundle extras = getIntent().getExtras();
                LatLng loc = new LatLng(location[0], location[1]);
                drawDirections(userLoc, loc);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLoc[0], userLoc[1]), 15));
            }
        }
    } // onActivityResult()
}
