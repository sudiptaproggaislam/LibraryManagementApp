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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class categoryListViewStdActivity extends AppCompatActivity {

    private ListView list;
    private Toolbar mToolbar;
    private DatabaseReference refRetrive;
    private SearchView srcRoll;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups=new ArrayList<>();
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list_view_std);

        Initialization();
        mToolbar=(Toolbar)findViewById(R.id.std_bookCategoryActivityToolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Books Category");

        refRetrive= FirebaseDatabase.getInstance().getReference().child("Books Category");

        retriveAnddisplayCategory();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category=parent.getItemAtPosition(position).toString();
                GotoBookListActivity();
            }
        });

        srcRoll.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                categoryListViewStdActivity.this.arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                categoryListViewStdActivity.this.arrayAdapter.getFilter().filter(newText);
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

    private void Initialization() {
        list=(ListView)findViewById(R.id.listView_stdBookCat);
        srcRoll=(SearchView)findViewById(R.id.search_bar_std_book_category);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_groups);
        list.setAdapter(arrayAdapter);
    }

    private void GotoBookListActivity() {
        Intent Intent= new Intent(categoryListViewStdActivity.this,stdBooksListActivity.class);
        Intent.putExtra("category",category);
        startActivity(Intent);
    }
}