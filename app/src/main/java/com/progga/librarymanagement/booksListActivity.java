package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class booksListActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_books_list);

        mToolbar=(Toolbar)findViewById(R.id.books_List_activity_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Books List");

        innitialization();

    }

    private void innitialization() {
        booksCategory=getIntent().getExtras().getString("category");
        reference= FirebaseDatabase.getInstance().getReference("Books Category").child(booksCategory);
        refUpload= FirebaseDatabase.getInstance().getReference("Books Category").child(booksCategory);
        recyclerView=findViewById(R.id.rv);
        searchView=findViewById(R.id.searchView);
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
                        AdapterClass adapterClass=new AdapterClass(list);
                        recyclerView.setAdapter(adapterClass);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(booksListActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
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
        AdapterClass adapterClass=new AdapterClass(mylist);
        recyclerView.setAdapter(adapterClass);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.addbutton,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.add_button){
            AlertDialog.Builder builder= new AlertDialog.Builder(booksListActivity.this,R.style.AlertDialog);
            builder.setTitle("Add Book-");

            final EditText setCategory=new EditText(booksListActivity.this);
            setCategory.setHint("e.g OOP");
            builder.setView(setCategory);

            builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String booksNames=setCategory.getText().toString();
                    if (TextUtils.isEmpty(booksNames)){
                        Toast.makeText(booksListActivity.this,"Please write a category name...",Toast.LENGTH_SHORT).show();
                    }else {
                        bookMap.put("name",booksNames);
                        bookMap.put("category",booksCategory);
                        bookMap.put("author"," ");
                        bookMap.put("publisher"," ");
                        bookMap.put("edition"," ");
                        bookMap.put("pages"," ");
                        bookMap.put("isbn"," ");
                        bookMap.put("avail","Unavailable");
                        refUpload.child(booksNames).setValue(bookMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(booksListActivity.this,booksNames+" is Uploaded",Toast.LENGTH_SHORT).show();
                                } else {
                                    String message = task.getException().toString();
                                    Toast.makeText(booksListActivity.this,booksNames+" is not Uploaded"+ "\n"+message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

        return true;
    }
}