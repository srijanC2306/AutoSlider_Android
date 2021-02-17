package com.example.simphealbusiness.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simphealbusiness.LoginActivity;
import com.example.simphealbusiness.MainActivity;
import com.example.simphealbusiness.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profilefragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profilefragment extends Fragment {

    EditText name, phone, email;
    TextView resetpass, editacc, logout;

    private DatabaseReference creference;
    private FirebaseAuth mAuth;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profilefragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profilefragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Profilefragment newInstance(String param1, String param2) {
        Profilefragment fragment = new Profilefragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profilefragment, container, false);

        ((MainActivity)getActivity()).checkCart();

        ((MainActivity)getActivity()).checkOrder();

        resetpass = view.findViewById(R.id.reset_password);
        editacc = view.findViewById(R.id.edit_prof);
        logout = view.findViewById(R.id.logout);

        name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        phone = view.findViewById(R.id.user_phone);

        email.setFocusable(false);

        name.setFocusable(false);
        /*
        phone.setFocusable(false);
        editacc.setClickable(false);*/
        editacc.setText("Edit Profile");





        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


//                Toast.makeText(getContext(), "Click `Edit Profile` to save details",
//                        Toast.LENGTH_SHORT).show();
                editacc.setClickable(true);


            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });


        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                Toast.makeText(getContext(), "Click `Edit Profile` to save details",
//                        Toast.LENGTH_SHORT).show();
                editacc.setClickable(true);


            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });


        editacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    editaccount();
                    name.clearFocus();
                    phone.clearFocus();




            }
        });


        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()

                        .replace(R.id.HomeActivity, resetPasswordFragment, resetPasswordFragment.getTag())
                        .commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LogOut();
            }
        });


        getAccdetails();

        return view;
    }

    private void LogOut() {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);

    }

    private void editaccount(){


        mAuth = FirebaseAuth.getInstance();
        String getuid = mAuth.getCurrentUser().getUid();

        creference = FirebaseDatabase.getInstance().getReference()
                .child("customer").child(getuid);


        creference.child("name").setValue(name.getText().toString());
        creference.child("phone").setValue(phone.getText().toString());


        Toast.makeText(getContext(), "Profile Updated ", Toast.LENGTH_SHORT).show();

        editacc.setText("Edit Profile");


    }

    private void getAccdetails() {

        mAuth = FirebaseAuth.getInstance();
        String getuid = mAuth.getCurrentUser().getUid();

        creference = FirebaseDatabase.getInstance().getReference()
                .child("customer").child(getuid);

        creference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());
                phone.setText(snapshot.child("phone").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}