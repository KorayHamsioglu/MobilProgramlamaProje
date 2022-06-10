package com.example.mobilprogramlamaproje;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AniYazActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    private static final int ACCESS_FINE_LOCATION = 100;
    private static final int ACCESS_COARSE_LOCATION = 101;
    private EditText editTextAniBaslik,editTextAniDesc,editTextPassword;
    private Button btnKaydet,btnResimEkle;
    private ImageView imageView;
    private boolean guncelle=false;
    Uri imageUri;
    ArrayList<Ani> aniArrayList;
    int position;

    GoogleMap googleMap;
    private LatLng myLatlng;
    Double latitude,longtitude;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK&&data!=null){
             imageUri=data.getData();

             imageView.setImageURI(imageUri);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ani_yaz);

       // btnLocation = findViewById(R.id.buttonLocation);
        editTextAniBaslik=findViewById(R.id.editTextAniBaslik);
        editTextAniDesc=findViewById(R.id.editTextAni);
        editTextPassword=findViewById(R.id.editTextPassword);
        imageView=findViewById(R.id.imageView);
        btnKaydet=findViewById(R.id.buttonSave);
        btnResimEkle=findViewById(R.id.buttonResimEkle);


        fineLocationPermission();
        coarseLocationPermission();
        if (getIntent().hasExtra("anilar")){
            guncelle=true;
            aniArrayList= (ArrayList<Ani>) getIntent().getSerializableExtra("anilar");
            Ani ani= (Ani) getIntent().getSerializableExtra("ani");
            position=getIntent().getIntExtra("position",-1);
            editTextAniBaslik.setText(ani.getBaslik());
            editTextPassword.setText(ani.getSifre());
            editTextAniDesc.setText(ani.getDescription());
            imageUri=Uri.parse(ani.getImageUri().toString());
            myLatlng=new LatLng(ani.getLatittude(),ani.getLongtitude());
            //imageView.setImageURI(Uri.parse(ani.getImageUri().toString()));

        }

        if (checkGoogleServicesAvailability()) {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
            supportMapFragment.getMapAsync(this);
        }
        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (guncelle==true){
                    try {
                        Date date= Calendar.getInstance().getTime();

                        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String formattedDate=dateFormat.format(date);
                        System.out.println(formattedDate);
                        URI uri=new URI(imageUri.toString());
                        Ani ani=new Ani(editTextAniBaslik.getText().toString(),editTextAniDesc.getText().toString(),myLatlng.longitude,myLatlng.latitude,editTextPassword.getText().toString(),uri,formattedDate);
                        Intent intent=new Intent(AniYazActivity.this,AnilarActivity.class);
                        System.out.println(imageUri.toString());
                        intent.putExtra("aniguncel", (Serializable) ani);
                        intent.putExtra("anilist",aniArrayList);
                        intent.putExtra("position",position);
                        Toast.makeText(getApplicationContext(),"Ani başarıyla guncellendi",Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                }else{
                    try {
                        Date date= Calendar.getInstance().getTime();

                        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String formattedDate=dateFormat.format(date);
                        System.out.println(formattedDate);
                        URI uri=new URI(imageUri.toString());
                        Ani ani=new Ani(editTextAniBaslik.getText().toString(),editTextAniDesc.getText().toString(),myLatlng.longitude,myLatlng.latitude,editTextPassword.getText().toString(),uri,formattedDate);
                        Intent intent=new Intent(AniYazActivity.this,AnilarActivity.class);
                        System.out.println(imageUri.toString());
                        intent.putExtra("ani", (Serializable) ani);
                        Toast.makeText(getApplicationContext(),"Ani başarıyla eklendi.",Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                }


            }
        });

        btnResimEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,3);
            }
        });



    }

    private boolean checkGoogleServicesAvailability() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    public void fineLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);

            }
        }
    }

    public void coarseLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION);

            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng latLng = new LatLng(0, 0);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Position");
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);

        this.googleMap.setOnMapClickListener(this);



    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Toast.makeText(this,"asssssss",Toast.LENGTH_SHORT).show();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        System.out.println("GIRDI");
        markerOptions.title("Position");
        googleMap.clear();
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 0);
        googleMap.animateCamera(location);
        googleMap.addMarker(markerOptions);
        myLatlng=latLng;


    }



}