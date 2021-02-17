package com.example.simphealbusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();

            Thread thread = new Thread() {

                public void run() {
                    try {
                        sleep(2000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                        checkLogin();
                        /*Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);*/
                    }
                    finish();

                }
            };
            thread.start();
        }
    }

    protected void checkLogin() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(checkInternetConnectivity() == 1) {

            if (firebaseUser == null) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
            //progressBar.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(this, "Internet connection disable!", Toast.LENGTH_SHORT).show();
            //progressBar.setVisibility(View.GONE);
        }
    }
    private int checkInternetConnectivity() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            return 0;
        } else {
            return 1;
        }
    }
}