package users.frangment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apnishuvidha.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import model.Seller;
import users.SellerProduct;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    String SELLER_UID="";
    DatabaseReference mDataRef;
    TextView tvSellerEmail,tvSellerPhone,tvShopDes,tvShopAddress,tvShopStatus;
    ImageView imgShopStatus,imgShop;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_contact, container, false);
        mDataRef= FirebaseDatabase.getInstance().getReference();

        tvSellerEmail=view.findViewById(R.id.tv_seller_email);
        tvSellerPhone=view.findViewById(R.id.tv_seller_phone);
        tvShopDes=view.findViewById(R.id.tv_shop_des);
        tvShopAddress=view.findViewById(R.id.tv_shop_address);
        tvShopStatus=view.findViewById(R.id.tv_shop_status);
        imgShopStatus=view.findViewById(R.id.img_shop_status);
        imgShop=view.findViewById(R.id.img_shop);
        SELLER_UID=getArguments().getString("SELLER_UID");
        tvSellerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        tvSellerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeACall();
            }
        });
        tvShopAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });


        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDataRef.child("sellers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(SELLER_UID).exists())
                {
                    Seller seller=dataSnapshot.child(SELLER_UID).getValue(Seller.class);
                    if(seller.getShopImage()!=null)
                        Picasso.get().load(seller.getShopImage()).into(imgShop);
                    tvSellerEmail.setText(seller.getSellerEmail());
                    tvSellerPhone.setText(seller.getSellerPhone());
                    tvShopAddress.setText(seller.getShopAddress());
//                    if(seller.getShopStatus().equals("open"))
//                    {
//                        tvShopStatus.setText("avaliable");
//                        imgShopStatus.setImageResource(R.drawable.available);
//                    }
//                    else if(seller.getShopStatus().equals("close"))
//                    {
//                        tvShopStatus.setText("not avaliable");
//                        imgShopStatus.setImageResource(R.drawable.not_avaliable);
//                    }
                    tvShopDes.setText(seller.getShopDes());

                }
                else
                {
                    Toast.makeText(getContext(),"there is problem we try to fix it",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void makeACall()
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+tvSellerPhone.getText().toString()));
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},0);
        }
        else{
            try {
                startActivity(intent);
            }
            catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    public void sendEmail()
    {
        Intent intent=new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{tvSellerEmail.getText().toString()});
        intent.putExtra(Intent.EXTRA_SUBJECT,"Email by ApniShuvidha");
        intent.setType("message/rfc822");
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},0);
        }
        else{
            try {
                startActivity(Intent.createChooser(intent,"Chooses an email client"));
            }
            catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    public void openMap()
    {
        String url="http://maps.google.com/maps?daddr="+tvShopAddress.getText().toString();
        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        startActivity(intent);
    }
}
