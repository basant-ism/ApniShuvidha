package seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apnishuvidha.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

import model.CustumProgress2;
import model.Product;
import viewHolder.ProductViewHolder;

public class SellerHomePage extends AppCompatActivity {
    FirebaseRecyclerAdapter<Product, ProductViewHolder>adapter;
    DatabaseReference mDataRef;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    TextView tvNotAddedYet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_home_page);

        tvNotAddedYet=findViewById(R.id.tv_not_added_yet);
        tvNotAddedYet.setVisibility(View.VISIBLE);

        recyclerView=findViewById(R.id.recycler_view);
        LinearLayoutManager manager=new LinearLayoutManager(SellerHomePage.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(false);

        mDataRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        isAnyProductExist();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.seller_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_shop_status:
                final CustumProgress2 custumProgress2=new CustumProgress2(SellerHomePage.this);
                custumProgress2.startProgressBar();
                custumProgress2.imgOpenShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("shopStatus","open");
                        mDataRef.child("sellers").child(mAuth.getCurrentUser().getUid()).updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(SellerHomePage.this,"shop opened",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(SellerHomePage.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });
                custumProgress2.imgCloseShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("shopStatus","closed");
                        mDataRef.child("sellers").child(mAuth.getCurrentUser().getUid()).updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            custumProgress2.closeProgressBar();
                                            Toast.makeText(SellerHomePage.this,"shop closed",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            custumProgress2.closeProgressBar();
                                            Toast.makeText(SellerHomePage.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });
                break;
        }
        return true;
    }
    public void addNewProduct(View view)
    {
        Intent intent=new Intent(SellerHomePage.this, AddNewProduct.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query=mDataRef.child("products")
                .orderByChild("sellerUid")
                .equalTo(mAuth.getCurrentUser().getUid());
        FirebaseRecyclerOptions<Product>options=new FirebaseRecyclerOptions.Builder<Product>()
                                                    .setQuery(query,Product.class)
                                                    .build();
        adapter=new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
                tvNotAddedYet.setVisibility(View.GONE);
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
    public  void isAnyProductExist()
    {

    }
}
