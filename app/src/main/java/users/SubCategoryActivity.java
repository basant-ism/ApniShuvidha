package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import decoration.SpacesItemDecoration;
import model.Category;
import seller.SellerRegisterActivity;
import viewHolder.CategoryViewHolder;

public class SubCategoryActivity extends AppCompatActivity {
    DatabaseReference mDataRef;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;
    RecyclerView recyclerView;

    String CATEGORY="";

    TextView myTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        mDataRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(SubCategoryActivity.this,2);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(false);

        CATEGORY=getIntent().getStringExtra("CATEGORY");
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myTitle=findViewById(R.id.my_tittle);
        myTitle.setText(CATEGORY);

    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mDataRef.child("subCategory").child(CATEGORY);

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
                        Intent intent=new Intent(SubCategoryActivity.this, SellerDetails.class);
                        intent.putExtra("CATEGORY",CATEGORY);
                        intent.putExtra("SUB_CATEGORY",model.getName());
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
