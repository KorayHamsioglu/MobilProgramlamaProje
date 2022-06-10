package com.example.mobilprogramlamaproje;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AnilarActivity extends AppCompatActivity {



    private Button btnAniEkle;
    private ArrayList<Ani> aniArrayList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anilar);
        btnAniEkle=findViewById(R.id.btnAddAni);
        recyclerView=findViewById(R.id.recyclerView);

        loadData();
        if(getIntent().hasExtra("ani")){
            Ani ani= (Ani) getIntent().getSerializableExtra("ani");
            aniArrayList.add(ani);
        }
        if(getIntent().hasExtra("aniguncel")){
            Ani ani= (Ani) getIntent().getSerializableExtra("aniguncel");
            int position=getIntent().getIntExtra("position",-1);
            aniArrayList.set(position,ani);
        }

        if(aniArrayList.size()!=0){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new AnilarAdapter(aniArrayList,getApplicationContext(),AnilarActivity.this));
        }
        saveData();

        btnAniEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AnilarActivity.this,AniYazActivity.class));
            }
        });


    }


    public void saveData(){
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String data=gson.toJson(aniArrayList);
        editor.putString("anilar",data);
        editor.apply();


    }

    public void loadData(){

        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        Gson gson=new Gson();
        String data=sharedPreferences.getString("anilar",null);
        Type type=new TypeToken<ArrayList<Ani>>() {}.getType();
        aniArrayList=gson.fromJson(data,type);

        if(aniArrayList==null){
            aniArrayList=new ArrayList<>();
        }

    }
}