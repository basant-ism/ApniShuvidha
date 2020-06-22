package viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apnishuvidha.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerUserViewHolder extends RecyclerView.ViewHolder {
    public TextView tvSellerName;
    public CircleImageView imgSeller;
    public SellerUserViewHolder(@NonNull View itemView) {
        super(itemView);
        tvSellerName=itemView.findViewById(R.id.tv_seller_name);
        imgSeller=itemView.findViewById(R.id.img_seller);
    }
    public void setSellerImage(String imageUri)
    {
        Picasso.get().load(imageUri).into(imgSeller);
    }
    public void setSellerName(String name)
    {
        tvSellerName.setText(name);
    }
}
