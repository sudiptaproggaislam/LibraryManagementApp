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

public class studentListActivity extends AppCompatActivity {
    private SearchView srcRoll;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups=new ArrayList<>();

    private ListView rRoll;
    private Toolbar mToolbar;
    private DatabaseReference refRetrive;
    private String roll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        mToolbar=(Toolbar)findViewById(R.id.std_list_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Student Roll List");
        initialization();

        refRetrive= FirebaseDatabase.getInstance().getReference().child("All Student List");
        //refUpload= FirebaseDatabase.getInstance().getReference().child("All Student List");

        retriveAnddisplayCategory();


        rRoll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roll=parent.getItemAtPosition(position).toString();
                GotoStdReturnBookActivity();
            }
        });

        srcRoll.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                studentListActivity.this.arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                studentListActivity.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    private void retriveAnddisplayCategory() {
        refRetrive.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<>();
                Iterator iterator=dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()){

                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GotoStdReturnBookActivity() {
        Intent Intent= new Intent(studentListActivity.this,studentActivity.class);
        Intent.putExtra("roll",roll);
        Toast.makeText(studentListActivity.this,roll,Toast.LENGTH_LONG).show();
        startActivity(Intent);
    }

    private void initialization() {
        srcRoll=(SearchView)findViewById(R.id.search_bar_std_list);
        rRoll=(ListView)findViewById(R.id.list_item_std_list);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_groups);
        rRoll.setAdapter(arrayAdapter);
    }

}