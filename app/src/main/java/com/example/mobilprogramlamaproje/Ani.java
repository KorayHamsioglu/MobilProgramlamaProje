package com.example.mobilprogramlamaproje;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.net.URI;

public class Ani implements Serializable {
    String baslik;
    String description;
    Double longtitude,latittude;
    String sifre;
    URI imageUri;
    String date;

    public Ani(String baslik, String description, Double longtitude,Double latittude, String sifre,URI imageUri,String date) {
        this.baslik = baslik;
        this.description = description;
        this.longtitude = longtitude;
        this.latittude=latittude;
        this.sifre = sifre;
        this.imageUri=imageUri;
        this.date=date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Double getLatittude() {
        return latittude;
    }

    public void setLatittude(Double latittude) {
        this.latittude = latittude;
    }

    public URI getImageUri() {
        return imageUri;
    }

    public void setImageUri(URI imageUri) {
        this.imageUri = imageUri;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }
}
