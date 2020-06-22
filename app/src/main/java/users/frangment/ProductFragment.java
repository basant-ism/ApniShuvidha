package users.frangment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apnishuvidha.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import model.Product;
import viewHolder.ProductViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment {
    DatabaseReference mDataRef;
    String SELLER_UID="";
    RecyclerView recyclerView;

    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_product, container, false);
        mDataRef= FirebaseDatabase.getInstance().getReference();
        SELLER_UID=getArguments().getString("SELLER_UID");
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query=mDataRef.child("sellers").child(SELLER_UID).child("products");
        FirebaseRecyclerOptions<Product> options=new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query,Product.class)
                .build();
        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
                holder.setProductName(model.getPname());
                holder.setProductImage(model.getPimage());
                holder.setProductDes(model.getPdescription());
                holder.setProductPrice(model.getPrice()+"$");
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(getContext()).inflate(R.layout.product_layout,parent,false);

                return new ProductViewHolder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
