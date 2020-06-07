package users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apnishuvidha.MainActivity;
import com.example.apnishuvidha.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import model.Prevlent;
import model.Shop;
import users.frangment.SearchFragment;
import users.frangment.SellerFragment;
import viewHolder.ShopViewHolder;


public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;

    TextView tvUserName,tvUserPhone;
    ImageView imgUser;

    SharedPreferences sp;

    DatabaseReference mDataRef;



    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bundle=new Bundle();

        sp=getSharedPreferences("MyPreferance",MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mDataRef= FirebaseDatabase.getInstance().getReference();




        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragment=new SellerFragment();
        bundle.putString(Prevlent.catagory,Prevlent.CLOTHES);
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.nav_host_fragment,fragment);
        fragmentTransaction.commit();




        tvUserName=navigationView.getHeaderView(0).findViewById(R.id.tv_user_name);
        tvUserPhone=navigationView.getHeaderView(0).findViewById(R.id.tv_user_phone);
        imgUser=navigationView.findViewById(R.id.img_user);


        setUserData();

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_open);
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    private void setUserData()
    {
      tvUserPhone.setText(Prevlent.currentUser.getPhone());
      tvUserName.setText(Prevlent.currentUser.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_page, menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        final SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setQueryHint("search here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v("TAG",query);
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragment=new SearchFragment();
                bundle.putString("INPUT_NAME",query);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
                fragmentTransaction.commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v("TAG",newText);
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragment=new SearchFragment();
                bundle.putString("INPUT_NAME",newText);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
                fragmentTransaction.commit();
                return true;
            }
        });
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.clearFocus();
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragment=new SellerFragment();
                bundle.putString(Prevlent.catagory,Prevlent.CLOTHES);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
                fragmentTransaction.commit();
                return true;
            }
        });

         return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        fragmentTransaction=fragmentManager.beginTransaction();
        switch (item.getItemId())
        {
            case R.id.action_settings:
               // fragment= new SettingFrangment();
                break;
            case R.id.action_contacts:
               // fragment=new ContactFragment();
                break;
            case R.id.action_about:
                //fragment=new AboutUsFrangment();
                break;

        }
//        fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragment=new SellerFragment();
        switch (menuItem.getItemId())
        {
            case R.id.nav_clothes:
                bundle.putString(Prevlent.catagory,Prevlent.CLOTHES);
                fragment.setArguments(bundle);
                break;
            case R.id.nav_general_store:
                bundle.putString(Prevlent.catagory,Prevlent.GENERAL_STORE);
                fragment.setArguments(bundle);
                break;
            case R.id.nav_medical_store:
                bundle.putString(Prevlent.catagory,Prevlent.MEDICAL_STORE);
                fragment.setArguments(bundle);
                break;
            case R.id.nav_taksi:
               // fragment=new TaksiFragment();
                break;
            case R.id.nav_tent:
                //fragment=new TentFragment();
                break;
            case R.id.nav_raj_mistri:
               // fragment=new RajmistryFragment();
                break;
            case R.id.nav_bus:
                Toast.makeText(HomePage.this,"bus",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_train:
                Toast.makeText(HomePage.this,"train",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_logout:
                sp.edit().clear().apply();
                Intent intent=new Intent(HomePage.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;

        }
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        drawer.closeDrawers();
        return true;
    }
}
