package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class booksCategoryActivity extends AppCompatActivity {

    private ListView list;
    private Toolbar mToolbar;
    private DatabaseReference refRetrive,refUpload;
    private SearchView srcRoll;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups=new ArrayList<>();
    private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_category);

        Initialization();
        mToolbar=(Toolbar)findViewById(R.id.books_Category_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Books Category");

        refRetrive= FirebaseDatabase.getInstance().getReference().child("Books Category");
        refUpload= FirebaseDatabase.getInstance().getReference().child("Books Category");

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
                booksCategoryActivity.this.arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                booksCategoryActivity.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void GotoBookListActivity() {
        Intent Intent= new Intent(booksCategoryActivity.this,booksListActivity.class);
        Intent.putExtra("category",category);
        startActivity(Intent);
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
            AlertDialog.Builder builder= new AlertDialog.Builder(booksCategoryActivity.this,R.style.AlertDialog);
            builder.setTitle("Add Category-");

            final EditText setCategory=new EditText(booksCategoryActivity.this);
            setCategory.setHint("e.g Science");
            builder.setView(setCategory);

            builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String booksCategory=setCategory.getText().toString();
                    if (TextUtils.isEmpty(booksCategory)){
                        Toast.makeText(booksCategoryActivity.this,"Please write a category name...",Toast.LENGTH_SHORT).show();
                    }else {
                        refUpload.child(booksCategory).setValue(" ").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(booksCategoryActivity.this,booksCategory+" is Uploaded",Toast.LENGTH_SHORT).show();
                                } else {
                                    String message = task.getException().toString();
                                    Toast.makeText(booksCategoryActivity.this,booksCategory+" is not Uploaded"+ "\n"+message,Toast.LENGTH_SHORT).show();
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

    private void Initialization() {
        list=(ListView)findViewById(R.id.booksCategoryList);
        srcRoll=(SearchView)findViewById(R.id.search_bar_admin_book_category);

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_groups);
        list.setAdapter(arrayAdapter);
    }
}