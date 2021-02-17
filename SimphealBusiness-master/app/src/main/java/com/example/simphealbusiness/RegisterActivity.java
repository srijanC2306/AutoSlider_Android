package com.example.simphealbusiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText mEmail,mFull_name,mPassword, mphone;
    private FirebaseAuth mAuth;
    private TextView Signin,mbtn_reg;
    private ProgressBar progressbar;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mEmail=findViewById(R.id.user_email);
        mFull_name=findViewById(R.id.reg_user_name);
        mPassword=findViewById(R.id.user_password);
        mphone = findViewById(R.id.userphone);
        mAuth = FirebaseAuth.getInstance();
        Signin=findViewById(R.id.signin_pg);
        mbtn_reg=findViewById(R.id.btn_reg);
        progressbar=findViewById(R.id.progress);

        mbtn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();

                String phone = mphone.getText().toString();
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email address required");
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

                if(phone.length() < 10){
                    mphone.setError("Phone must have 10 digits");
                    return;
                }
                progressbar.setVisibility(View.VISIBLE);
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(mFull_name.getText().toString().matches("") || !mEmail.getText().toString().trim().matches(emailPattern))
                {
                    Toast.makeText(getApplicationContext(), "Enter all details; follow proper format !", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                databaseReference = FirebaseDatabase.getInstance().getReference("customer");
                                databaseReference.child(mAuth.getUid()).child("name").setValue(mFull_name.getText().toString());
                                databaseReference.child(mAuth.getUid()).child("email").setValue(mEmail.getText().toString());
                                databaseReference.child(mAuth.getUid()).child("phone").setValue(mphone.getText().toString());


                                Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
                                Log.i("UID", "onComplete: " + mAuth.getUid());
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));


                            } else {
                                Toast.makeText(RegisterActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressbar.setVisibility(View.GONE);
                            }

                        }
                    });

                }

            }
        });

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , LoginActivity.class));
            }
        });


    }
}