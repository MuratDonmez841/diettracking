package com.cakestudios.diettracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class selectMenuActivity extends AppCompatActivity {
    Button newEntryButton, listButton, profileButton,btnDoctorProfil,btnSearchSick;
    DatabaseReference dbRef;
    String isDoctorCheck="false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDoctor();
    }

    private void InitilazionDoctor() {
        btnDoctorProfil=findViewById(R.id.user_doctor_profile);
        btnSearchSick=findViewById(R.id.search_sick);
    }

    private void listPagePass() {
        Intent intent = new Intent(selectMenuActivity.this, listActivity.class);
        startActivity(intent);
        finish();
    }

    private void newEntryPagePass() {
        Intent intent = new Intent(selectMenuActivity.this, newMeasurementActivity.class);
        startActivity(intent);
        finish();
    }

    private void profilePagePass() {
        Intent intent = new Intent(selectMenuActivity.this, profilePageActivity.class);
        startActivity(intent);
        finish();
    }

    private void Initilazion() {
        newEntryButton = findViewById(R.id.newEntry);
        profileButton = findViewById(R.id.userPage);
        listButton = findViewById(R.id.list);
    }

    //Geri Tuşuna Tıklandığında AlertDialog çıkması için yazılmış kod
    //Kullanıcının yanlışlıkla geri tuşuna bastığında uygulamadan direk çıkmaması için yazıldı.
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(selectMenuActivity.this);
        builder.setTitle(getString(R.string.exit))
                .setMessage(getString(R.string.exitSure))
                .setCancelable(false)
                .setIcon(R.drawable.exit)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.no), null);
        //Creating dialog box
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void isDoctor() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uId = user.getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("UserDoctor").child(uId).child("doctor");
        dbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    String doctor=String.valueOf(task.getResult().getValue());
                    if (!doctor.equals("true")) {
                        setContentView(R.layout.activity_select_menu);
                        Initilazion();
                        profileButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                profilePagePass();
                            }
                        });

                        newEntryButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                newEntryPagePass();
                            }
                        });

                        listButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listPagePass();
                            }
                        });
                    } else {
                        setContentView(R.layout.activity_doctor_select_menu);
                        InitilazionDoctor();
                        btnSearchSick.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(selectMenuActivity.this, SearchSick.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        btnDoctorProfil.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(selectMenuActivity.this, DoctorProfil.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}