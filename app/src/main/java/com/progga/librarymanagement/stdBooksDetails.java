package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class stdBooksDetails extends AppCompatActivity {

    private String category,name;
    private Toolbar mToolbar;
    private TextView bookName,bookcategory,author,publisher,edition,isbn,page,availab;

    private DatabaseReference refRetrive;
    final HashMap<String, String> bookDetailsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_books_details);


        mToolbar=(Toolbar)findViewById(R.id.std_books_details_activity_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Books Details");

        initialization();
        refRetrive= FirebaseDatabase.getInstance().getReference().child("Books Category").child(category).child(name);


        refRetrive.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                availab.setText(dataSnapshot.child("avail").getValue().toString());
                bookName.setText(dataSnapshot.child("name").getValue().toString());
                bookcategory.setText(dataSnapshot.child("category").getValue().toString());
                author.setText(dataSnapshot.child("author").getValue().toString());
                publisher.setText(dataSnapshot.child("publisher").getValue().toString());
                edition.setText(dataSnapshot.child("edition").getValue().toString());
                isbn.setText(dataSnapshot.child("isbn").getValue().toString());
                page.setText(dataSnapshot.child("pages").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialization() {
        category=getIntent().getExtras().getString("category");
        name=getIntent().getExtras().getString("name");
        Toast.makeText(stdBooksDetails.this,category+": "+name,Toast.LENGTH_SHORT).show();



        bookName=(TextView) findViewById(R.id.stdetBookName);
        bookcategory=(TextView) findViewById(R.id.stdetCategoryName);
        author=(TextView) findViewById(R.id.stdetAuthorName);
        publisher=(TextView) findViewById(R.id.stdetPublisherName);
        edition=(TextView) findViewById(R.id.stdetEdition);
        isbn=(TextView) findViewById(R.id.stdetIsbnNumber);
        page=(TextView) findViewById(R.id.stdetPages);
        availab=(TextView)findViewById(R.id.stdAvailablityCheck);
    }
}