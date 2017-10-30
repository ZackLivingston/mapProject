package com.example.zack.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Zack on 2017-07-19.
 */

class Art {

    public String title;
    public String artist;
    public double[] location = new double[2];
    public String description;
    public String picture;

    public Art(String name, String maker, double[] loc, String desc) {
        title = name;
        artist = maker;
        location = loc;
        description = desc;
    }

    public Art(String name, String maker, LatLng loc, String desc) {
        title = name;
        artist = maker;
        location[0] = loc.latitude;
        location[1] = loc.longitude;
        description = desc;
    }

    public Art(String name, String maker, LatLng loc, String desc, String img) {
        title = name;
        artist = maker;
        location[0] = loc.latitude;
        location[1] = loc.longitude;
        description = desc;
        picture = img;
    }

    public Art(String name, String maker, double[] loc, String desc, String img) {
        title = name;
        artist = maker;
        location = loc;
        description = desc;
        picture = img;
    }

    public double distanceTo(double[] userLoc) {
        return (Math.sqrt(Math.pow(this.location[0] - userLoc[0], 2) + Math.pow(this.location[1] - userLoc[1], 2))) * 111.319;
    }

    public String getUrl(double[] userLoc) {
        String toReturn = "https://maps.googleapis.com/maps/api/directions/json?origin="+ userLoc[0] +','+ userLoc[1]+"&destination="+ this.location[0] +','+ this.location[1] + "&mode=walking&key=AIzaSyBntgYz3AkKx-tQRPJGAompqzvC5byG2Eo";
        System.out.println(toReturn);
        return toReturn;
    }

    public double angleTo(double[] userLoc) {
        double vertical = this.location[1] - userLoc[1];
        double horizontal = this.location[0] - userLoc[0];
        double toReturn = Math.toDegrees(Math.atan2(vertical, horizontal));
        return toReturn;
    }

    public String toString() {
        return this.title;
    }

}
