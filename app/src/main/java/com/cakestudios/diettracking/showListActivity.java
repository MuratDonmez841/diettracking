package com.cakestudios.diettracking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
//bir Önceki sayfadan çekilen liste
import static com.cakestudios.diettracking.listActivity.measurementList;

public class showListActivity extends AppCompatActivity {
    TextView varMeasurementTextView,isHungryTextView,noteTextView,totalCaloriTextView,dateTextView;
    Button backButton, deleteButton;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        Initilazion();

        //seşilen kısmın uniq id si
        final String id=getIntent().getExtras().getString("id");
        //tüm listden id si gelen id ile eşit olanı çekiyoruz
        Measurement measurement=getMeasurement(id);

        //verileri TextView yazıyoruz
        setTextViews(measurement);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPass();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogShow(firebaseUser.getUid(),id);
            }
        });

    }

    //Kullanıya silmesi için pop-up menü açıyoruz
    private void alertDialogShow(final String uid, final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(showListActivity.this);
        builder.setTitle(getString(R.string.deleteRecord))
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteVar(uid,id);
                    }
                })
                .setNegativeButton(getString(R.string.no),null);
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    private void deleteVar(String uid,final String id) {
        //FireBase Datanbese Delete fonksiyonunu kullanıyoruz. buraada verilen yol çok önemli yanlışlıklla full veriler silinebilir.
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query applesQuery = ref.child("DiyetbetTakip").child("Measurement").child(uid);
            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        String id1=appleSnapshot.child("id").getValue(String.class);
                        if(id1.equals(id)) {
                            appleSnapshot.getRef().removeValue();
                            Toast.makeText(showListActivity.this,getString(R.string.deleteToast),Toast.LENGTH_SHORT).show();
                            listPass();
                        }else {

                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("TAG", databaseError.getMessage(), databaseError.toException());
                }
            });

        }

    }

    private void listPass() {
        Intent intent=new Intent(showListActivity.this,listActivity.class);
        startActivity(intent);
        finish();
    }

    private void setTextViews(Measurement measurement) {
        varMeasurementTextView.setText(measurement.getVarMesurement());
        isHungryTextView.setText(measurement.getIsHungry());
        noteTextView.setText(measurement.getNote());
        totalCaloriTextView.setText(String.valueOf(measurement.getTotalCalori()));
        dateTextView.setText(measurement.getDate());
    }

    private Measurement getMeasurement(String id) {
        for(Measurement measurement:measurementList){
            if(measurement.getId().equals(id)){
               return measurement;
            }
        }
        return null;
    }

    private void Initilazion() {
        varMeasurementTextView=findViewById(R.id.textView6);
        isHungryTextView =findViewById(R.id.textView8);
        noteTextView=findViewById(R.id.textView10);
        totalCaloriTextView=findViewById(R.id.textView12);
        dateTextView=findViewById(R.id.showDate);
        backButton =findViewById(R.id.showBack);
        deleteButton=findViewById(R.id.deleteButton);
        ///////////////////////////////////////
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {
       listPass();
    }
}