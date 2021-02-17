package com.example.simphealbusiness.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simphealbusiness.MainActivity;
import com.example.simphealbusiness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {

    EditText oldpass, newpass1, newpass2;

    TextView resetbtn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResetPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
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

        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        ((MainActivity)getActivity()).checkCart();

        ((MainActivity)getActivity()).checkOrder();
        oldpass = view.findViewById(R.id.oldpass);
        newpass1 = view.findViewById(R.id.newpass1);
        newpass2 = view.findViewById(R.id.newpass2);
        resetbtn = view.findViewById(R.id.resetpass);

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassword();
            }
        });


        return view;  }

        private void resetpassword(){



            if(!newpass1.getText().toString().matches(newpass2.getText().toString())){
                Toast.makeText(getContext(), "New passwords don't match !", Toast.LENGTH_SHORT).show();
            }
            else
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference databaseReference = FirebaseDatabase
                        .getInstance().getReference("customer").child(mAuth.getUid());


                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       String checkemail =  snapshot.child("email").getValue().toString();
                      String  checkpass = oldpass.getText().toString();
                        Log.i("email found", checkemail);

                        AuthCredential credential = EmailAuthProvider.getCredential(checkemail, checkpass);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.i("check-user", "User re-authenticated.");
                                if (task.isSuccessful()){
                                    user.updatePassword(newpass2.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Log.i("user-check-pass", "onComplete: updated password");
                                                Toast.makeText(getContext(), "Password Updated", Toast.LENGTH_LONG).show();

                                                Profilefragment profile_fragment = new Profilefragment();
                                                FragmentManager manager = getFragmentManager();
                                                manager.beginTransaction()

                                                        .replace(R.id.HomeActivity, profile_fragment, profile_fragment.getTag())
                                                        .commit();
                                            }
                                            else {
                                                Log.i("user-check-pass", "onFailure: cannot update password");
                                            }
                                        }
                                    });
                                }

                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Failed to authenticate", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.


            }


        }
}