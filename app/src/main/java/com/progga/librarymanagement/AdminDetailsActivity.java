package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminDetailsActivity extends AppCompatActivity {

    private TextView AdName,AdId,AdPhn,check;
    private Button AdminAccept,AdminDecline;
    private LinearLayout adButtons;
    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private DatabaseReference refAccept,refDelete,ref,refUser;
    private String AdmnIn,currentUserId;
    final HashMap<String, String> profileMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_details);

        InnitializeFields();

        mAuth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference();
        currentUser=mAuth.getCurrentUser();
        currentUserId=mAuth.getCurrentUser().getUid().toString();
        if (currentUserId.equals("AYn231cxNDcX9FLIwCjpGhTCqkw1")){
            adButtons.setVisibility(View.VISIBLE);
        }else{
            adButtons.setVisibility(View.INVISIBLE);
        }

        mToolbar=(Toolbar)findViewById(R.id.reqAdminsDetails);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Admin Details");

        AdmnIn=getIntent().getExtras().getString("AdminID");

        ref= FirebaseDatabase.getInstance().getReference().child("Admin Request").child(AdmnIn);
        refDelete= FirebaseDatabase.getInstance().getReference().child("Admin Request").child(AdmnIn);
        refAccept= FirebaseDatabase.getInstance().getReference().child("Admin Request").child(AdmnIn);
        refUser= FirebaseDatabase.getInstance().getReference().child("User");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profileMap.put("name",dataSnapshot.child("name").getValue().toString());
                profileMap.put("adminId",dataSnapshot.child("adminId").getValue().toString());
                profileMap.put("adminPhn",dataSnapshot.child("adminPhn").getValue().toString());
                profileMap.put("status",dataSnapshot.child("status").getValue().toString());
                profileMap.put("uid",dataSnapshot.child("uid").getValue().toString());

                AdName.setText(profileMap.get("name"));
                AdId.setText(profileMap.get("adminId"));
                AdPhn.setText(profileMap.get("adminPhn"));
                check.setText(dataSnapshot.child("access").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        AdminAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUserId.equals("AYn231cxNDcX9FLIwCjpGhTCqkw1")) {
                    refUser.child(profileMap.get("uid")).child("access").setValue("granted").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                refAccept.child("access").setValue("granted");


                                Toast.makeText(AdminDetailsActivity.this, "Request accepted Successfully...", Toast.LENGTH_LONG).show();
                                //refDelete.removeValue();

                                //SendUserToAddMemberActivity();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(AdminDetailsActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(AdminDetailsActivity.this,"You can't access this feature",Toast.LENGTH_LONG).show();
                }
            }
        });

        AdminDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUserId.equals("AYn231cxNDcX9FLIwCjpGhTCqkw1")) {
                    refDelete.child("access").setValue("Declined").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                refUser.child(profileMap.get("uid")).child("access").setValue("Declined");
                                Toast.makeText(AdminDetailsActivity.this, "Request declined Successfully...", Toast.LENGTH_SHORT).show();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(AdminDetailsActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(AdminDetailsActivity.this,"You can't access this feature",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void InnitializeFields() {
        AdName=(TextView)findViewById(R.id.tvNameofAdmin);
        AdId=(TextView) findViewById(R.id.tvIdOfAdmin);
        AdPhn=(TextView)findViewById(R.id.tvphnOfAdmin);
        check=(TextView)findViewById(R.id.tvCheckAdmin);

        AdminAccept=(Button)findViewById(R.id.acceptAdmin);
        AdminDecline=(Button)findViewById(R.id.declineAdmin);

        adButtons=(LinearLayout)findViewById(R.id.adButtons);
        adButtons.setVisibility(View.INVISIBLE);
    }
}