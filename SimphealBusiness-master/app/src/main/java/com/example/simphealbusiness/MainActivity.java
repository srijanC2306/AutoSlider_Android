package com.example.simphealbusiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.simphealbusiness.fragments.Cartfragment;
import com.example.simphealbusiness.fragments.Homefragment;
import com.example.simphealbusiness.fragments.Ordersfragment;
import com.example.simphealbusiness.fragments.Profilefragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public TextView badgeview, badgeview2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // newFragment(new Homefragment());


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                ((BottomNavigationView.OnNavigationItemSelectedListener) this);


        BottomNavigationMenuView bottomNavigationMenuView
                = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(1);

        View v2 = bottomNavigationMenuView.getChildAt(2);

        BottomNavigationItemView itemView
                = (BottomNavigationItemView) v;
        BottomNavigationItemView itemView2
                = (BottomNavigationItemView) v2;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.badge_layout, itemView, true);
        View badge2 = LayoutInflater.from(this)
                .inflate(R.layout.badge_layout2, itemView2, true);



        badgeview = findViewById(R.id.notifications_badge);
        badgeview.setVisibility(View.INVISIBLE);

        badgeview2 = findViewById(R.id.notifications_badge2);
        badgeview2.setVisibility(View.INVISIBLE);



        final Handler handler = new Handler();
        final int delay = 15000; // 1000 milliseconds == 1 second


        handler.postDelayed(new Runnable() {

            int secondsAlive = 0;
            public void run() {
                Log.i("check cart & order", String.valueOf(secondsAlive));
                secondsAlive+=delay;
                checkCart();
                checkOrder();
                handler.postDelayed(this, delay);
            }
        }, delay);

//        checkCart();
//        checkOrder();

        newFragment(new Homefragment());


    }


    public void checkCart() {



        badgeview.setVisibility(View.INVISIBLE);
        final int[] finalcount = {0};

        DatabaseReference countreference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("userorders");

        countreference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usnap: snapshot.getChildren()){

                    DatabaseReference odrReference = countreference.child(usnap.getKey());

                    odrReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot osnap : snapshot.getChildren()){
                                DatabaseReference medref = odrReference.child(osnap.getKey());

                                //Log.i("medref ", osnap.getKey());
                                medref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot medIDsnap: snapshot.getChildren()){


                                            if(String.valueOf(medIDsnap.child("ustatus").getValue()).matches("CART")  &&
                                                    String.valueOf(medIDsnap.child("UID").getValue()).matches(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            ){

                                                Log.i("medID status ", String.valueOf(medIDsnap.child("ustatus").getValue()));
                                                finalcount[0] =5;
                                                badgeview.setVisibility(View.VISIBLE);
                                                break;
                                            }
                                            Log.i("count check", String.valueOf(finalcount[0]));

                                            medIDsnap.getChildrenCount();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

//        Log.i("final count", String.valueOf(count));

    }

    public void checkOrder() {


        badgeview2.setVisibility(View.INVISIBLE);
        final int[] finalcount = {0};

        DatabaseReference countreference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("userorders");

        countreference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usnap: snapshot.getChildren()){

                    DatabaseReference odrReference = countreference.child(usnap.getKey());

                    odrReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot osnap : snapshot.getChildren()){
                                DatabaseReference medref = odrReference.child(osnap.getKey());

                                //Log.i("medref ", osnap.getKey());
                                medref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot medIDsnap: snapshot.getChildren()){


                                            if(!String.valueOf(medIDsnap.child("ustatus").getValue()).matches("CART")  &&
                                                    String.valueOf(medIDsnap.child("UID").getValue()).matches(FirebaseAuth
                                                            .getInstance()
                                                            .getCurrentUser()
                                                            .getUid())
                                            ){

                                                Log.i("medID status ", String.valueOf(medIDsnap.child("ustatus").getValue()));
                                                finalcount[0] =5;
                                                badgeview2.setVisibility(View.VISIBLE);
                                                break;
                                            }
                                            Log.i("count check", String.valueOf(finalcount[0]));

                                            medIDsnap.getChildrenCount();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

//        Log.i("final count", String.valueOf(count));

    }


    private boolean newFragment(Fragment fragment) {

        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.HomeActivity, fragment).commit();
            return true;

        }
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.app_home:
                fragment = new Homefragment();
                break;
            case R.id.cart:
                fragment = new Cartfragment();
                break;
            case R.id.profile:
                fragment = new Profilefragment();
                break;
            case R.id.order:
                fragment = new Ordersfragment();
                break;
        }
        return newFragment(fragment);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}