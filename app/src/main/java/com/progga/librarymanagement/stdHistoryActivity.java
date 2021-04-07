package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
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

public class stdHistoryActivity extends AppCompatActivity {
    private SearchView srcRoll;
    private ListView rRoll;
    private Toolbar mToolbar;
    private DatabaseReference refRetrive,refUpload;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups=new ArrayList<>();
    private String roll;
    private ret r=new ret();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_history);

        mToolbar=(Toolbar)findViewById(R.id.stdRollList_forHistory_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("History");

        initialization();

        refRetrive= FirebaseDatabase.getInstance().getReference().child("All Student List").child(roll).child("history");

        retriveAnddisplayHistory();

        srcRoll.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                stdHistoryActivity.this.arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                stdHistoryActivity.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void retriveAnddisplayHistory() {
        refRetrive.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list_of_groups.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    r=ds.getValue(ret.class);
                    list_of_groups.add(r.getBook()+"\n"+"Category-"+r.getCategory()+"\n"+"Issue Date-"+r.getIssuedate()+"\n"+"Return Date-"+r.getIssuedate()+"\n"+"Status-"+r.getStatus());
                }

                rRoll.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialization() {
        roll=getIntent().getExtras().getString("roll");

        srcRoll=(SearchView)findViewById(R.id.search_bar_std_history);
        rRoll=(ListView)findViewById(R.id.listViewStdRollForHistory);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_groups);
        rRoll.setAdapter(arrayAdapter);
    }
}