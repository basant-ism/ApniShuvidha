package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apnishuvidha.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import model.Prevlent;
import model.Product;
import model.Shop;
import viewHolder.ProductViewHolder;
import viewHolder.ShopViewHolder;

public class ShopProducts extends AppCompatActivity {
    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;
    RecyclerView recyclerView;

    DatabaseReference mDataRef;

    String SELLER_UID="";

    TextView tvNotAddedByThemYet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_products);

        recyclerView=findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ShopProducts.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mDataRef= FirebaseDatabase.getInstance().getReference();

        SELLER_UID=getIntent().getStringExtra(Prevlent.sellerUid);
        tvNotAddedByThemYet=findViewById(R.id.tv_not_added_by_them_yet);
        tvNotAddedByThemYet.setVisibility(View.VISIBLE);
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
                tvNotAddedByThemYet.setVisibility(View.GONE);
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
}
