package com.cakestudios.diettracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Objects;

public class SearchSick extends AppCompatActivity {
    TextView varMeasurementTextView,isHungryTextView,noteTextView,totalCaloriTextView,dateTextView;
    Button backButton, btnFill;
    EditText editSearch;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    String searchSickId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sick);
        Initilazion();
        btnFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=editSearch.getText().toString();
                if (!TextUtils.isEmpty(text))
                searchSick(text);
                else
                    Toast.makeText(SearchSick.this, "İsim giriniz!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void searchSick(final String Name) {
        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mFirebaseDatabaseReference.child("DiyetbetTakip").child("User");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Iterator<DataSnapshot> items = snapshot.getChildren().iterator();
                    while (items.hasNext()) {
                        DataSnapshot item = items.next();
                        String name = Objects.requireNonNull(item.child("userName").getValue()).toString();
                        if (name.equals(Name)){
                            searchSickId=item.getRef().getKey();
                            fill();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fill() {
        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mFirebaseDatabaseReference.child("DiyetbetTakip").child("Measurement").child(searchSickId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String date = null;
                    String varMesurement = null;
                    String isHungry=null;
                    String note=null;
                    String totalCalori=null;
                    Iterator<DataSnapshot> items = snapshot.getChildren().iterator();
                    while (items.hasNext()) {
                        DataSnapshot item = items.next();
                        date = Objects.requireNonNull(item.child("date").getValue()).toString();
                        varMesurement=Objects.requireNonNull(item.child("varMesurement").getValue()).toString();
                        isHungry=Objects.requireNonNull(item.child("isHungry").getValue()).toString();
                        note=Objects.requireNonNull(item.child("note").getValue()).toString();
                        totalCalori=Objects.requireNonNull(item.child("totalCalori").getValue()).toString();
                    }
                    dateTextView.setText(date);
                    varMeasurementTextView.setText(varMesurement);
                    isHungryTextView.setText(isHungry);
                    noteTextView.setText(note);
                    totalCaloriTextView.setText(totalCalori);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Initilazion() {
        varMeasurementTextView=findViewById(R.id.textView6);
        isHungryTextView =findViewById(R.id.textView8);
        noteTextView=findViewById(R.id.textView10);
        totalCaloriTextView=findViewById(R.id.textView12);
        dateTextView=findViewById(R.id.showDate);
        backButton =findViewById(R.id.showBack);
        btnFill=findViewById(R.id.deleteButton);
        editSearch=findViewById(R.id.edit_search);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }
}