package com.example.simphealbusiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText mEmail,mPassword;
    private TextView Sign_up_txt,Forgot_text_pass,Login_btn;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


       // checkLogin();

        mAuth = FirebaseAuth.getInstance();
        mEmail=findViewById(R.id.user_email);
        mPassword=findViewById(R.id.user_password);
        Login_btn=findViewById(R.id.btn_login);
        Sign_up_txt=findViewById(R.id.signup_pg);
        Forgot_text_pass=findViewById(R.id.reset_pass);
        progressbar=findViewById(R.id.progressbar);


        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=mEmail.getText().toString().trim();
                String password= mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email address required.");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password required");
                    return;
                }
                if (password.length() <6){
                    mPassword.setError("Password must have 6 charecters");
                    return;
                }

                progressbar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                } else {
                                    Toast.makeText(LoginActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.GONE);

                                }
                                finish();
                            }
                        });
            }
        });

        Sign_up_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , RegisterActivity.class));
            }
        });

        Forgot_text_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText resetmail =new EditText(view.getContext());
                final AlertDialog.Builder passwprdresetdialog=new AlertDialog.Builder(view.getContext());
                passwprdresetdialog.setTitle("reset password?");
                passwprdresetdialog.setMessage("Enter your email address to reset the password.");
                passwprdresetdialog.setView(resetmail);

                passwprdresetdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String mail= resetmail.getText().toString().trim();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset link sent to your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error!Reset link is not sent"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

                passwprdresetdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                passwprdresetdialog.create().show();
            }
        });




    }



}