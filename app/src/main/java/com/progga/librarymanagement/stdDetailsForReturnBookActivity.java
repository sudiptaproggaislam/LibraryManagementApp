package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class stdDetailsForReturnBookActivity extends AppCompatActivity {

    private  String roll;
    private Toolbar mToolbar;
    private DatabaseReference refStd,refAdmin,refStdChange,refHistory;
    private Button retbook;
    private ListView list;
    private TextView stdDetails;
    final HashMap<String, String> historyMap = new HashMap<>();

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups=new ArrayList<>();
    private ArrayList<ret> category=new ArrayList<>();
    private String bookName,bookKey,issueStatus,bookCategory,iDate,rDate,rrDate;
    private ret r=new ret();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_details_for_return_book);

        mToolbar=(androidx.appcompat.widget.Toolbar)findViewById(R.id.std_details_returnBook_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Return Book");

        initialization();

        retriveAnddisplayBookCategory();

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        rDate=simpleDateFormat.format(Calendar.getInstance().getTime());
        //issueDate.setText(iDate);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bookName=category.get(position).getBook().toString();
                bookKey=category.get(position).getKey().toString();
                issueStatus=category.get(position).getStatus().toString();
                bookCategory=category.get(position).getCategory().toString();
                iDate=category.get(position).getIssuedate().toString();
                rrDate=category.get(position).getReturndate().toString();
                stdDetails.setText("Status:"+ issueStatus+"\nCategory:"+ bookCategory+"\nBook Name:"+ bookName+"\nIssue Date:"+ iDate+"\nReturn Date:"+ rrDate);
                refStdChange=FirebaseDatabase.getInstance().getReference().child("All Student List").child(roll).child("Taken Book").child(bookKey);
                refAdmin=FirebaseDatabase.getInstance().getReference().child("Books Category").child(bookCategory).child(bookName).child("Taken Book").child(roll);
                refHistory=FirebaseDatabase.getInstance().getReference().child("All Student List").child(roll).child("history").child(bookKey);
                historyMap.put("book",bookName);
                historyMap.put("category",bookCategory);
                historyMap.put("issuedate",iDate);
                historyMap.put("returndate",rDate);
                historyMap.put("key",bookKey);
                historyMap.put("roll",roll);
                historyMap.put("status","returned");
            }
        });

        retbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refStdChange.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            refAdmin.removeValue();
                            refHistory.setValue(historyMap);
                            Toast.makeText(stdDetailsForReturnBookActivity.this,"Returned",Toast.LENGTH_SHORT).show();
                            stdDetails.setText("Select Again");
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(stdDetailsForReturnBookActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void retriveAnddisplayBookCategory() {
        refStd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               /* Set<String> set=new HashSet<>();
                Iterator iterator=dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()){

                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_groups.clear();
                list_of_groups.addAll(set);*/
                list_of_groups.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    r=ds.getValue(ret.class);
                    list_of_groups.add(r.getBook());
                    category.add(r);

                }

                list.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialization() {

        roll=getIntent().getExtras().getString("roll");
        refStd= FirebaseDatabase.getInstance().getReference().child("All Student List").child(roll).child("Taken Book");


        retbook=(Button)findViewById(R.id.btnRetBook);
        list=(ListView)findViewById(R.id.lv_return_book_details);
        stdDetails=(TextView)findViewById(R.id.tv_issue_details);

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_groups);
        list.setAdapter(arrayAdapter);

    }
}