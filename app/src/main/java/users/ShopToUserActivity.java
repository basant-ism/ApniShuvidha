package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apnishuvidha.HomeFragment;
import com.example.apnishuvidha.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import model.Seller;
import users.frangment.ContactFragment;
import users.frangment.ProductFragment;
import users.frangment.ShopFragment;

public class ShopToUserActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Fragment fragment;
    FragmentTransaction transaction;
    Bundle bundle;

    DatabaseReference mDataRef;

    String SELLER_UID="";
    ImageView sellerProfileImage;
    TextView tvHeading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_to_user);

        mDataRef= FirebaseDatabase.getInstance().getReference();

         Toolbar toolbar=findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         sellerProfileImage=findViewById(R.id.img_top_seller);
         tvHeading=findViewById(R.id.my_tittle);

        SELLER_UID=getIntent().getStringExtra("SELLER_UID");

        fragmentManager=getSupportFragmentManager();
        transaction=fragmentManager.beginTransaction();
        bundle=new Bundle();
        bundle.putString("SELLER_UID", SELLER_UID);
        fragment=new ShopFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragmnt,fragment);

        transaction.commit();

        setToolBar();


        bottomNavigationView=findViewById(R.id.bottom_nev_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        Log.v("HOME","HOME");
                        fragment=new ShopFragment();
                        bundle.putString("SELLER_UID", SELLER_UID);
                        break;
                    case R.id.nav_contact:
                        fragment=new ContactFragment();
                        bundle.putString("SELLER_UID", SELLER_UID);
                        break;
                    case R.id.nav_product:
                        fragment=new ProductFragment();
                        bundle.putString("SELLER_UID", SELLER_UID);
                        break;
                }
                Log.v("HOME","kkkkk");
                fragmentManager=getSupportFragmentManager();
                transaction=fragmentManager.beginTransaction();
                fragment.setArguments(bundle);
                transaction.replace(R.id.fragmnt,fragment);

                transaction.commit();

                return true;
            }
        });


        }

    private void setToolBar()
    {
        mDataRef.child("sellers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(SELLER_UID).exists())
                {
                    Seller seller=dataSnapshot.child(SELLER_UID).getValue(Seller.class);
                    if(dataSnapshot.child(SELLER_UID).child("sellerImage").exists())
                        Picasso.get().load(seller.getSellerImage()).into(sellerProfileImage);
                    else  if(dataSnapshot.child(SELLER_UID).child("shopImage").exists())
                        Picasso.get().load(seller.getShopImage()).into(sellerProfileImage);
                    tvHeading.setText(seller.getSellerName()+" shop");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.shop_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_about:
                Toast.makeText(ShopToUserActivity.this,"about seller",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_send_email:
                mDataRef.child("sellers").child(SELLER_UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Intent intent=new Intent(Intent.ACTION_SEND);

                        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{dataSnapshot.child("sellerEmail").getValue(String.class)});
                        intent.putExtra(Intent.EXTRA_SUBJECT,"Email by ApniShuvidha");
                        intent.setType("message/rfc822");
                        if(ContextCompat.checkSelfPermission(ShopToUserActivity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(ShopToUserActivity.this,new String[]{Manifest.permission.SEND_SMS},0);
                        }
                        else{
                            try {
                                startActivity(Intent.createChooser(intent,"Chooses an email client"));
                            }
                            catch (Exception e){
                                Toast.makeText(ShopToUserActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
        }
        return true;
    }
}
