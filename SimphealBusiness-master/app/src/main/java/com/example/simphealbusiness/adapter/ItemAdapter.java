package com.example.simphealbusiness.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simphealbusiness.GenerateRandomString;
import com.example.simphealbusiness.R;
import com.example.simphealbusiness.model.ItemModel;
import com.example.simphealbusiness.model.customerModel;
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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ExampleViewHolder> implements Filterable {

    String cname, cphone;




    private List<ItemModel> exampleList;
    private List<ItemModel> exampleListFull;

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView name, desc, price, qty, discount, size, org_price, discountPrice ;
        public TextView button;
        private EditText uqty;
        public RelativeLayout relativeLayout;

        public ExampleViewHolder(View itemView){
            super(itemView);
            this.uqty = itemView.findViewById(R.id.uqty);
            this.imageView = itemView.findViewById(R.id.item_img);
            this.name = itemView.findViewById(R.id.item_name);
            this.desc = itemView.findViewById(R.id.item_desc);
            this.org_price = itemView.findViewById(R.id.item_original_price);
            this.discountPrice = itemView.findViewById(R.id.item_price);
            this.discount = itemView.findViewById(R.id.item_discount);
            this.qty = itemView.findViewById(R.id.item_qty);
            this.size = itemView.findViewById(R.id.item_size);
            this.button = itemView.findViewById(R.id.btn_remove);
           // button.setVisibility(View.INVISIBLE);
            relativeLayout = itemView.findViewById(R.id.item_relativeLayout);
        }



    }
    public ItemAdapter(List<ItemModel> exampleList) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ItemModel currentItem = exampleList.get(position);
        //holder.imageView.setImageResource(currentItem.getImageResource());
        //holder.textView2.setText(currentItem.getText2());


        holder.name.setText(currentItem.getName());
        holder.desc.setText(currentItem.getDescription());
        holder.org_price.setText("₹"+currentItem.getPrice());


        float discount = Integer.parseInt(currentItem.getDiscount());
        float price = Integer.parseInt(currentItem.getPrice());

        int dprice = (int) ((price*(100-discount))/100);
        holder.discountPrice.setText("₹"+dprice);
        holder.qty.setText("Qty :"+ currentItem.getQty());
        holder.discount.setText(currentItem.getDiscount()+"%off");
        holder.size.setText("Size :"+ currentItem.getSize());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.uqty.getText().toString().matches("") || Integer.parseInt(holder.uqty.getText().toString()) < 0) {
                    Toast.makeText(v.getContext(), "Please enter quantity more than 1", Toast.LENGTH_SHORT)
                            .show();

                } else {


                holder.name.getText();

                Log.i("storeId", currentItem.getStoreId());
                Log.i("umedID", currentItem.getUmedID());

                String medname = holder.name.getText().toString();
                String umedID = currentItem.getUmedID();
                String storeId = currentItem.getStoreId();
                float amount1 = Integer.parseInt(holder.uqty.getText().toString())
                        * Float.parseFloat(currentItem.getPrice()) *
                        (float)((100- Float.parseFloat(currentItem.getDiscount()))/100);

                String amount = String.valueOf(amount1);

                String uqty = holder.uqty.getText().toString();
                String tax = String.valueOf(amount1 * 0.18);
                String ustatus = "CART";

                //check before add to cart
                Log.i("medname", medname);
                Log.i("umedId", umedID);
                Log.i("storeId", storeId);
                Log.i("amount", amount);
                Log.i("uqty", uqty);
                Log.i("tax", tax);
                Log.i("ustatus", ustatus);

                    DatabaseReference cartref = FirebaseDatabase
                            .getInstance()
                            .getReferenceFromUrl("https://smplmedicalapp-408ea-default-rtdb.firebaseio.com/")
                            .child("userorders");



                    String curuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference customerdetails = FirebaseDatabase
                            .getInstance()
                            .getReferenceFromUrl("https://smplmedicalapp-408ea-default-rtdb.firebaseio.com/")
                            .child("customer").child(curuser);

                   cartref =  cartref.child(curuser)
                           .child(GenerateRandomString.randomString(20))
                           .child(umedID);


                    DatabaseReference finalCartref = cartref;
                    customerdetails.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            customerModel cmodel = snapshot.getValue(customerModel.class);


                            cname = cmodel.getName();
                            cphone = cmodel.getPhone();

                            finalCartref.child("UID").setValue(curuser);
                            finalCartref.child("address").setValue("");
                            finalCartref.child("amount").setValue(amount);
                            finalCartref.child("medicineName").setValue(medname);
                            finalCartref.child("name").setValue(cname);
                            finalCartref.child("phone").setValue(cphone);
                            finalCartref.child("quantity").setValue(uqty);
                            finalCartref.child("storeId").setValue(storeId);
                            finalCartref.child("tax").setValue(tax);
                            finalCartref.child("umedID").setValue(umedID);
                            finalCartref.child("ustatus").setValue("CART");

                            Log.i("cdetails", cmodel.getName());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                 /*  cartref.child("UID").setValue(curuser);
                   cartref.child("address").setValue("NA");
                   cartref.child("amount").setValue(amount);
                   cartref.child("medicineName").setValue(medname);
                   cartref.child("name").setValue(cname);
                   cartref.child("phone").setValue(cphone);
                   cartref.child("quantity").setValue(uqty);
                   cartref.child("storeId").setValue(storeId);
                   cartref.child("tax").setValue(tax);
                   cartref.child("umedID").setValue(umedID);
                   cartref.child("ustatus").setValue("CART");*/



                Toast.makeText(v.getContext(), "Added to cart ! ", Toast.LENGTH_SHORT).show();
                holder.uqty.setText("");
            }


            }
        });


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage
                .getReferenceFromUrl("gs://smplmedicalapp-408ea.appspot.com/Images/")
                .child(currentItem.getImage());
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
                holder.imageView.setImageBitmap(bitmap);
            }
        });

    }
    @Override
    public int getItemCount() {
        return exampleList.size();
    }


    private Filter examplefilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ItemModel> filterlist=new ArrayList<>();
            if(constraint==null|| constraint.length()==0){
                filterlist.addAll(exampleListFull);
            }
            else{
                String pattrn=constraint.toString().toLowerCase().trim();
                for(ItemModel item :exampleListFull){
                    if(item.getName().toLowerCase().contains(pattrn)){
                        filterlist.add(item);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filterlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

}
