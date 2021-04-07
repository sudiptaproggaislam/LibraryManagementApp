package com.progga.librarymanagement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterClass extends  RecyclerView.Adapter<AdapterClass.MyViewHolder>{

    ArrayList<deal> list;
    private Context context;
    private String booksCategory;
   // private TextView textView;

    public AdapterClass(Context context) {
        this.context = context;
    }

    public AdapterClass(ArrayList<deal> list) {
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.fnam.setText(list.get(position).getName());
        if(list.get(position).getAvail().equals("Unavailable")){
            holder.av.setVisibility(View.INVISIBLE);
        }else{
            holder.av.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView fnam;
        Button button;
        CircleImageView av;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fnam=itemView.findViewById(R.id.nameDoc);
            button=itemView.findViewById(R.id.btnrv);
            av=itemView.findViewById(R.id.imgBtnAvailablity);

            button.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),BooksDetails.class);
            intent.putExtra("category",list.get(getAdapterPosition()).getCategory());
            intent.putExtra("name",list.get(getAdapterPosition()).getName());
            //textView.setText(list.get(getAdapterPosition()).getName());
            Toast.makeText(v.getContext(),list.get(getAdapterPosition()).getName(),Toast.LENGTH_SHORT).show();
            v.getContext().startActivity(intent);
        }
    }

}
