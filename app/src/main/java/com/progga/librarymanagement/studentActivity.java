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
import java.util.HashMap;

public class studentActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView name,roll,dept,series;
    private ListView iBook,rBook;

    private ArrayAdapter<String> iAdapter;
    private ArrayList<String> iList=new ArrayList<>();
    private ArrayAdapter<String> rAdapter;
    private ArrayList<String> rList=new ArrayList<>();

    private DatabaseReference refIssueBooks,refReturnBook,refRetriveData;
    final HashMap<String, String> stdInfo = new HashMap<>();
    final HashMap<String, String> issueInfo = new HashMap<>();
    final HashMap<String, String> returnInfo = new HashMap<>();

    private String r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        mToolbar=(Toolbar)findViewById(R.id.std_details_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Student Details");
        initialization();

        r=getIntent().getExtras().getString("roll");
        refRetriveData= FirebaseDatabase.getInstance().getReference().child("All Student List").child(r);
        refIssueBooks= FirebaseDatabase.getInstance().getReference().child("All Student List").child(r).child("Taken Book");
        refReturnBook= FirebaseDatabase.getInstance().getReference().child("All Student List").child(r).child("history");

        refRetriveData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue().toString());
                roll.setText(dataSnapshot.child("roll").getValue().toString());
                series.setText(dataSnapshot.child("series").getValue().toString());
                dept.setText(dataSnapshot.child("department").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        name=(TextView)findViewById(R.id.stdDetails_name);
        roll=(TextView)findViewById(R.id.stdDetails_roll);
        dept=(TextView)findViewById(R.id.stdDetails_department);
        series=(TextView)findViewById(R.id.stdDetails_series);
        iBook=(ListView)findViewById(R.id.listView_issueBooks);
        rBook=(ListView)findViewById(R.id.listView_returnBooks);

        iAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,iList);
        iBook.setAdapter(iAdapter);

        rAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,rList);
        rBook.setAdapter(rAdapter);
    }
}