package com.example.simphealbusiness.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.simphealbusiness.MainActivity;
import com.example.simphealbusiness.R;
import com.example.simphealbusiness.adapter.ItemAdapter;
import com.example.simphealbusiness.model.ItemModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Medicine_listing_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Medicine_listing_fragment extends Fragment {


    TextView shopname, shopaddress, shopemail, shopphone;
    ImageView shopimg;
    public SearchView medsearch;
    private RecyclerView recyclerView;
    //ItemAdapter itemAdapter;
    DatabaseReference databaseReference;

    String medval;
    private ItemAdapter adapter;
    private List<ItemModel> exampleList=new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Medicine_listing_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Medicine_listing_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Medicine_listing_fragment newInstance(String param1, String param2) {
        Medicine_listing_fragment fragment = new Medicine_listing_fragment();
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

        View view = inflater
                .inflate(R.layout.fragment_medicine_listing_fragment,
                        container,
                        false);


        ((MainActivity)getActivity()).checkCart();

        ((MainActivity)getActivity()).checkOrder();
        shopname = view.findViewById(R.id.sdname);
        shopemail = view.findViewById(R.id.sdemail);
        shopaddress = view.findViewById(R.id.sdaddress);
        shopphone = view.findViewById(R.id.sdphone);
        shopimg = view.findViewById(R.id.sdimg);


        String val = getArguments().getString("key");
        Log.i(TAG, "searchactivity: " + val);

        medsearch = view.findViewById(R.id.medsrc);
        medsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.i(TAG, "onQueryTextSubmit: "+query);
                Bundle bundleString = new Bundle();
                bundleString.putString("medkey", query);
                bundleString.putString("key", val);


                Medicine_listing_fragment medicine_listing_fragment = new Medicine_listing_fragment();
                medicine_listing_fragment.setArguments(bundleString);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()

                        .replace(R.id.HomeActivity, medicine_listing_fragment, medicine_listing_fragment.getTag())
                        .commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });








        if(getArguments().getString("medkey") != null){

            fillshopdetails(val);
            medval = getArguments().getString("medkey");
            getMeds(medval, val);


        }else{
            fillshopdetails(val);
            fillExampleList(val);
        }






        return view;
    }

    private void fillshopdetails(String val) {

        DatabaseReference shopref = FirebaseDatabase.getInstance()
                .getReference()
                .child("medicineProfile")
                .child(val);

        shopref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                shopname.setText(snapshot.child("shopName").getValue().toString());
                shopemail.setText(snapshot.child("email").getValue().toString());
                shopphone.setText(snapshot.child("phone").getValue().toString());
                shopaddress.setText(snapshot.child("address").getValue().toString());

                String picture = snapshot.child("picture").getValue().toString();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage
                        .getReferenceFromUrl("gs://smplmedicalapp-408ea.appspot.com/Stores/")
                        .child(picture);
                File file = null;
                try {
                    file = File.createTempFile("image", "jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                File finalFile = file;
                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
                        shopimg.setImageBitmap(bitmap);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMeds(String medval, String val) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String curuser = user.getUid();
        String url = "https://smplmedicalapp-408ea-default-rtdb.firebaseio.com"; //https://smplmedicalapp-408ea-default-rtdb.firebaseio.com
        databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(url)
                .child("items").child(val);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    Log.i("demo", ds.getKey());


                    String name=ds.child("name").getValue().toString();
                    String company=ds.child("company").getValue().toString();
                    String image=ds.child("image").getValue().toString();
                    String discount=ds.child("discount").getValue().toString();
                    //String dprice=ds.child("dprice").getValue().toString();
                    String price=ds.child("price").getValue().toString();
                    String qty=ds.child("qty").getValue().toString();
                    String size=ds.child("size").getValue().toString();
                    //for cart
                    String umedID = ds.getKey();
                    String storeID = val;



                    ItemModel item = new ItemModel(name, company, image,
                            price,  size, qty, discount, umedID, storeID);
                    exampleList.add(item);
                }
                RecyclerView recyclerView = getView().findViewById(R.id.shopmedrclv);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new
                        LinearLayoutManager(getContext());
                adapter = new ItemAdapter(exampleList);
                recyclerView.setLayoutManager(layoutManager);
                adapter.getFilter().filter(medval);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void fillExampleList(String val) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String curuser = user.getUid();
        String url = "https://smplmedicalapp-408ea-default-rtdb.firebaseio.com"; //https://smplmedicalapp-408ea-default-rtdb.firebaseio.com
        databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(url)
                .child("items").child(val);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    Log.i("demo", ds.getKey());


                    String name=ds.child("name").getValue().toString();
                    String company=ds.child("company").getValue().toString();
                    String image=ds.child("image").getValue().toString();
                    String discount=ds.child("discount").getValue().toString();
                    //String dprice=ds.child("dprice").getValue().toString();
                    String price=ds.child("price").getValue().toString();
                    String qty=ds.child("qty").getValue().toString();
                    String size=ds.child("size").getValue().toString();
                    //for cart
                    String umedID = ds.getKey();
                    String storeID = val;



                    ItemModel item = new ItemModel(name, company, image,
                            price,  size, qty, discount, umedID, storeID);
                    exampleList.add(item);
                }
                RecyclerView recyclerView = getView().findViewById(R.id.shopmedrclv);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new
                        LinearLayoutManager(getContext());
                adapter = new ItemAdapter(exampleList);
                recyclerView.setLayoutManager(layoutManager);
                // adapter.getFilter().filter(val);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}