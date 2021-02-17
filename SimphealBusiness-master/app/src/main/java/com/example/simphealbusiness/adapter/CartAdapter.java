package com.example.simphealbusiness.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simphealbusiness.R;
import com.example.simphealbusiness.model.ItemModel;
import com.example.simphealbusiness.model.OrderModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class CartAdapter extends FirebaseRecyclerAdapter<OrderModel, CartAdapter.ViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CartAdapter(@NonNull FirebaseRecyclerOptions<OrderModel> options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull OrderModel model) {


        holder.name.setText(model.getMedicineName());
        holder.desc.setText(model.getTax());
        holder.discountPrice.setText("₹"+model.getAmount());

       /* float discount = Integer.parseInt(model.getDiscount());
        float price = Integer.parseInt(model.getPrice());

        int dprice = (int) ((price*(100-discount))/100);*/


        //holder.discountPrice.setText("₹"+dprice);
        holder.qty.setText("Qty :"+ model.getQuantity());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    FirebaseDatabase.getInstance().getReference().child("items")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(getRef(position).getKey())
                        .removeValue();
                Toast.makeText( v.getContext(), "Deleted !", Toast.LENGTH_SHORT).show();
*/
            }
        });

       /* FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage
                .getReferenceFromUrl("gs://smplmedicalapp-408ea.appspot.com/Images/")
                .child(model.getImage());
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
        });*/

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView name, desc, price, qty, discount, size, org_price, discountPrice ;
        public TextView button;

        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView){
            super(itemView);
            this.imageView = itemView.findViewById(R.id.item_img);
            this.name = itemView.findViewById(R.id.item_name);
            this.desc = itemView.findViewById(R.id.item_desc);
            this.discountPrice = itemView.findViewById(R.id.item_price);
            this.qty = itemView.findViewById(R.id.item_qty);
            this.button = itemView.findViewById(R.id.btn_remove);
            relativeLayout = itemView.findViewById(R.id.item_relativeLayout);
        }

    }
}
