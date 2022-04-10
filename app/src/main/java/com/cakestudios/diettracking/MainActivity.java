package com.cakestudios.diettracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText;
    TextView passResetTextView, singUptextView;
    Button logInButton;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //Xml dosyasındaki id leri java dosyasında kullanabilmemiz için bağlıyoruz. xml Mapping
            Initilazion();

            logInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Firebase veri yazma veya kullanıcı oluşturma gibi işlemler internet aracılıyla olduğu için
                    //biraz yavaş sürüyor. kullanıcıyı bekletmek için progressDialog oluşturuyoruz.
                    final ProgressDialog progressDialog = getProgressDialog();

                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    //email ve password alanlarının zorunlu dolu olması için nullChecks fonksiyonunu yazıyoruz
                    if (nullChecks(email, getString(R.string.entryEmail)))
                        return;
                    if (nullChecks(password, getString(R.string.entryPassword)))
                        return;
                    //FireBase Authentication özelliğini kullanarak kullanıcı eğer FireBase de var, kayıt olmuş ise giriş yapıyor
                    logInFunction(progressDialog, email, password);
                }
            });


            passResetTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Activity Geçişleri
                    passwordResetPass();
                }
            });

            singUptextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    singUpPass();
                }
            });



    }


    private void logInFunction(final ProgressDialog progressDialog, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String id = user.getUid();
                            progressDialog.dismiss();
                            selectMenuPass();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, getString(R.string.errorLogIn),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });
    }

    private void selectMenuPass() {
        Intent intent=new Intent(MainActivity.this,selectMenuActivity.class);
        startActivity(intent);
        finish();

    }

    private void singUpPass() {
        Intent intent=new Intent(MainActivity.this,singUpActivity.class);
        startActivity(intent);
        finish();
    }
    private void passwordResetPass() {
        Intent intent=new Intent(MainActivity.this,passwordResetActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean nullChecks(String var, String text) {
        if (TextUtils.isEmpty(var)) {
           Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            return true;
       }
       return false;
    }

    private ProgressDialog getProgressDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        return progressDialog;
    }

    private void Initilazion() {
        emailEditText=findViewById(R.id.emailAddress);
        passwordEditText =findViewById(R.id.numberPassword);
        passResetTextView=findViewById(R.id.passwordReset);
        singUptextView=findViewById(R.id.signUp);
        logInButton=findViewById(R.id.logIn);
        //Kaydol Text in altını çizmek için kullanılan kod parçası
        singUptextView.setPaintFlags(singUptextView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        ///////////////////////////////////////
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    //Geri Tuşuna tıklandığında çalışan fonkdsiyon
    @Override
    public void onBackPressed() {
        finish();
    }

}