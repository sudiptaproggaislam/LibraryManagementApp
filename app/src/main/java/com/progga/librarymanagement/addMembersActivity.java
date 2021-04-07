package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class addMembersActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ListView stdRollNo;

    private String rollNo;
    private SearchView srcRoll;


    private DatabaseReference refStudent,refStudent2,refAccept,refDelete,ref,refAccept2,reff;

    ArrayAdapter<String> adapter;
    ArrayList<String> rollList;
    final HashMap<String, String> profileMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        mToolbar=(Toolbar)findViewById(R.id.add_member_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add New Member");

        Initialization();




        stdRollNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rollNo=parent.getItemAtPosition(position).toString();


                Toast.makeText(addMembersActivity.this,rollNo, Toast.LENGTH_SHORT).show();
                Intent Intent= new Intent(addMembersActivity.this,requestedMembersDetails.class);
                Intent.putExtra("roll",rollNo);
                startActivity(Intent);
            }
        });

        srcRoll.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                addMembersActivity.this.adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                addMembersActivity.this.adapter.getFilter().filter(newText);
                return false;
            }
        });


    }

    private void Initialization() {

        stdRollNo=(ListView)findViewById(R.id.stdRoll);

        srcRoll=(SearchView)findViewById(R.id.search_bar_add_member);


        rollList=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,rollList);
        stdRollNo.setAdapter(adapter);

        refStudent= FirebaseDatabase.getInstance().getReference("Request Student List");
        refStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<>();
                Iterator iterator=dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()){

                    //String p=((DataSnapshot)iterator.next()).getKey();
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                rollList.clear();
                rollList.addAll(set);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}