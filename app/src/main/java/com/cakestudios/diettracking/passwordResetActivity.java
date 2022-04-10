package com.cakestudios.diettracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class passwordResetActivity extends AppCompatActivity {
    EditText passResetEmailEditText;
    TextView infoTextView;
    Button passResetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        Initilazion();

        passResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=passResetEmailEditText.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.entryEmail), Toast.LENGTH_SHORT).show();
                    return;
                }
                resetUserPassword(email);

            }
        });
    }

    private void resetUserPassword(String email) {
        {
            //FireBase Authentication özelliğinde kendi resetpassword fonksiyonunu kullanıyoruz.
            //Burada girilen Email eğer kayıtlı ise E- Postasına activasyon linki gönderiyor
            //ve linke tıklandığında şifre değiştirme ekranına yönlendirilip şifrrenizi değiştirebiliyorsunuz
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            final ProgressDialog progressDialog = getProgressDialog();
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                infoTextView.setText(getString(R.string.infoResetPass));
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.errorPassReset), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            });
        }

    }

    private void Initilazion() {
        passResetEmailEditText=findViewById(R.id.passResetEmail);
        passResetButton =findViewById(R.id.passwordResetButton);
        infoTextView =findViewById(R.id.infoResetPass);
    }

    private ProgressDialog getProgressDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(passwordResetActivity.this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        return progressDialog;
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent( passwordResetActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}