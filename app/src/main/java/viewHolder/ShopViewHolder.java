package viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apnishuvidha.R;

public class ShopViewHolder extends RecyclerView.ViewHolder {
    public TextView tvSellerName,tvShopStatus,tvSellerEmail,tvSellerPhone,tvShopAddress;
    public ImageView imgPressHere;

    public ShopViewHolder(@NonNull View itemView) {
        super(itemView);
        tvSellerEmail=itemView.findViewById(R.id.tv_seller_email);
        tvSellerName=itemView.findViewById(R.id.tv_seller_name);
        tvSellerPhone=itemView.findViewById(R.id.tv_seller_phone);
        tvShopAddress=itemView.findViewById(R.id.tv_shop_address);
        tvShopStatus=itemView.findViewById(R.id.tv_shop_status);
        imgPressHere=itemView.findViewById(R.id.img_press_here);

    }

    public void setTvSellerName(String sellerName) {
        tvSellerName.setText(sellerName);
    }

    public void setTvShopStatus(String shopStatus) {
        tvShopStatus.setText(shopStatus);
    }

    public void setTvSellerEmail(String sellerEmail) {
        tvSellerEmail.setText(sellerEmail);
    }

    public void setTvSellerPhone(String sellerPhone) {
        tvSellerPhone.setText(sellerPhone);
    }

    public void setTvShopAddress(String shopAddress) {
        tvShopAddress.setText(shopAddress);
    }


}
