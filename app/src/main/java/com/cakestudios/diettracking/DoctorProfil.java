package com.cakestudios.diettracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class DoctorProfil extends AppCompatActivity {

    EditText editTextHospital,editTextAddDoctorSick;
    Button btnBack,btnAdd,btnAddHospital;
    TextView txtDoctorName,txtAddSicktext;
    ListView listViewSicks;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userId;
    ArrayAdapter adapter;
    ArrayList<String> sickList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profil);
        Initilazion();
        doctorName();
        setSicks();
        setHospital();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),selectMenuActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addSick();
            }
        });
        btnAddHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHospital();
            }
        });
        listViewSicks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            String name=listViewSicks.getItemAtPosition(position).toString();
                            deleteSick(name);
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorProfil.this);
                builder.setMessage("Kişiyi silmek istediğinize emin misiniz?").setNegativeButton("Sil",dialogClickListener).setPositiveButton("Kapat", dialogClickListener).show();

                return false;
            }
        });

    }

    private void setHospital() {
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("UserDoctor").child(userId);
        dbRef.child("hospitalDoctor").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    editTextHospital.setText(String.valueOf(task.getResult().getValue()));
                }

            }
        });
    }

    private void deleteSick(final String Name) {
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("DoctorSick").child(userId);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Iterator<DataSnapshot> items = snapshot.getChildren().iterator();
                    while (items.hasNext()) {
                        DataSnapshot item = items.next();
                        String name = Objects.requireNonNull(item.child("sickName").getValue()).toString();
                        if (name.equals(Name)){
                           item.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addHospital() {
        String hospitalName=editTextHospital.getText().toString();
        if(!TextUtils.isEmpty(hospitalName)){
            HashMap<String,Object> hospitalMap = new HashMap<>();
            hospitalMap.put("hospitalDoctor",hospitalName);
            DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("UserDoctor").child(userId);
            dbRef.updateChildren(hospitalMap);
        }
    }

    private void setSicks() {
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("DoctorSick").child(userId);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    listDisplay(snapshot);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listDisplay(DataSnapshot snapshot) {
        adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,sickList);
        listViewSicks.setAdapter(adapter);
        sickList.clear();
        Iterator<DataSnapshot> items = snapshot.getChildren().iterator();
        while (items.hasNext()) {
            DataSnapshot item = items.next();
            String name = Objects.requireNonNull(item.child("sickName").getValue()).toString();
            sickList.add(name);
        }

    }

    private void doctorName() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("UserDoctor").child(userId);
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    txtDoctorName.setText("Doktor İsmi: "+String.valueOf(Objects.requireNonNull(task.getResult()).child("userName").getValue()));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DoctorProfil.this, "İnternet bağlantınızı kontrol ediniz!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Initilazion() {
        editTextAddDoctorSick=findViewById(R.id.edit_txt_add_doctor_sick);
        editTextHospital=findViewById(R.id.txt_hospital);
        btnAdd=findViewById(R.id.btn_edit_doctor);
        listViewSicks=findViewById(R.id.lv_doctor);
        btnBack=findViewById(R.id.btn_back_doctor);
        txtDoctorName=findViewById(R.id.doctor_name_txt);
        txtAddSicktext=findViewById(R.id.txt_add_sick_text);
        btnAddHospital=findViewById(R.id.btn_edit_doctor_hospital);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        userId=user.getUid();
    }
    public void addSick(){
        String name=editTextAddDoctorSick.getText().toString();
        if (!TextUtils.isEmpty(name)){
            dbRef=FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("DoctorSick").child(userId);
            String key=dbRef.push().getKey();
            HashMap<String, Object> keyMap = new HashMap<>();
            dbRef.updateChildren(keyMap);
            DatabaseReference mapRef=dbRef.child(key);
            HashMap<String, Object> nameMap = new HashMap<>();
            nameMap.put("sickName",name);
            mapRef.updateChildren(nameMap);
            Toast.makeText(this, "Hasta ekleme başarılı!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Hasta ismi boş kalamaz!", Toast.LENGTH_SHORT).show();
        }
    }


}