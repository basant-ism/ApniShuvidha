package seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apnishuvidha.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import adapter.SliderAdapterExample;
import decoration.SpacesItemDecoration;
import model.Category;
import model.SliderItem;
import viewHolder.CategoryViewHolder;

public class SellerCatagroy extends AppCompatActivity {
    DatabaseReference mDataRef;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_catagroy);

        mDataRef=FirebaseDatabase.getInstance().getReference();

        recyclerView=findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(SellerCatagroy.this,2);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(false);

        setAdvertisementSlides();
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

        SliderView sliderView = findViewById(R.id.imageSlider);

        SliderAdapterExample adapter = new SliderAdapterExample(this);
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
    public void onStart() {
        super.onStart();
        Query query = mDataRef.child("sellerCategory");

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final Category model) {
                 holder.tvCategoryName.setText(model.getName());
                 holder.setCategoryImage(model.getImage());
                 holder.imgCategory.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent=new Intent(SellerCatagroy.this,SellerRegisterActivity.class);
                         intent.putExtra("categoryImage",model.getImage());
                         intent.putExtra("categoryName",model.getName());
                         startActivity(intent);
                     }
                 });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);

                return new CategoryViewHolder(view);
            }


        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
