package users;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apnishuvidha.BecomeSellerFragment;
import com.example.apnishuvidha.MainActivity;
import com.example.apnishuvidha.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.SliderAdapterExample;
import decoration.SpacesItemDecoration;
import model.Category;
import model.Prevlent;
import model.Seller;
import model.SliderItem;
import model.User;
import viewHolder.CategoryViewHolder;
import viewHolder.SellerUserViewHolder;
import viewHolder.VerticalViewHolder;


public class HomePage extends AppCompatActivity  implements
        PopupMenu.OnMenuItemClickListener {

//    FragmentManager fragmentManager;
//    FragmentTransaction fragmentTransaction;
//    Fragment fragment;

    TextView tvUserName,tvUserEmail;
    ImageView imgUser;

    DatabaseReference mDataRef;
    RecyclerView sellerRecyclerView;

    FirebaseAuth mAuth;


    SearchView searchView;


    BottomNavigationView bottom_nav;
    NavigationView navigationView;
    DrawerLayout drawer;
    NavController navController;
    AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth=FirebaseAuth.getInstance();
        mDataRef= FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottom_nav=findViewById(R.id.bottom_nev_view);

        navController=Navigation.findNavController(this,R.id.nav_host_fragment);
        appBarConfiguration=new AppBarConfiguration.Builder(new int[]{R.id.home,R.id.notification,R.id.seller})
                                                .setDrawerLayout(drawer)
                                                .build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView,navController);
        NavigationUI.setupWithNavController(bottom_nav,navController);


        tvUserName=navigationView.getHeaderView(0).findViewById(R.id.tv_user_name);
        tvUserEmail=navigationView.getHeaderView(0).findViewById(R.id.tv_user_email);
        imgUser=navigationView.findViewById(R.id.img_user);
        searchView=findViewById(R.id.search_bar);
        searchView.setQueryHint("Search here");

        setUserData();

       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId())
               {
                   case R.id.nav_cart:
                       Toast.makeText(HomePage.this,"my cart list",Toast.LENGTH_LONG).show();
                       break;
                   case R.id.nav_setting:
                       Toast.makeText(HomePage.this,"settings ",Toast.LENGTH_LONG).show();
                       break;
                   case R.id.nav_share:
                       Toast.makeText(HomePage.this,"share",Toast.LENGTH_LONG).show();
                       break;
                   case R.id.nav_logout:
                       mAuth.signOut();
                Intent intent=new Intent(HomePage.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                       break;

               }
               drawer.closeDrawer(GravityCompat.START);
               return true;
           }
       });





    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,appBarConfiguration);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();


    }




    private void setUserData()
    {
        mDataRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mAuth.getUid()).exists())
                {
                    User user=dataSnapshot.child(mAuth.getUid()).getValue(User.class);
                    if(user.getUserImage()!=null)
                        Picasso.get().load(user.getUserImage()).into(imgUser);
                   tvUserName.setText(user.getUserName());
                   tvUserEmail.setText(user.getUserEmail());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_page, menu);
         return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_pop:
                View view=findViewById(R.id.action_pop);
                showPopUp(view);
                break;
            case R.id.action_cart_list:
                Toast.makeText(HomePage.this,"my cart list",Toast.LENGTH_LONG).show();
                break;


        }
        return true;
    }

    private void showPopUp(View view)
    {
        PopupMenu popupMenu=new PopupMenu(this,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.as_visiter:
                Toast.makeText(HomePage.this,"you are already a vister",Toast.LENGTH_LONG).show();
                break;
            case R.id.as_a_seller:
                Fragment fragment=new BecomeSellerFragment();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.logout:
                mAuth.signOut();
                Intent intent=new Intent(HomePage.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        return true;
    }



}
