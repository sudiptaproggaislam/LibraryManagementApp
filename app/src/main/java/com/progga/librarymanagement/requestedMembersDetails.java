package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class requestedMembersDetails extends AppCompatActivity {
    private TextView stdname,stdroll,stdseries,stddept,check;
    private Button stdaccept,stddecline;
    private Toolbar mToolbar;

    private DatabaseReference refAccept,refDelete,ref,refAccept2,reff;
    private String roll;
    final HashMap<String, String> profileMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_members_details);
        InnitializeFields();

        mToolbar=(Toolbar)findViewById(R.id.reqMembersDetails);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Students Details");

        roll=getIntent().getExtras().getString("roll");
        ref= FirebaseDatabase.getInstance().getReference().child("Request Student List").child(roll);
        refDelete= FirebaseDatabase.getInstance().getReference().child("Request Student List").child(roll);
        refAccept= FirebaseDatabase.getInstance().getReference().child("Student");
        refAccept2= FirebaseDatabase.getInstance().getReference().child("All Student List").child(roll);
        reff=FirebaseDatabase.getInstance().getReference();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profileMap.put("department",dataSnapshot.child("department").getValue().toString());
                profileMap.put("name",dataSnapshot.child("name").getValue().toString());
                profileMap.put("roll",dataSnapshot.child("roll").getValue().toString());
                profileMap.put("series",dataSnapshot.child("series").getValue().toString());
                profileMap.put("status",dataSnapshot.child("status").getValue().toString());
                profileMap.put("uid",dataSnapshot.child("uid").getValue().toString());
                profileMap.put("request","Accepted");

                stdname.setText(profileMap.get("name"));
                stdroll.setText(profileMap.get("roll"));
                stdseries.setText(profileMap.get("series"));
                stddept.setText(profileMap.get("department"));
                check.setText(dataSnapshot.child("access").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        stdaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refAccept.child(profileMap.get("department")).child(profileMap.get("series")).child(profileMap.get("roll")).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            refAccept2.setValue(profileMap);

                            reff.child("User").child(profileMap.get("uid")).child("access").setValue("granted");


                            Toast.makeText(requestedMembersDetails.this, "Request accepted Successfully...", Toast.LENGTH_LONG).show();
                            refDelete.removeValue();

                            //SendUserToAddMemberActivity();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(requestedMembersDetails.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        stddecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refDelete.child("access").setValue("Declined").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            reff.child("User").child(profileMap.get("uid")).child("access").setValue("Declined");
                            Toast.makeText(requestedMembersDetails.this, "Request declined Successfully...", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(requestedMembersDetails.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });




    }

    private void InnitializeFields() {
        stdname=(TextView)findViewById(R.id.name);
        stdroll=(TextView)findViewById(R.id.roll);
        stdseries=(TextView)findViewById(R.id.series);
        stddept=(TextView)findViewById(R.id.department);
        stdaccept=(Button) findViewById(R.id.accept);
        stddecline=(Button)findViewById(R.id.decline);
        check=(TextView)findViewById(R.id.tvCheck);


    }
    private void SendUserToAddMemberActivity() {
        Intent reqMemberIntent= new Intent(requestedMembersDetails.this,addMembersActivity.class);
        startActivity(reqMemberIntent);
    }
}