package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AdminRequestActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ListView adminID;

    private String Aid;
    private SearchView srcID;


    private DatabaseReference refAdmin,refStudent2,refAccept,refDelete,ref,refAccept2,reff;

    ArrayAdapter<String> adapter;
    ArrayList<String> IDList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request);

        mToolbar=(Toolbar)findViewById(R.id.Admin_List_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add New Admin");

        Initialization();

        adminID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Aid=parent.getItemAtPosition(position).toString();


                Toast.makeText(AdminRequestActivity.this,Aid, Toast.LENGTH_SHORT).show();
                Intent Intent= new Intent(AdminRequestActivity.this,AdminDetailsActivity.class);
                Intent.putExtra("AdminID",Aid);
                startActivity(Intent);
            }
        });

        srcID.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AdminRequestActivity.this.adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AdminRequestActivity.this.adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void Initialization() {

        adminID=(ListView)findViewById(R.id.listAdminID);

        srcID=(SearchView)findViewById(R.id.search_bar_adminId);


        IDList=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,IDList);
        adminID.setAdapter(adapter);

        refAdmin= FirebaseDatabase.getInstance().getReference("Admin Request");
        refAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<>();
                Iterator iterator=dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()){

                    //String p=((DataSnapshot)iterator.next()).getKey();
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                IDList.clear();
                IDList.addAll(set);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}