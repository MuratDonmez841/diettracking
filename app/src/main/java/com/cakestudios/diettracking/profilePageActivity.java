package com.cakestudios.diettracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;

public class profilePageActivity extends AppCompatActivity {
    Button saveProfileButton, createProfileButton;
    TextView backButton, updateButton, infoProfile;
    EditText genderEditText,ageEditText,weightEditText,lenghtEditText,illnessesEditText,useMedicineEditText;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    User user;  boolean isActive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Initilazion();
        //FireBaseden User Bilgileri Çekilme fonksiyonu.
        getDataFromFireBase(firebaseUser.getUid());

         createProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibleVisibleFirstLogin();
            }
        });

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender=genderEditText.getText().toString();
                String age=ageEditText.getText().toString();
                String weight=weightEditText.getText().toString();
                String lenght=lenghtEditText.getText().toString();
                String illennes=illnessesEditText.getText().toString();
                String useMedicine=useMedicineEditText.getText().toString();
                createProfileSetFireBase(gender,age,weight,lenght,illennes,useMedicine,user);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMenuPass();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateVars();
            }
        });

    }

    private void updateVars() {
        if(isActive()){
            disEnableEditText(true);
            saveProfileButton.setVisibility(View.VISIBLE);
            Toast.makeText(profilePageActivity.this , getString(R.string.updateVars),Toast.LENGTH_SHORT).show();
            setActive(false);
        }else {
            disEnableEditText(false);
            saveProfileButton.setVisibility(View.GONE);
            setActive(true);
        }
    }

    private void selectMenuPass() {
        Intent intent=new Intent(profilePageActivity.this,selectMenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void setUserToEditText(User user) {
        infoProfile.setText(getString(R.string.user)+user.getUserName());
        ageEditText.setText(String.valueOf(user.getAge()));
        genderEditText.setText(user.getGender());
        weightEditText.setText(String.valueOf(user.getWeight()));
        lenghtEditText.setText(String.valueOf(user.getLenght()));
        illnessesEditText.setText(user.getIllennes());
        useMedicineEditText.setText(user.getUseMedicine());
        disEnableEditText(false);
    }

    private void disEnableEditText(boolean is) {
        ageEditText.setEnabled(is);
        genderEditText.setEnabled(is);
        weightEditText.setEnabled(is);
        lenghtEditText.setEnabled(is);
        illnessesEditText.setEnabled(is);
        useMedicineEditText.setEnabled(is);
    }

    private void createProfileSetFireBase(String gender, String age, String weight, String lenght, String illennes, String useMedicine, User user)
    {
        user.setFirstLogin(false);
        user.setAge(Integer.parseInt(age));
        user.setGender(gender);
        //boy ve kilo değerlerinin .(nokta) kullanımında kod hata vermemesi için if koşulunu ekliyoruz
        if(!weight.contains("\\."))
        user.setWeight(Integer.parseInt(weight));
        if(!lenght.contains("\\."))
            user.setLenght(Integer.parseInt(lenght));
        user.setIllennes(illennes);
        user.setUseMedicine(useMedicine);
        String id=firebaseUser.getUid();
        saveDataFireBase(id,user);
    }


    public void getDataFromFireBase(String id){
        final ProgressDialog progressDialog=getProgressDialog();
        DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("User").child(id);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               user =dataSnapshot.getValue(User.class);
                progressDialog.dismiss();
                //çekilen User bilgilerinde eğer ilk giriş yapıyorsa Profil oluştur butonu hariç diğer inputları Gizliyoruz
                //eğer ilk giriş değilse tamtersini yapıyoruz
                if(user.isFirstLogin()){
                    setVisibleGoneFirstLogin();
                }else{
                    setVisibleGone();
                    setUserToEditText(user);
                }
             }
           @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private ProgressDialog getProgressDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(profilePageActivity.this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        return progressDialog;
    }
    //Database, alınan input bilgilerini setliyororuz
    private void saveDataFireBase(String id,User user) {
        DatabaseReference ddRef = FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("User").child(id);
        ddRef.setValue(user);
        Toast.makeText(profilePageActivity.this , getString(R.string.createProfileToast),Toast.LENGTH_LONG).show();
        selectMenuPass();
    }

    private void setVisibleVisibleFirstLogin() {
        infoProfile.setVisibility(View.GONE);
        createProfileButton.setVisibility(View.GONE);
        saveProfileButton.setVisibility(View.VISIBLE);
        genderEditText.setVisibility(View.VISIBLE);
        ageEditText.setVisibility(View.VISIBLE);
        weightEditText.setVisibility(View.VISIBLE);
        lenghtEditText.setVisibility(View.VISIBLE);
        illnessesEditText.setVisibility(View.VISIBLE);
        useMedicineEditText.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.VISIBLE);
    }

    private void setVisibleGone() {
        createProfileButton.setVisibility(View.GONE);
        saveProfileButton.setVisibility(View.GONE);
    }

    private void setVisibleGoneFirstLogin() {
        saveProfileButton.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        updateButton.setVisibility(View.GONE);
        genderEditText.setVisibility(View.GONE);
        ageEditText.setVisibility(View.GONE);
        weightEditText.setVisibility(View.GONE);
        lenghtEditText.setVisibility(View.GONE);
        illnessesEditText.setVisibility(View.GONE);
        useMedicineEditText.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        updateButton.setVisibility(View.GONE);
      }

    private void Initilazion() {
        genderEditText=findViewById(R.id.gender);
        ageEditText =findViewById(R.id.age);
        weightEditText=findViewById(R.id.weight);
        lenghtEditText=findViewById(R.id.lenght);
        illnessesEditText=findViewById(R.id.illnesses);
        useMedicineEditText=findViewById(R.id.useMedicine);
        backButton =findViewById(R.id.backButton);
        updateButton=findViewById(R.id.updateProfile);
        saveProfileButton=findViewById(R.id.saveProfile);
        createProfileButton=findViewById(R.id.createProfile);
        infoProfile=findViewById(R.id.infoProfile);
        ///////////////////////////////////////
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        setActive(true);
    }

    @Override
    public void onBackPressed() {
        selectMenuPass();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}