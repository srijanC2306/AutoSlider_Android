package com.example.simphealbusiness.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;


import com.example.simphealbusiness.MainActivity;
import com.example.simphealbusiness.R;
import com.example.simphealbusiness.model.shopModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.EmptyNode;
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
 * Use the {@link Homefragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Homefragment extends Fragment {


    public SearchView shopsearch;
    public String finalshopkey;

    private List<shopModel> shoplist=new ArrayList<>();


    private ShopAdapter newadapter;
    DatabaseReference databaseReference;
    //private shopAdapter myadapter;
   // TextView checkID;
    RecyclerView recyclerView;

    private List<shopModel> list = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Homefragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Homefragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Homefragment newInstance(String param1, String param2) {
        Homefragment fragment = new Homefragment();
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
                .inflate(R.layout.fragment_homefragment, container, false);

        ((MainActivity)getActivity()).checkCart();

        ((MainActivity)getActivity()).checkOrder();

        if(getArguments()!= null){
        String shopval = getArguments().getString("shopsrc");
            Log.i(TAG, "searchactivity: " + shopval);

            getshopsrc(shopval);

        }else{

            getallshops();
        }




         shopsearch = view.findViewById(R.id.shopsrc);
        shopsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.i(TAG, "onQueryTextSubmit: "+query);
                Bundle bundleString = new Bundle();
                bundleString.putString("shopsrc", query);


                Homefragment homefragment = new Homefragment();
                homefragment.setArguments(bundleString);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()

                        .replace(R.id.HomeActivity, homefragment, homefragment.getTag())
                        .commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });




        return view;
    }


    private void getshopsrc(String shopnamefilter) {



        databaseReference = FirebaseDatabase
                .getInstance()
                .getReferenceFromUrl("https://smplmedicalapp-408ea-default-rtdb.firebaseio.com")
                .child("medicineProfile");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds1: snapshot.getChildren()){

                    shopModel model = ds1.getValue(shopModel.class);

                    String shopname = model.getShopName();
                    String address = model.getAddress();
                    String shopimg = model.getPicture();
                    String shopkey = model.getShopkey();

                    if (shopname.toLowerCase().contains(shopnamefilter.toLowerCase())) {
                        shopModel allshops =
                                new shopModel(address, shopname, shopimg, shopkey);
                        shoplist.add(allshops);
                    }
                }


                recyclerView = getView().findViewById(R.id.shoprclv);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager =
                        new LinearLayoutManager(getContext());
                newadapter = new ShopAdapter(shoplist);
                recyclerView.setLayoutManager(layoutManager);
                //adapter.getFilter().filter(val);
                recyclerView.setAdapter(newadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
    private void getallshops() {



        databaseReference = FirebaseDatabase
                .getInstance()
                .getReferenceFromUrl("https://smplmedicalapp-408ea-default-rtdb.firebaseio.com")
                .child("medicineProfile");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds1: snapshot.getChildren()){

                    shopModel model = ds1.getValue(shopModel.class);

                    String shopname = model.getShopName();
                    String address = model.getAddress();
                    String shopimg = model.getPicture();
                    String shopkey = model.getShopkey();

                    shopModel allshops =
                            new shopModel(address, shopname, shopimg, shopkey);

                    shoplist.add(allshops);



                }


                recyclerView = getView().findViewById(R.id.shoprclv);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager =
                        new LinearLayoutManager(getContext());
                newadapter = new ShopAdapter(shoplist);
                recyclerView.setLayoutManager(layoutManager);
                //adapter.getFilter().filter(val);
                recyclerView.setAdapter(newadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.shopviewHolder>{




        private List<shopModel> list;

        public ShopAdapter(List<shopModel> list){

            this.list = list;
        }

        @NonNull
        @Override
        public shopviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view =
                    LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_shop, parent, false);
            return new shopviewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull shopviewHolder holder, int position) {

            shopModel model = list.get(position);
            holder.shopname.setText(model.getShopName());
            holder.shopaddress.setText(model.getAddress());

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage
                    .getReferenceFromUrl("gs://smplmedicalapp-408ea.appspot.com/Stores/")
                    .child(model.getPicture());

            File file = null;
            try {
                file = File.createTempFile("image", "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

            File finalfile = file;


            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(finalfile.getAbsolutePath());
                    holder.shop_image.setImageBitmap(bitmap);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    DatabaseReference shopkeyref =
                            FirebaseDatabase.getInstance()
                            .getReference().child("medicineProfile");
                    shopkeyref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot keyds: snapshot.getChildren()){

                                DatabaseReference keyref2 = shopkeyref.child(keyds.getKey());

                                shopModel temp = keyds.getValue(shopModel.class);

                                Log.i("shopname", model.getShopName());

                                if(temp.getShopName().equals(model.getShopName())){

                                    finalshopkey = keyref2.getKey();
                                    Log.i("key ds", keyref2.getKey());


                    Log.i("get key", finalshopkey);


                    Bundle bundleString = new Bundle();
                    bundleString.putString("key", finalshopkey);

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Medicine_listing_fragment medicineListingFragment = new Medicine_listing_fragment();
                    medicineListingFragment.setArguments(bundleString);
                    activity.getSupportFragmentManager()
                            .beginTransaction().replace(R.id.HomeActivity, medicineListingFragment)
                            .addToBackStack(null).commit();

                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




//                    Log.i("get key", finalshopkey);
//
//
//                    Bundle bundleString = new Bundle();
//                    bundleString.putString("key", finalshopkey);
//
//                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                    Medicine_listing_fragment medicineListingFragment = new Medicine_listing_fragment();
//                    medicineListingFragment.setArguments(bundleString);
//                    activity.getSupportFragmentManager()
//                            .beginTransaction().replace(R.id.HomeActivity, medicineListingFragment)
//                            .addToBackStack(null).commit();


                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class shopviewHolder extends RecyclerView.ViewHolder{

            ImageView shop_image;
            TextView shopname,shopaddress;
            public RelativeLayout relativeLayout;


            public shopviewHolder(@NonNull View itemView) {
                super(itemView);

                shop_image=itemView.findViewById(R.id.shop_pic);
                shopname=itemView.findViewById(R.id.shop_name);
                shopaddress=itemView.findViewById(R.id.shop_address);
                relativeLayout = itemView.findViewById(R.id.shop_layout);


            }
        }
    }



}