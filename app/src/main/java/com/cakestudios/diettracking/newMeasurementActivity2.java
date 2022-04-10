package com.cakestudios.diettracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class newMeasurementActivity2 extends AppCompatActivity {
    EditText morningEditText,noonEditText,nightEditText,snackEditText;
    TextView totalButton, viewTotal;
    Button saveButton,backButton;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_measurement2);
        Initilazion();

        //newMeasurementActivity den gelen verileri alıyoruz.
        final String isHungry=getIntent().getExtras().getString("isHungry");
        final String varMeasurement=getIntent().getExtras().getString("varMeasurement");
        final String note=getIntent().getExtras().getString("note");
        final String date=getIntent().getExtras().getString("date");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String morningCalori=morningEditText.getText().toString();
                String noonCalori=noonEditText.getText().toString();
                String nightCalori=nightEditText.getText().toString();
                String snackCalori=snackEditText.getText().toString();
                int totalCalori=getTotalCalori(morningCalori,noonCalori,snackCalori,nightCalori);
                saveMesurementFireBase(isHungry,varMeasurement,note,date,totalCalori);
            }
        });

        totalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String morningCalori=morningEditText.getText().toString();
                String noonCalori=noonEditText.getText().toString();
                String nightCalori=nightEditText.getText().toString();
                String snackCalori=snackEditText.getText().toString();
                int totalCalori=getTotalCalori(morningCalori,noonCalori,snackCalori,nightCalori);
                viewTotal.setText(String.valueOf(totalCalori));
            }
        });


    }
    //Girilen bilgileri tutup FireBase push etmek için Measurement Model oluşturuyoruz
    private void saveMesurementFireBase(String isHungry, String varMeasurement, String note, String date, int totalCalori) {
        String id= getRandomString();
        Measurement measurement=new Measurement(id,isHungry,date,varMeasurement,note,totalCalori);
        saveDataFireBase(firebaseUser.getUid(),measurement);
    }

    // her yeni oluşan Measurement modelini ileride listActivity kullanmak için uniq primaryKey olarak random id üretiyoruz
    private String getRandomString(){
        Random rand = new Random();
        int int1 = rand.nextInt(1000);
        return String.valueOf(int1);
    }

    //FireBase Kayıt için DiyetbetTakip başlığı altında, ölçüm verilerini tutuğumuz belitrmek için
    //Measurement başlığı altında kullanıcı id altına push ediyoruz.
    //Push etmek her öğe için birkey oluşturup o key alına verileri kaydediyor demek.
    private void saveDataFireBase(String id,Measurement measurement) {
        DatabaseReference ddRef = FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("Measurement").child(id);
        ddRef.push().setValue(measurement);
        Toast.makeText(newMeasurementActivity2.this , getString(R.string.measurementToast),Toast.LENGTH_LONG).show();
        lisPass();
    }

    private int getTotalCalori(String morningCalori, String noonCalori, String snackCalori, String nightCalori) {
        int total=0;
        if(!morningCalori.equals("") && !noonCalori.equals("") && !snackCalori.equals("")&& !nightCalori.equals("") )
             total=Integer.parseInt(morningCalori)+Integer.parseInt(noonCalori)+Integer.parseInt(snackCalori)+Integer.parseInt(nightCalori);
        return total;
    }

    private void lisPass() {
        Intent intent=new Intent(newMeasurementActivity2.this,listActivity.class);
        startActivity(intent);
        finish();
    }


    private void Initilazion() {
        morningEditText=findViewById(R.id.morning);
        noonEditText =findViewById(R.id.noon);
        nightEditText=findViewById(R.id.night);
        snackEditText=findViewById(R.id.snack);
        totalButton=findViewById(R.id.totalButton);
        saveButton=findViewById(R.id.saveButton);
        backButton=findViewById(R.id.backButton1);
        viewTotal=findViewById(R.id.total);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent( newMeasurementActivity2.this, selectMenuActivity.class);
        startActivity(intent);
        finish();
    }


}