package com.progga.librarymanagement;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class stdAdapterClass extends  RecyclerView.Adapter<stdAdapterClass.MyViewHolder>{

    ArrayList<deal> list;
    private Context context;
    private String booksCategory;
    // private TextView textView;

    public stdAdapterClass(Context context) {
        this.context = context;
    }

    public stdAdapterClass(ArrayList<deal> list) {
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.std_card_holder,parent,false);

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

            fnam=itemView.findViewById(R.id.std_nameDoc);
            button=itemView.findViewById(R.id.std_btnrv);
            av=itemView.findViewById(R.id.std_imgBtnAvailablity);

            button.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),stdBooksDetails.class);
            intent.putExtra("category",list.get(getAdapterPosition()).getCategory());
            intent.putExtra("name",list.get(getAdapterPosition()).getName());
            //textView.setText(list.get(getAdapterPosition()).getName());
            Toast.makeText(v.getContext(),list.get(getAdapterPosition()).getName(),Toast.LENGTH_SHORT).show();
            v.getContext().startActivity(intent);
        }
    }

}

