package com.example.mobilprogramlamaproje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SifreBelirleActivity extends AppCompatActivity {
    private EditText editTextGenelSifre;
    private Button buttonBelirle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_belirle);

        editTextGenelSifre=findViewById(R.id.editTextGenelSifre);
        buttonBelirle=findViewById(R.id.buttonBelirle);

        buttonBelirle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextGenelSifre.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Sifre alanı boş bırakılamaz.",Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences sharedPreferences=getSharedPreferences("sifre",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("genelsifre",editTextGenelSifre.getText().toString());
                    editor.apply();
                    startActivity(new Intent(SifreBelirleActivity.this,MainActivity.class));
                }
            }
        });
    }
}