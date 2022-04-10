package com.cakestudios.diettracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class listActivity extends AppCompatActivity {
    TextView userNameTextView;
    ListView listView;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    // static tanımladım çünkü showActivitiy de seçilen sütünün bilgilerini göstermek için
    //gönderdiğim id ile for içinde arama yaparak Measurement object alıyoruz.
    //tekrar FireBaseden verileri çekebilirdim ama internetten veri çekmek biraz yavaş olduğu için bu yöntemi kullandım
    //showActivitiy kullanak için import ediyoruz.
    static  List<Measurement> measurementList =new ArrayList<Measurement>();
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Initilazion();

        //tüm kayırtlı verileri çekiyoruz. Burada önemli olan kaydederken verdiğimiz yolu düzgün bir şekilde yazmak
        getAllMeasurement(firebaseUser.getUid());
        //yalnızcsa UserName kısmını çekiyoruz
        getUserNameFromFireBase(firebaseUser.getUid());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(listActivity.this, showListActivity.class);
                    i.putExtra("id", measurementList.get(position).getId());
                    startActivity(i);
                    finish();
            }
        });

    }

    private void getUserNameFromFireBase(String id) {
       DatabaseReference dbRef;
       dbRef = FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("User").child(id).child("userName");

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userName =dataSnapshot.getValue(String.class);
                    userNameTextView.setText(userName);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

    }
    private ProgressDialog getProgressDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(listActivity.this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        return progressDialog;
    }

    private void getAllMeasurement(String id) {
       final ProgressDialog progressDialog=getProgressDialog();
        measurementList.clear();
           DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("Measurement").child(id);
            ValueEventListener frbsvalue=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = ds.child("id").getValue(String.class);
                        String isHungry = ds.child("isHungry").getValue(String.class);
                        String date = ds.child("date").getValue(String.class);
                        String varMesurement = ds.child("varMesurement").getValue(String.class);
                        String note=ds.child("note").getValue(String.class);
                        Integer totalCalori=ds.child("totalCalori").getValue(Integer.class);
                        System.out.println("id :" + id);
                        if(id !=null){
                            measurementList.add(new Measurement(id,isHungry,date,varMesurement,note,totalCalori));//
                        }

                    }

                    //Listview Adepte etme kodu customAdapter ismi de java kodunu yazıp onun içinde
                    //ona özel .xml kısmınıda oluşturup bağlama ve gösterme işlmeleerini onun içinde yapıyoruz
                    customAdapter mAdapter = new customAdapter(listActivity.this,measurementList);
                    listView.setAdapter(mAdapter);
                    progressDialog.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbRef.addValueEventListener(frbsvalue);
    }

    private void Initilazion() {
        userNameTextView=findViewById(R.id.listUserName);
        listView =findViewById(R.id.listview);
        ///////////////////////////////////////
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent( listActivity.this, selectMenuActivity.class);
        startActivity(intent);
        finish();
    }

}