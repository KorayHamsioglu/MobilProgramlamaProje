package com.example.mobilprogramlamaproje;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AnilarAdapter extends RecyclerView.Adapter<AnilarAdapter.ViewHolder> {

    ArrayList<Ani> aniArrayList;
    Context context;
    Activity activity;
    public AnilarAdapter(ArrayList<Ani> aniArrayList, Context context, Activity activity) {
        this.aniArrayList = aniArrayList;
        this.context=context;
        this.activity=activity;
    }

    @NonNull
    @Override
    public AnilarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.carview,parent,false);
        return new AnilarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnilarAdapter.ViewHolder holder, int position) {
        Ani ani=aniArrayList.get(position);
        holder.textViewBaslik.setText(ani.getBaslik());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDialog(ani);

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 showDialogForDelete(holder);
                 Toast.makeText(context.getApplicationContext(), "Tıklandı",Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,AniYazActivity.class);
                intent.putExtra("anilar",aniArrayList);
                intent.putExtra("position",holder.getAdapterPosition());
                intent.putExtra("ani",ani);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, ani.getBaslik());
                intent.putExtra(android.content.Intent.EXTRA_TEXT, ani.getDescription());
                activity.startActivity(Intent.createChooser(intent,"Share via"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return aniArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewBaslik;
        Button btnDelete,btnGuncelle,btnSend;


        public ViewHolder(View itemView) {
            super(itemView);
          textViewBaslik=itemView.findViewById(R.id.textViewAniAd);
          btnDelete=itemView.findViewById(R.id.buttonDelete);
          btnGuncelle=itemView.findViewById(R.id.buttonGuncelle);
          btnSend=itemView.findViewById(R.id.buttonSend);


        }
    }

    public void showDialog(Ani ani){
        Dialog dialog=new Dialog(activity);
        dialog.setContentView(R.layout.custom_popup);

        Button btnDevam=dialog.findViewById(R.id.buttonDevamPopup);
        EditText editTextSifre=dialog.findViewById(R.id.editTextPopup);

        btnDevam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if (editTextSifre.getText().toString().equals(ani.getSifre())){
                 Intent intent=new Intent(context,AniGosterActivity.class);
                 Date date= Calendar.getInstance().getTime();

                 SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                 String formattedDate=dateFormat.format(date);
                 ani.setDate(formattedDate);
                 intent.putExtra("anigoster",ani);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 dialog.dismiss();
                 context.startActivity(intent);
             }
             else{
                 Toast.makeText(context,"Sifre Hatalı",Toast.LENGTH_SHORT).show();
                 dialog.dismiss();
             }
            }
        });
        dialog.show();
    }

    public void showDialogForDelete(AnilarAdapter.ViewHolder holder){
        Dialog dialogDelete=new Dialog(activity);
        dialogDelete.setContentView(R.layout.custom_popup_delete);

        Button btnYes=dialogDelete.findViewById(R.id.buttonYes);
        Button btnNo=dialogDelete.findViewById(R.id.buttonHayır);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences= context.getSharedPreferences("data",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                Gson gson=new Gson();
                String data=sharedPreferences.getString("anilar",null);
                Type type=new TypeToken<ArrayList<Ani>>() {}.getType();
                aniArrayList=gson.fromJson(data,type);
                aniArrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyDataSetChanged();
                String dataPut=gson.toJson(aniArrayList);
                editor.putString("anilar",dataPut);
                editor.apply();
                Toast.makeText(context.getApplicationContext(), "Ani silindi.",Toast.LENGTH_SHORT).show();
                dialogDelete.dismiss();

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialogDelete.dismiss();
            }
        });
        dialogDelete.show();

    }
}
