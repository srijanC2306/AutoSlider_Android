package com.example.simphealbusiness.fragments;

import android.app.Dialog;
import android.appwidget.AppWidgetHost;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simphealbusiness.MainActivity;
import com.example.simphealbusiness.R;
import com.example.simphealbusiness.model.OrderModel;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cartfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cartfragment extends Fragment {

    DatabaseReference databaseReference;
    //private RecyclerView recyclerView;

    public int flag ;
    CardView cardViewbill;
    double a1=0.0;
    double tx=0.0;
    double a2=0.0;
    private OrderAdapter userorderAdapter;
   // private int flag = 0;
    private TextView placeorder, tamount, ttax, toamount;



    private EditText uaddress;
    public TextView nbadge;
    private List<OrderModel> orderlist=new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Cartfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cartfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Cartfragment newInstance(String param1, String param2) {
        Cartfragment fragment = new Cartfragment();
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
        View view = inflater.inflate(R.layout.fragment_cartfragment, container, false);

        //((MainActivity)getActivity()).checkCart();

        ((MainActivity)getActivity()).checkOrder();


        View view2 = getActivity().findViewById(R.id.HomeActivity);
        nbadge = view2.findViewById(R.id.notifications_badge);
        nbadge.setVisibility(View.INVISIBLE);

        tamount = view.findViewById(R.id.totalamount);
        ttax = view.findViewById(R.id.totaltax);
        toamount = view.findViewById(R.id.totalodramount);

        cardViewbill = view.findViewById(R.id.billview);
        cardViewbill.setVisibility(View.INVISIBLE);


        uaddress = view.findViewById(R.id.u_address);

        uaddress.setVisibility(View.INVISIBLE);

        placeorder = view.findViewById(R.id.buy_btn);

        placeorder.setVisibility(View.INVISIBLE);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!uaddress.getText().toString().matches(""))
                    order(uaddress.getText().toString());
                else
                    Toast.makeText(getContext(), "Enter your address !", Toast.LENGTH_SHORT).show();

            }
        });
        List<String> uodrlist = new ArrayList<String>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        String url = "https://smplmedicalapp-408ea-default-rtdb.firebaseio.com/";
        databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(url)
                .child("userorders")
                .child(uid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot2 : snapshot.getChildren()){

                    DatabaseReference medref = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl(url).child("userorders")
                            .child(uid)
                            .child(dataSnapshot2.getKey());

                    medref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot snapshot3: snapshot.getChildren()){
                                Log.i("check keys 3", snapshot3.getKey());



                                OrderModel orderModel = snapshot3.getValue(OrderModel.class);

                                if(orderModel.getUstatus().matches("CART")){




                                    Log.i("curuser", String.valueOf(user));
                                    String name = orderModel.getName();
                                    String mName = orderModel.getMedicineName();
                                    String address = orderModel.getAddress();
                                    String amount = orderModel.getAmount();
                                    String phone = orderModel.getPhone();
                                    String tax = orderModel.getTax();
                                    String quantity = orderModel.getQuantity();


                                    DecimalFormat df = new DecimalFormat();
                                    df.setMaximumFractionDigits(2);
                                    String total = (df.format(Float.parseFloat(amount) + Float.parseFloat(tax)));
                                    String orderID = dataSnapshot2.getKey();
                                    String storeId = orderModel.getStoreId();
                                    String umedID = orderModel.getUmedID();
                                    String UID = orderModel.getUID();
                                    String ustatus = orderModel.getUstatus();


                                    a1 = a1 + Float.parseFloat(amount);
                                    tx = tx + Float.parseFloat(tax);
                                    a2 = a2 + a1 + tx;




                                    Log.i("storeId", storeId);

                                    Log.i("childval3//amount:-- ", String.valueOf(orderModel.getAmount()));

                                    OrderModel item = new OrderModel(mName, quantity, amount, address,
                                            name, phone, tax, total, orderID, storeId, umedID, UID, ustatus);
                                    orderlist.add(item);

                                    Log.i("orderlist value :--", String.valueOf(orderlist));

                                    placeorder.setVisibility(View.VISIBLE);
                                    uaddress.setVisibility(View.VISIBLE);

                                    cardViewbill.setVisibility(View.VISIBLE);

                                }





                            }




                            RecyclerView recyclerView = view.findViewById(R.id.add_item_cart);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            userorderAdapter = new OrderAdapter(orderlist);
                            recyclerView.setLayoutManager(layoutManager);
                            //adapter.getFilter().filter(val);
                            recyclerView.setAdapter(userorderAdapter);
                            flag = 1;
                            DecimalFormat df = new DecimalFormat("0.00");
                            tamount.setText((df.format(a1)));
                            ttax.setText(df.format(tx));
                            toamount.setText(df.format(a1+tx));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            Toast.makeText(getContext(), "Cart is empty !", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




       /* recyclerView = view.findViewById(R.id.add_item_cart);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        String url = "https://smplmedicalapp-408ea-default-rtdb.firebaseio.com";

        String curuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartref = FirebaseDatabase
                .getInstance()
                .getReferenceFromUrl(url)
                .child("userorders").child(curuser);
        Log.i(TAG, cartref.getKey());


*/


        return view;
    }



    private void order(String getaddress) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        String url = "https://smplmedicalapp-408ea-default-rtdb.firebaseio.com/";
        databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(url)
                .child("userorders")
                .child(uid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot2 : snapshot.getChildren()){

                    DatabaseReference medref = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl(url).child("userorders")
                            .child(uid)
                            .child(dataSnapshot2.getKey());

                    medref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot snapshot3: snapshot.getChildren()){
                                Log.i("check keys 3", snapshot3.getKey());

                                OrderModel orderModel = snapshot3.getValue(OrderModel.class);

                                if(orderModel.getUstatus().matches("CART")){


                                    DatabaseReference update = medref.child(snapshot3.getKey());

//                                    orderModel.setAddress("hello");
//                                    orderModel.setUstatus("ORDERED");

                                    update.child("address").setValue(getaddress);
                                    update.child("ustatus").setValue("ORDERED");

                                    Log.i("test", orderModel.getName());

                                }

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

        Toast.makeText(getContext(), "Order complete. Return to Home tab", Toast.LENGTH_SHORT).show();

        Homefragment homefragment = new Homefragment();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()

                .replace(R.id.HomeActivity, homefragment, homefragment.getTag())
                .commit();


    }



    public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.orderViewHolder>  {


        private List<OrderModel> list;

        public OrderAdapter(List<OrderModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public OrderAdapter.orderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //Context context = parent.getContext();
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
            return new OrderAdapter.orderViewHolder(v);
        }

        @Override
        public  void onBindViewHolder(@NonNull OrderAdapter.orderViewHolder holder, int position) {

            OrderModel currentItem = list.get(position);


            //final int index = holder.getAdapterPosition();

            holder.quantity.setText(list.get(position).getQuantity());
            //holder.address.setText(list.get(position).getAddress());
            holder.medicineName.setText(list.get(position).getMedicineName());
            //holder.name.setText(list.get(position).getName());
        /*holder.amount.setText(list.get(position).getAmount());
        holder.tax.setText(list.get(position).getTax());*/

            //holder.amount.setText("");
            //holder.tax.setText("");
            //holder.orderid.setText(list.get(position).getOrderID());
            float amount = Float.parseFloat(list.get(position).getAmount());
            float tax = Float.parseFloat(list.get(position).getTax());
            float total = amount + tax;
            holder.changeOdr.setText("Remove");


            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            holder.total.setText("₹"+df.format(total));
            //holder.phone.setText(list.get(position).getPhone());



            String medID= currentItem.getUmedID();


            DatabaseReference imgref = FirebaseDatabase.getInstance().getReference().child("items").child(currentItem.getStoreId()).child(medID);

            imgref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String imgname =  snapshot.child("image").getValue().toString();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage
                            .getReferenceFromUrl("gs://smplmedicalapp-408ea.appspot.com/Images/")
                            .child(imgname);
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
                            holder.img.setImageBitmap(bitmap);
                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





            if(currentItem.getUstatus().matches("ORDERED")){
                holder.changeOdr.setVisibility(View.INVISIBLE);
            }
            holder.changeOdr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uorderID = currentItem.getOrderID();

                    Log.i(TAG, uorderID);


                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("userorders")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(uorderID)
                            .removeValue();


                    Toast.makeText(v.getContext(), "Item removed from cart!", Toast.LENGTH_LONG)
                            .show();

                    Cartfragment cartfragment = new Cartfragment();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction()

                            .replace(R.id.HomeActivity, cartfragment, cartfragment.getTag())
                            .commit();





            /*    FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(this).attach(this).commit();
*/

/*
                String umedID = currentItem.getUmedID();
                String orderStoreID = currentItem.getStoreId();
                String UID = currentItem.getUID();



                DatabaseReference uidref = FirebaseDatabase
                        .getInstance()
                        .getReferenceFromUrl("https://smplmedicalapp" +
                                "-408ea-default-rtdb.firebaseio.com/" +
                                "userorders")
                        .child(UID)
                        .child(uorderID);



                uidref.child(umedID).child("ustatus").setValue("ACCEPTED");*/

/*

String url = "https://smplmedicalapp-408ea-default-rtdb.firebaseio.com/";
                DatabaseReference vendorOderRef = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl(url)
                        .child("orders");
                vendorOderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            DatabaseReference voderRef1 = FirebaseDatabase.getInstance()
                                    .getReferenceFromUrl(url).child("orders").child(dataSnapshot.getKey());

                            voderRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot snapshot2: snapshot.getChildren()){

                                        Log.i("testing which ID", snapshot2.getKey());

                                        DatabaseReference voderRef2 = voderRef1.child(snapshot2.getKey());
                                        DatabaseReference voderRefcust = voderRef1.child(snapshot2.child("customer").getKey());

                                        VendorOrderModel vendorOrderModel = snapshot2.getValue(VendorOrderModel.class);

                                        VendorOrderModel vendorOrderModelcust = snapshot2.child("customer").getValue(VendorOrderModel.class);
                                        Log.i("testing which key: ", snapshot2.getValue().toString());
                                        Log.i("testing which key 2: ", vendorOrderModel.getStoreId());


                                        if(vendorOrderModel.getStoreId().equals(orderStoreID) &&
                                                vendorOrderModel.getMedicineId().equals(umedID)  &&
                                        vendorOrderModelcust.getUid().equals(UID)){
                                            vendorOrderModel.setStatus("ACCEPTED");
                                        }

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

*/


                    //holder.changeOdr.setText("Accepted");

                }
            });
            //holder.quantity.setText(list.get(position).getQuantity());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class orderViewHolder extends RecyclerView.ViewHolder{

            TextView medicineName, address, name,phone, amount, tax,quantity, total, orderid;
            RelativeLayout relativeLayout;
            ImageView img;
            Button changeOdr;

            public orderViewHolder(@NonNull View itemView) {
                super(itemView);

                img = itemView.findViewById(R.id.citem_img);
                medicineName = itemView.findViewById(R.id.item_name);
                //address = itemView.findViewById(R.id.address);
                name = itemView.findViewById(R.id.user_name);
                // phone = itemView.findViewById(R.id.phone);
                // amount = itemView.findViewById(R.id.item_original_price);
                // tax = itemView.findViewById(R.id.item_discount);
                quantity = itemView.findViewById(R.id.item_qty);
                total = itemView.findViewById(R.id.item_price);
                //orderid = itemView.findViewById(R.id.odrid);
                changeOdr = itemView.findViewById(R.id.changeodr);
                relativeLayout = itemView.findViewById(R.id.order_relativeLayout);

            }
        }


    }


}