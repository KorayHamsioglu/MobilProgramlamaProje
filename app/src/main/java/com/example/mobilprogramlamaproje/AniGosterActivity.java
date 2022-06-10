package com.example.mobilprogramlamaproje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AniGosterActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView txtViewBaslik,txtViewDesc,txtViewTarih;
    private ImageView imageViewAniGoster;
    private Button PDFcevir;

    GoogleMap myMap;
    Double longtitude,latitude;
    Ani ani;
    LatLng myLatlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ani_goster);


        txtViewBaslik=findViewById(R.id.textViewBaslikGoster);
        txtViewDesc=findViewById(R.id.textViewDescription);
        imageViewAniGoster=findViewById(R.id.imageViewAniGoster);
        txtViewTarih=findViewById(R.id.textViewTarih);
        PDFcevir=findViewById(R.id.buttonPdf);

        ani= (Ani) getIntent().getSerializableExtra("anigoster");


        txtViewBaslik.setText(ani.getBaslik());
        txtViewDesc.setText(ani.getDescription());
        System.out.println(ani.getDate());
         txtViewTarih.setText("Tarih : "+ani.getDate());
        imageViewAniGoster.setImageURI(Uri.parse(ani.getImageUri().toString()));
        longtitude=ani.getLongtitude();
        latitude=ani.getLatittude();

        myLatlng=new LatLng(latitude,longtitude);


        if (checkGoogleServicesAvailability()) {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAniGoster);
            supportMapFragment.getMapAsync(this);
            supportMapFragment.getView().setClickable(false);
        }

        PDFcevir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions();
            }
        });
    }

    private void requestPermissions() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            },200);
            createPDF();
        }else {
            createPDF();
        }
    }

    private void createPDF() {
        PdfDocument document=new PdfDocument();

        int witdh=500;
        int height=1000;

        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(witdh,height,1).create();
        PdfDocument.Page page=document.startPage(pageInfo);

        Canvas canvas=page.getCanvas();

        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(15);
        paint.setTextAlign(Paint.Align.CENTER);

        int line=50;
        canvas.drawText(txtViewBaslik.getText().toString(),witdh/2,line,paint);
        line=line+20;
        canvas.drawText(txtViewDesc.getText().toString(),70,line,paint);
        line=line+50;

        canvas.drawText(txtViewTarih.getText().toString(),witdh/2,line,paint);


        document.finishPage(page);


        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"/"+txtViewBaslik.getText().toString()+".pdf");
        System.out.println("BURASI");

        try {
         if(!file.exists()){
             file.createNewFile();
             System.out.println("oldu");
         }
        }catch (Exception e){
            System.out.println("olmadı");
        }

        try {
            document.writeTo(new FileOutputStream(file));
            document.close();
            Toast.makeText(getApplicationContext(),"PDF olusturuldu",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap=googleMap;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Position");
        markerOptions.position(myLatlng);
        googleMap.addMarker(markerOptions);
        CameraUpdate location= CameraUpdateFactory.newLatLngZoom(myLatlng,5);
        myMap.animateCamera(location);


    }
}