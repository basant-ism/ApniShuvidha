package users.frangment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.squareup.picasso.Picasso;

import decoration.SpacesItemDecoration;
import model.Category;
import model.Product;
import model.ProductType;
import model.Seller;
import viewHolder.CategoryViewHolder;
import viewHolder.ProductViewHolder;
import viewHolder.VerticalViewHolder;


public class ShopFragment extends Fragment {
    RecyclerView allProductRView;
    RecyclerView typeWiseRView;
    ImageView shopImage;
    RatingBar shopRatingBar;
    TextView tvRating,tvSellerName;
    String SELLER_UID="";

    DatabaseReference mDataRef;
    FirebaseAuth mAuth;
    boolean tag=false;


    public ShopFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_shop, container, false);
        SELLER_UID = getArguments().getString("SELLER_UID");

        allProductRView=view.findViewById(R.id.all_product_recycler_view);
        typeWiseRView=view.findViewById(R.id.type_wise_recycler_view);
        shopImage=view.findViewById(R.id.img_shop_fragment);
        shopRatingBar=view.findViewById(R.id.ratingBar);
        tvRating=view.findViewById(R.id.tv_rating);
        tvSellerName=view.findViewById(R.id.tv_seller_name);

        allProductRView.setLayoutManager(new LinearLayoutManager(getContext()));
        allProductRView.addItemDecoration(new SpacesItemDecoration(5));
        typeWiseRView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        typeWiseRView.addItemDecoration(new SpacesItemDecoration(5));

        mDataRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        shopRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setShopRating(ratingBar,rating);
            }
        });







        setSellerDetails();



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setProductAccordingtype();
        setAllProduct();
    }

    private void setAllProduct()
    {
        Query query=mDataRef.child("sellers").child(SELLER_UID).child("products");
        FirebaseRecyclerOptions<Product>options=new FirebaseRecyclerOptions.Builder<Product>()
                                                .setQuery(query,Product.class)
                                                .build();
        FirebaseRecyclerAdapter<Product,ProductViewHolder>adapter=new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
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
        allProductRView.setAdapter(adapter);
    }
public  void setShopRating(final RatingBar ratingBar, final float rating )
{
    tag=false;
    final DatabaseReference mChildRef=mDataRef.child("sellers").child(SELLER_UID).child("rating");
    mChildRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(!tag) {
                tag=true;
                if (dataSnapshot.child(mAuth.getUid()).exists()) {
                    Float prevStar = dataSnapshot.child(mAuth.getUid()).getValue(Float.class);
                    mChildRef.child(mAuth.getUid()).setValue(rating);
                    Float totalStar = dataSnapshot.child("totalStar").getValue(Float.class);
                    totalStar = totalStar - prevStar + rating;
                    mChildRef.child("totalStar").setValue(totalStar);
                } else {

                    mChildRef.child(mAuth.getUid()).setValue(rating);
                    Float totalStar=0.0f;
                    Integer totalMember=0;
                    if(dataSnapshot.child("totalStar").exists())
                        totalStar= dataSnapshot.child("totalStar").getValue(Float.class);
                    if(dataSnapshot.child("totalMember").exists())
                        totalMember=dataSnapshot.child("totalMember").getValue(Integer.class);
                    mChildRef.child("totalMember").setValue(totalMember + 1);
                    mChildRef.child("totalStar").setValue(totalStar + rating);

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

}

    private void setProductAccordingtype()
    {
        Query query=mDataRef.child("sellers").child(SELLER_UID).child("productType");
        FirebaseRecyclerOptions<ProductType>options=new FirebaseRecyclerOptions.Builder<ProductType>()
                        .setQuery(query,ProductType.class)
                        .build();
        FirebaseRecyclerAdapter<ProductType, VerticalViewHolder>adapter=new FirebaseRecyclerAdapter<ProductType, VerticalViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull VerticalViewHolder holder, int position, @NonNull ProductType model) {
                        holder.tvCategoryName.setText(model.getTypeName());
                        Query query1=mDataRef.child("sellers").child(SELLER_UID).child("products")
                                                    .orderByChild("ptype").startAt(model.getTypeName());
                        FirebaseRecyclerOptions<Product>options1=new FirebaseRecyclerOptions.Builder<Product>()
                                                                .setQuery(query1,Product.class)
                                                                .build();
                        FirebaseRecyclerAdapter<Product, CategoryViewHolder>adapter1=new FirebaseRecyclerAdapter<Product, CategoryViewHolder>(options1) {
                            @Override
                            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Product model) {
                                holder.setCategoryName(model.getPrice());
                                Picasso.get().load(model.getPimage()).into(holder.imgCategory);
                            }

                            @NonNull
                            @Override
                            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view=LayoutInflater.from(getContext()).inflate(R.layout.horizontal_layout,parent,false);
                                return new CategoryViewHolder(view);
                            }
                        };
                        adapter1.startListening();
                        adapter1.notifyDataSetChanged();
                        holder.horizontalRecyclerView.setAdapter(adapter1);

                    }


                    @NonNull
                    @Override
                    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(getContext()).inflate(R.layout.vertical_layout,parent,false);
                        return new VerticalViewHolder(view);
                    }
                };
                adapter.startListening();
                adapter.notifyDataSetChanged();
                typeWiseRView.setAdapter(adapter);

            }





    private void setSellerDetails()
    {
        tag=false;
        mDataRef.child("sellers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(tag==false) {
                    tag=true;
                    if (dataSnapshot.child(SELLER_UID).exists()) {
                        Seller seller = dataSnapshot.child(SELLER_UID).getValue(Seller.class);
                        tvRating.setText(seller.getShopRating());
                        if (seller.getShopImage() != null)
                            Picasso.get().load(seller.getShopImage()).into(shopImage);
                        else if (seller.getSellerImage() != null)
                            Picasso.get().load(seller.getSellerImage()).into(shopImage);
                        tvSellerName.setText(seller.getSellerName());
                        if (dataSnapshot.child(SELLER_UID).child("rating").exists()) {
                            Float totalStar = dataSnapshot.child(SELLER_UID).child("rating").child("totalStar").getValue(Float.class);
                            Integer totalMember = dataSnapshot.child(SELLER_UID).child("rating").child("totalMember").getValue(Integer.class);
                            Float rating = totalStar / totalMember;
                            String ratingSting = rating.toString();
                            tvRating.setText(ratingSting);
                            if (dataSnapshot.child(SELLER_UID).child("rating").child(mAuth.getUid()).exists()) {
                                Float totalStar1 = dataSnapshot.child(SELLER_UID).child("rating").child(mAuth.getUid()).getValue(Float.class);
                                shopRatingBar.setRating(totalStar1);

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
