package com.example.mobilprogramlamaproje;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

   private ImageButton buttonAnilar,buttonSifreBelirle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAnilar=findViewById(R.id.imageButtonAnıActivity);
        buttonSifreBelirle=findViewById(R.id.imageButtonSifreBelirle);

        SharedPreferences sharedPreferences=getSharedPreferences("sifre",MODE_PRIVATE);
        String sifre=sharedPreferences.getString("genelsifre",null);

        buttonAnilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sifre==null){
                    startActivity(new Intent(MainActivity.this,AnilarActivity.class));
                }else{
                    showDialogGenel(sifre);
                }

            }
        });

        buttonSifreBelirle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sifre==null){
                    startActivity(new Intent(MainActivity.this,SifreBelirleActivity.class));
                }
                else{
                    showDialogGenel2(sifre);
                }
            }
        });
    }

    public void showDialogGenel(String sifre){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.custom_popup_genel);

        Button btnDevam=dialog.findViewById(R.id.buttonDevamGenel);
        EditText editTextSifre=dialog.findViewById(R.id.editTextGenelSifreGir);

        btnDevam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sifre.equals(editTextSifre.getText().toString())){
                   startActivity(new Intent(MainActivity.this,AnilarActivity.class));
                   dialog.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Hatalı sifre girdiniz",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }


    public void showDialogGenel2(String sifre){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.custom_popup_genel);

        Button btnDevam=dialog.findViewById(R.id.buttonDevamGenel);
        EditText editTextSifre=dialog.findViewById(R.id.editTextGenelSifreGir);

        btnDevam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sifre.equals(editTextSifre.getText().toString())){
                    startActivity(new Intent(MainActivity.this,SifreBelirleActivity.class));
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Hatalı sifre girdiniz",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}