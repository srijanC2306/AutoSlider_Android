package com.example.simphealbusiness.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simphealbusiness.R;
import com.example.simphealbusiness.model.ItemModel;
import com.example.simphealbusiness.model.OrderModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.List;

import static android.content.ContentValues.TAG;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.orderViewHolder>  {


    private List<OrderModel> list;

    public OrderAdapter(List<OrderModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public orderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Context context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
        return new OrderAdapter.orderViewHolder(v);
    }

    @Override
    public  void onBindViewHolder(@NonNull orderViewHolder holder, int position) {

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
        holder.changeOdr.setText("Cancel");


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





       /* if(currentItem.getUstatus().matches("ORDERED")){
            holder.changeOdr.setVisibility(View.INVISIBLE);
        }*/

        holder.changeOdr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String curuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference orderCollectionRef = FirebaseDatabase.getInstance().getReference()
                        .child("userorders").child(curuid);

                orderCollectionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot orderSnapshot: snapshot.getChildren()){

                            String odrKey = orderSnapshot.getKey();
                            Log.i("OrderID", String.valueOf(orderSnapshot.child(medID)));

                         if(orderSnapshot.child(medID).exists()){
                             orderSnapshot.getRef().removeValue();
                             Log.i("remove msg", "Removed from userodr");

                         }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
