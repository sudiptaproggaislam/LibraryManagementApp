package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class historyActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ListView iBook,rBook;

    private ArrayAdapter<String> iAdapter;
    private ArrayList<String> iList=new ArrayList<>();
    private ArrayAdapter<String> rAdapter;
    private ArrayList<String> rList=new ArrayList<>();

    private DatabaseReference refIssueBooks,refReturnBook;
    private String r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mToolbar=(Toolbar)findViewById(R.id.history_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Student Details");
        initialization();

        r=getIntent().getExtras().getString("roll");
        refIssueBooks= FirebaseDatabase.getInstance().getReference().child("All Student List").child(r).child("Taken Book");
        refReturnBook= FirebaseDatabase.getInstance().getReference().child("All Student List").child(r).child("history");

        refIssueBooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                iList.clear();
                for (DataSnapshot dss: dataSnapshot.getChildren()){
                    //r=ds.getValue(ret.class);
                    iList.add(dss.child("book").getValue().toString()+" was issued at "+dss.child("issuedate").getValue().toString()+ " and have to return within "+dss.child("returndate").getValue().toString());

                }

                iBook.setAdapter(iAdapter);
                iAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refReturnBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //r=ds.getValue(ret.class);
                    rList.add(ds.child("book").getValue().toString()+" was issued at "+ds.child("issuedate").getValue().toString()+ " and returned at "+ds.child("returndate").getValue().toString());

                }

                rBook.setAdapter(rAdapter);
                rAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialization() {
        iBook=(ListView)findViewById(R.id.listView_issueBooks_history);
        rBook=(ListView)findViewById(R.id.listView_returnBooks_history);

        iAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,iList);
        iBook.setAdapter(iAdapter);

        rAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,rList);
        rBook.setAdapter(rAdapter);
    }
}