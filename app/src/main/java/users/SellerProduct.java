package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apnishuvidha.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import decoration.SpacesItemDecoration;
import model.Category;
import model.Product;
import model.Seller;
import viewHolder.CategoryViewHolder;
import viewHolder.ProductViewHolder;

public class SellerProduct extends AppCompatActivity {
    String SELLER_UID="";


    FirebaseAuth mAuth;

    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;
    RecyclerView recyclerView;

    DatabaseReference mDataRef;

    TextView tvSellerEmail,tvSellerPhone,tvShopDes,tvShopAddress,tvShopStatus;
    ImageView imgShopStatus,imgShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product);

        SELLER_UID=getIntent().getStringExtra("SELLER_UID");
        mDataRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        mAuth=FirebaseAuth.getInstance();

        tvSellerEmail=findViewById(R.id.tv_seller_email);
        tvSellerPhone=findViewById(R.id.tv_seller_phone);
        tvShopDes=findViewById(R.id.tv_shop_des);
        tvShopAddress=findViewById(R.id.tv_shop_address);
        tvShopStatus=findViewById(R.id.tv_shop_status);
        imgShopStatus=findViewById(R.id.img_shop_status);
        imgShop=findViewById(R.id.img_shop);

        setSellerDetails();
    }

    private void setSellerDetails()
    {
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
                    if(seller.getShopStatus().equals("open"))
                    {
                        tvShopStatus.setText("avaliable");
                        imgShopStatus.setImageResource(R.drawable.open);
                    }
                    else if(seller.getShopStatus().equals("close"))
                    {
                        tvShopStatus.setText("not avaliable");
                        imgShopStatus.setImageResource(R.drawable.close);
                    }
                    tvShopDes.setText(seller.getShopDes());

                }
                else
                {
                    Toast.makeText(SellerProduct.this,"there is problem we try to fix it",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query=mDataRef.child("products")
                .orderByChild("sellerUid")
                .equalTo(SELLER_UID);
        FirebaseRecyclerOptions<Product> options=new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query,Product.class)
                .build();
        adapter=new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {

                holder.setProductImage(model.getPimage());
                holder.setProductName(model.getPname());
                holder.setProductPrice("price:"+model.getPrice());
                holder.setProductDes(model.getPdescription());

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false);

                return new ProductViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    public void makeACall(View view)
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+tvSellerPhone.getText().toString()));
        if(ContextCompat.checkSelfPermission(SellerProduct.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SellerProduct.this,new String[]{Manifest.permission.CALL_PHONE},0);
        }
        else{
            try {
                startActivity(intent);
            }
            catch (Exception e){
                Toast.makeText(SellerProduct.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    public void sendEmail(View view)
    {
        Intent intent=new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{tvSellerEmail.getText().toString()});
        intent.putExtra(Intent.EXTRA_SUBJECT,"Email by ApniShuvidha");
        intent.setType("message/rfc822");
        if(ContextCompat.checkSelfPermission(SellerProduct.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SellerProduct.this,new String[]{Manifest.permission.SEND_SMS},0);
        }
        else{
            try {
                startActivity(Intent.createChooser(intent,"Chooses an email client"));
            }
            catch (Exception e){
                Toast.makeText(SellerProduct.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    public void openMap(View view)
    {
        String url="http://maps.google.com/maps?daddr="+tvShopAddress.getText().toString();
        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        startActivity(intent);
    }
}
