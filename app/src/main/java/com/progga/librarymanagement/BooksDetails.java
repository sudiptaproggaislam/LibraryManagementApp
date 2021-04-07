package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BooksDetails extends AppCompatActivity {

    private String category,name;
    private Toolbar mToolbar;
    private EditText bookName,bookcategory,author,publisher,edition,isbn,page;
    private Button updateBtn,deleteBtn;

    private RadioGroup radioGroup;

    private RadioButton availablity,unavailablity;
    private DatabaseReference refRetrive,refUpdate,refDelete;
    final HashMap<String, String> bookDetailsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_details);

        mToolbar=(Toolbar)findViewById(R.id.books_details_activity_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Books Details");

        initialization();
        refRetrive= FirebaseDatabase.getInstance().getReference().child("Books Category").child(category).child(name);
        refDelete= FirebaseDatabase.getInstance().getReference().child(category).child(name);
        refUpdate= FirebaseDatabase.getInstance().getReference().child("Books Category").child(category).child(name);

        refRetrive.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String avail=dataSnapshot.child("avail").getValue().toString();

                if(avail.equals("Unavailable")) {
                    unavailablity.setChecked(true);
                    availablity.setChecked(false);
                }else{
                    availablity.setChecked(true);
                    unavailablity.setChecked(false);
                }

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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String avail;
                if(unavailablity.isChecked()){
                    avail=unavailablity.getText().toString();
                }else{
                    avail=availablity.getText().toString();
                }
                bookDetailsMap.put("name",bookName.getText().toString());
                bookDetailsMap.put("category",bookcategory.getText().toString());
                bookDetailsMap.put("author",author.getText().toString());
                bookDetailsMap.put("publisher",publisher.getText().toString());
                bookDetailsMap.put("edition",edition.getText().toString());
                bookDetailsMap.put("isbn",isbn.getText().toString());
                bookDetailsMap.put("pages",page.getText().toString());
                bookDetailsMap.put("avail",avail);
                refUpdate.setValue(bookDetailsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(BooksDetails.this,"Book data is Updated",Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(BooksDetails.this,"Book data is not updated."+ "\n"+message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void initialization() {
        category=getIntent().getExtras().getString("category");
        name=getIntent().getExtras().getString("name");
        Toast.makeText(BooksDetails.this,category+": "+name,Toast.LENGTH_SHORT).show();

        availablity=(RadioButton)findViewById(R.id.rbAvailable);
        unavailablity=(RadioButton)findViewById(R.id.rbUnavailable);


        updateBtn=(Button)findViewById(R.id.btnUpdateBook);

        bookName=(EditText)findViewById(R.id.etBookName);
        bookcategory=(EditText)findViewById(R.id.etCategoryName);
        author=(EditText)findViewById(R.id.etAuthorName);
        publisher=(EditText)findViewById(R.id.etPublisherName);
        edition=(EditText)findViewById(R.id.etEdition);
        isbn=(EditText)findViewById(R.id.etIsbnNumber);
        page=(EditText)findViewById(R.id.etPages);
    }
}