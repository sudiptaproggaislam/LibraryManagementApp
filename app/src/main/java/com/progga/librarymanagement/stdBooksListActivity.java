package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class stdBooksListActivity extends AppCompatActivity {

    private DatabaseReference reference,refUpload;
    ArrayList<deal> list;
    public String booksCategory;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private Toolbar mToolbar;
    final HashMap<String, String> bookMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_books_list);

        mToolbar=(Toolbar)findViewById(R.id.std_books_List_activity_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Books List");

        innitialization();
    }

    private void innitialization() {
        booksCategory=getIntent().getExtras().getString("category");
        reference= FirebaseDatabase.getInstance().getReference("Books Category").child(booksCategory);
        refUpload= FirebaseDatabase.getInstance().getReference("Books Category").child(booksCategory);
        recyclerView=findViewById(R.id.std_rv);
        searchView=findViewById(R.id.std_searchView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (reference!=null){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                        list=new ArrayList<>();
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            list.add(ds.getValue(deal.class));
                        }
                        stdAdapterClass adapterClass=new stdAdapterClass(list);
                        recyclerView.setAdapter(adapterClass);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(stdBooksListActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String str){

        ArrayList<deal>mylist=new ArrayList<>();

        for(deal object : list){

            String x= object.getName();
            if(x.toLowerCase().contains(str.toLowerCase())){
                mylist.add(object);
            }

        }
        stdAdapterClass adapterClass=new stdAdapterClass(mylist);
        recyclerView.setAdapter(adapterClass);

    }

}