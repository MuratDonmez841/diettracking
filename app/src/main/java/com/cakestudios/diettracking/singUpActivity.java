package com.cakestudios.diettracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class singUpActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText, passwordConfirmEditText, userNameEditText;
    Button singUpOk;
    CheckBox checkBoxDoctor;
    String email;
    String password;
    String passwordConfirm;
    String userName;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        Initilazion();

        singUpOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                passwordConfirm = passwordConfirmEditText.getText().toString();
                userName = userNameEditText.getText().toString();

                // *********************************************************************************

                //******************************************************************************
                //email formatına uygun olup olmadığını kontrol ediyoruz.
                boolean isEmailFormat = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
                if (!isEmailFormat) {
                    Toast.makeText(getApplicationContext(), getString(R.string.emailFormat), Toast.LENGTH_SHORT).show();
                    return;
                }
                //passwordlar birbiri ile uyuşuyor mu kontrol ediyoruz
                if (!password.equals(passwordConfirm)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.notPassConfirm), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (nullChecks(email, getString(R.string.entryEmail)))
                    return;
                if (nullChecks(password, getString(R.string.entryPassword)) && nullChecks(passwordConfirm, getString(R.string.entryPassword)))
                    return;


                //FireBase Authentication özelliğini kullanarak kullanıcı oluşturuyoruz. oluşturduğumuz kullanıcı otematik bir id
                //üretilir bunu biz primary Key olarak kullanacağız
                singUpFunction(email, password, userName);

            }
        });

    }

    private void singUpFunction(final String email, final String password, final String userName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(singUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String id = user.getUid();
                            //Kullanıcı başarıyla oluşturuldu şimdi Bilgilerini FireBase RealTime DataBase kullanarak kaydediyoruz
                            //kaydetmeyi önceden oluşturduğumuz User modeli üzerinden yapıyoruz
                            //user Modelini input alınan kısımları setliyoruz diğer değişkenleri default olarak biz giriyoruz
                            if (checkBoxDoctor.isChecked()) {
                                fireBaseProcessUserDoctor(id, email, userName);
                            } else {
                                fireBaseProcess(id, email, userName, password);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(singUpActivity.this, getString(R.string.errorLogIn),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void fireBaseProcessUserDoctor(String id, String email, String userName) {
        UserDoctor userDoctor= new UserDoctor(id, userName, email,true,"-");
        saveDataFireBaseDoctor(id,userDoctor);
    }

    private void fireBaseProcess(String id, String email, String userName, String password) {
        User userCreate = new User(id, email, userName, password, "null", 0, 0, 0, "null", "null", true);
        saveDataFireBase(id, userCreate);
    }

    private void saveDataFireBase(String id, User user) {
        //FireBase RealTime DataBase de fveriler Json tipi ile tutulur. burada DiyabetTakip Ana başlığı altında User bilgilerini
        //Tuttuğumuzu belirten User başlığı altında oluşturulan her kullanıcının id aldınta bilgileri commit liyoruz
        DatabaseReference ddRef = FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("User").child(id);
        ddRef.setValue(user);
        Toast.makeText(singUpActivity.this, getString(R.string.successSingUp), Toast.LENGTH_LONG).show();
        mainActivityPass();
    }
    private void saveDataFireBaseDoctor(String id, UserDoctor user) {
        //Aynı işlem sadece doctor için ayırdım.
        DatabaseReference ddRef = FirebaseDatabase.getInstance().getReference().child("DiyetbetTakip").child("UserDoctor").child(id);
        ddRef.setValue(user);
        Toast.makeText(singUpActivity.this, getString(R.string.successSingUp), Toast.LENGTH_LONG).show();
        mainActivityPass();
    }

    private void mainActivityPass() {
        Intent intent = new Intent(singUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    private void Initilazion() {
        emailEditText = findViewById(R.id.singUpEmail);
        passwordEditText = findViewById(R.id.singUpPassword);
        passwordConfirmEditText = findViewById(R.id.singUpPasswordConfirm);
        userNameEditText = findViewById(R.id.singUpUserName);
        singUpOk = findViewById(R.id.singUpOk);
        checkBoxDoctor = findViewById(R.id.checkbox_doctor);


        ///////////////////////////////////////
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    private boolean nullChecks(String var, String text) {
        if (TextUtils.isEmpty(var)) {
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(singUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}