package com.example.apnishuvidha;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapter.SliderAdapterExample;
import decoration.SpacesItemDecoration;
import model.Category;
import model.Seller;
import model.SliderItem;
import users.SellerDetails;
import users.ShopToUserActivity;
import users.SubCategoryActivity;
import viewHolder.CategoryViewHolder;
import viewHolder.SellerUserViewHolder;
import viewHolder.VerticalViewHolder;


public class HomeFragment extends Fragment {

    DatabaseReference mDataRef;
    FirebaseRecyclerAdapter<Category, VerticalViewHolder>adapter;
    RecyclerView verticalRecyclerView;
    RecyclerView sellerRecyclerView;
    SliderView sliderView;

    FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<Category,CategoryViewHolder>adapter2;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//    }
    public HomeFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        verticalRecyclerView=view.findViewById(R.id.vertical_recycler_view);
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        verticalRecyclerView.addItemDecoration(new SpacesItemDecoration(5));


        sellerRecyclerView=view.findViewById(R.id.all_sub_category_rview);
        sellerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        sellerRecyclerView.addItemDecoration(new SpacesItemDecoration(5));


        sliderView = view.findViewById(R.id.imageSlider);
        mDataRef= FirebaseDatabase.getInstance().getReference();
        setSeller();
        setAdvertisementSlides();

        onStart();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        Query query = mDataRef.child("sellerCategory");

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Category, VerticalViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull VerticalViewHolder holder, int position, @NonNull final Category model) {
                holder.tvCategoryName.setText(model.getName());
                holder.imgSeeMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(), SubCategoryActivity.class);
                        intent.putExtra("CATEGORY",model.getName());
                        startActivity(intent);

                    }
                });

                Query query=mDataRef.child("subCategory").child(model.getName());
                Log.v("ENTER",model.getName());
                FirebaseRecyclerOptions<Category>options2=new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(query,Category.class)
                        .build();
                adapter2=new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final Category model1) {
                        holder.setCategoryImage(model1.getImage());
                        holder.setCategoryName(model1.getName());
                        holder.imgCategory.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getActivity(), SellerDetails.class);
                                intent.putExtra("CATEGORY",model.getName());
                                intent.putExtra("SUB_CATEGORY",model1.getName());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_layout, parent, false);

                        return new CategoryViewHolder(view);
                    }
                };
                Log.v("COUNT",adapter2.getItemCount()+"kaka");

                adapter2.startListening();
                adapter2.notifyDataSetChanged();
                holder.horizontalRecyclerView.setAdapter(adapter2);
                if(adapter2!=null)
                {
                    Log.v("DEEP_SUB","not null");

                }
                else
                {
                    Log.v("DEEP_SUB","null");
                }

            }

            @NonNull
            @Override
            public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_layout, parent, false);

                return new VerticalViewHolder(view);
            }


        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        verticalRecyclerView.setAdapter(adapter);
        Log.v("n","nn");

    }
    private void setSeller()
    {
        Query query=mDataRef.child("sellers").orderByChild("isImage").startAt("true");
        FirebaseRecyclerOptions<Seller>options=new FirebaseRecyclerOptions.Builder<Seller>()
                .setQuery(query,Seller.class)
                .build();
        FirebaseRecyclerAdapter<Seller, SellerUserViewHolder>adapter=new FirebaseRecyclerAdapter<Seller, SellerUserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerUserViewHolder holder, int position, final @NonNull Seller model) {
                Picasso.get().load(model.getSellerImage()).into(holder.imgSeller);
                holder.tvSellerName.setText(model.getSellerName());
                holder.imgSeller.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getContext(), ShopToUserActivity.class);
                        intent.putExtra("SELLER_UID",model.getSellerUid());
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public SellerUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(getContext()).inflate(R.layout.seller_layout_user,parent,false);
                return new SellerUserViewHolder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        sellerRecyclerView.setAdapter(adapter);
    }

    private void setAdvertisementSlides() {
        SliderItem obj1=new SliderItem();
        obj1.setImage_id(R.drawable.adv1);
        obj1.setDes("advertismient");

        SliderItem obj2=new SliderItem();
        obj2.setImage_id(R.drawable.adv2);
        obj2.setDes("advertismient");

        SliderItem obj3=new SliderItem();
        obj3.setImage_id(R.drawable.adv3);
        obj3.setDes("advertismient");

        SliderItem obj4=new SliderItem();
        obj4.setImage_id(R.drawable.adv4);
        obj4.setDes("advertismient");

        SliderItem obj5=new SliderItem();
        obj5.setImage_id(R.drawable.adv5);
        obj5.setDes("advertismient");

        List<SliderItem> list=new ArrayList<>();
        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        list.add(obj4);
        list.add(obj5);



        SliderAdapterExample adapter = new SliderAdapterExample(getContext());
        adapter.renewItems(list);
        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}
