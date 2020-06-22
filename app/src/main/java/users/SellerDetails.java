package users;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import decoration.SpacesItemDecoration;
import model.Category;
import model.Seller;
import viewHolder.CategoryViewHolder;
import viewHolder.SellerUserViewHolder;

public class SellerDetails extends AppCompatActivity {
    String CATEGORY="",SUB_CATEGORY="";

    DatabaseReference mDataRef;
    FirebaseRecyclerAdapter<Seller, SellerUserViewHolder> adapter;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    String userCity=null;
    TextView myTitle,tvNoSellerYet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_details);

        mAuth=FirebaseAuth.getInstance();

        CATEGORY=getIntent().getStringExtra("CATEGORY");
        SUB_CATEGORY=getIntent().getStringExtra("SUB_CATEGORY");

        mDataRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(SellerDetails.this,2);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(false);

        tvNoSellerYet=findViewById(R.id.tv_no_seller_yet);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myTitle=findViewById(R.id.my_tittle);
        myTitle.setText(SUB_CATEGORY+"'sellers");
    }
    @Override
    public void onStart() {
        super.onStart();


        mDataRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mAuth.getUid()).exists())
                {
                    userCity=dataSnapshot.child(mAuth.getUid()).child("userCity").getValue(String.class);
                   String cityCategory=userCity+"/"+CATEGORY+"/"+SUB_CATEGORY;
                    Query query = mDataRef.child("sellers").orderByChild("cityCategory").equalTo(cityCategory);

                    FirebaseRecyclerOptions<Seller> options = new FirebaseRecyclerOptions.Builder<Seller>()
                            .setQuery(query, Seller.class)
                            .build();
                    adapter = new FirebaseRecyclerAdapter<Seller, SellerUserViewHolder>(options) {

                        @Override
                        protected void onBindViewHolder(@NonNull SellerUserViewHolder holder, int position, @NonNull final Seller model) {
                                tvNoSellerYet.setVisibility(View.GONE);
                                holder.setSellerName(model.getSellerName());
                                if(model.getSellerImage()!=null)
                                    holder.setSellerImage(model.getSellerImage());
                                holder.imgSeller.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SellerDetails.this,ShopToUserActivity. class);
                                        intent.putExtra("SELLER_UID", model.getSellerUid());
                                        startActivity(intent);
                                    }
                                });
                            }


                        @NonNull
                        @Override
                        public SellerUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_layout_user_2, parent, false);

                            return new SellerUserViewHolder(view);
                        }


                    };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
