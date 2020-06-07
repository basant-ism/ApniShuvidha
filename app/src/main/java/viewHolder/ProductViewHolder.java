package viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apnishuvidha.R;
import com.squareup.picasso.Picasso;

public class ProductViewHolder extends RecyclerView.ViewHolder
{
    TextView tvProductName,tvProductPrice,tvProductDes;
    ImageView imgProduct;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        tvProductName=itemView.findViewById(R.id.tv_product_name);
        tvProductPrice=itemView.findViewById(R.id.tv_product_price);
        tvProductDes=itemView.findViewById(R.id.tv_product_des);
        imgProduct=itemView.findViewById(R.id.img_product);
    }
    public void setProductName(String pname)
    {
        tvProductName.setText(pname);
    }
    public void setProductPrice(String price)
    {
        tvProductPrice.setText(price);
    }
    public void setProductDes(String des)
    {
        tvProductDes.setText(des);
    }
    public void setProductImage(String image)
    {
        Picasso.get().load(image).into(imgProduct);
    }
}
