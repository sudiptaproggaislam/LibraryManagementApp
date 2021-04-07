package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference ref,refStdRoll,refAdminAccess;
    public String UserStatus,UserName,category,currentUserId,currentUserGmail,stdRoll;

    private RelativeLayout adminsLayout, studentsLayout;
    private LinearLayout booksCategory, bookName, issueBook,returnbook,addMember,adminReq,bookListStdView,historyStdView;

    private TextView adminName,adminEmail,stdName,stdEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialization();

        mAuth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference();
        currentUser=mAuth.getCurrentUser();

        mToolbar=(Toolbar)findViewById(R.id.main_activity_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Library Management");
    }
    private void Initialization() {
        adminsLayout=(RelativeLayout)findViewById(R.id.adminsUI);
        studentsLayout=(RelativeLayout)findViewById(R.id.studentsUI);
        booksCategory=(LinearLayout)findViewById(R.id.btnBookCategory);
        bookName=(LinearLayout)findViewById(R.id.btnBooksName);
        issueBook=(LinearLayout)findViewById(R.id.btnIssueBook);
        returnbook=(LinearLayout)findViewById(R.id.btnreturnBook);
        addMember=(LinearLayout)findViewById(R.id.btnAddmember);
        adminReq=(LinearLayout)findViewById(R.id.btnAdminReq);
        bookListStdView=(LinearLayout)findViewById(R.id.btnbookliststdView);
        historyStdView=(LinearLayout)findViewById(R.id.btnHistoryStdView);
        adminName=(TextView)findViewById(R.id.tvadminName);
        adminEmail=(TextView)findViewById(R.id.tvadminEmail);
        stdName=(TextView)findViewById(R.id.tvStudentName);
        stdEmail=(TextView)findViewById(R.id.tvStudentEmail);

    }
    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser!=null && currentUser.isEmailVerified()){
            VerifyUserExistance();
        }else {
            LoginActivity();
        }
    }

    private void  VerifyUserExistance() {
        currentUserId=mAuth.getCurrentUser().getUid();
        currentUserGmail=mAuth.getCurrentUser().getEmail();

        ref.child("User").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").exists()){
                    if(dataSnapshot.child("access").getValue().toString().equals("granted")) {
                        UserStatus = dataSnapshot.child("status").getValue().toString();
                        UserName = dataSnapshot.child("name").getValue().toString();
                        adminName.setText(UserName);
                        adminEmail.setText(currentUserGmail);
                        stdEmail.setText(currentUserGmail);
                        stdName.setText(UserName);
                        ///Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        if (UserStatus.equals("Student")) {
                           // stdRoll= dataSnapshot.child("roll").getValue().toString();
                            adminsLayout.setVisibility(View.INVISIBLE);

                            bookListStdView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SendUserToBookListStdActivity();
                                }
                            });
                            historyStdView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SendUserToHistoryStdActivity();
                                }
                            });
                        } else {
                            studentsLayout.setVisibility(View.INVISIBLE);

                            /*refAdminAccess = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
                            refAdminAccess.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String ac = dataSnapshot.child("access").getValue().toString();
                                    if (ac.equals("granted")) {
                                        adminsLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        adminsLayout.setVisibility(View.INVISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });*/
                            booksCategory.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SendUserToBooksCategoryActivity();
                                }
                            });
                            bookName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SendUserToBookNameActivity();
                                }
                            });
                            issueBook.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SendUserToIssueBookActivity();
                                }
                            });

                            returnbook.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SendUserToReturnBookActivity();
                                }
                            });

                            addMember.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SendUserToAddMemberActivity();
                                }
                            });
                            adminReq.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SendUserToAdminReqActivity();
                                }
                            });
                        }
                    }else{
                        adminsLayout.setVisibility(View.INVISIBLE);
                        studentsLayout.setVisibility(View.INVISIBLE);

                        Toast.makeText(MainActivity.this,"Admin doesn't accept your request",Toast.LENGTH_LONG).show();
                    }
                }else{
                    SendUserToSettingsActivity();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_items,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_logout_option){
            mAuth.signOut();
            LoginActivity();
        }

        return true;
    }
    private void SendUserToHistoryStdActivity() {
        refStdRoll=FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
        refStdRoll.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stdRoll=dataSnapshot.child("roll").getValue().toString();
                Intent historyIntent= new Intent(MainActivity.this,historyActivity.class);
                historyIntent.putExtra("roll",stdRoll);
                startActivity(historyIntent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void SendUserToBookListStdActivity() {
        Intent addMemberIntent= new Intent(MainActivity.this,categoryListViewStdActivity.class);
        startActivity(addMemberIntent);
    }

    private void SendUserToAdminReqActivity() {
        Intent adminIntent= new Intent(MainActivity.this,AdminRequestActivity.class);
        startActivity(adminIntent);
    }

    private void SendUserToAddMemberActivity() {
        Intent addMemberIntent= new Intent(MainActivity.this,addMembersActivity.class);
        startActivity(addMemberIntent);
    }

    private void SendUserToReturnBookActivity() {
        Intent addMemberIntent= new Intent(MainActivity.this,returnBookActivity.class);
        startActivity(addMemberIntent);
    }

    private void SendUserToIssueBookActivity() {
        Intent issuebook=new Intent(MainActivity.this,issueBookActivityNew.class);
        startActivity(issuebook);
    }

    private void SendUserToBookNameActivity() {
        Intent booksCategory=new Intent(MainActivity.this,booksCategoryActivity.class);
        startActivity(booksCategory);
    }

    private void SendUserToBooksCategoryActivity() {
        Intent stdList=new Intent(MainActivity.this,studentListActivity.class);
        startActivity(stdList);
    }

    private void LoginActivity() {
        Intent loginIntent= new Intent(MainActivity.this,loginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void SendUserToSettingsActivity() {
        Intent settingsIntent= new Intent(MainActivity.this,settingsActivity.class);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
        finish();
    }
}