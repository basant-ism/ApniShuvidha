package com.example.apnishuvidha;
import com.onesignal.OneSignal;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import adapter.SliderAdapterExample;
import model.CustumProgress;
import model.Prevlent;
import model.SliderItem;
import model.User;
import seller.SellerCatagroy;
import seller.SellerHomePage;
import seller.SellerLoginActivity;
import seller.SellerRegisterActivity;
import users.HomePage;

import users.UserLogin;
import users.UserRegistration;
import users.VerifyUserEmail;

public class MainActivity extends AppCompatActivity  {


    // DrawerLayout drawer;

    FirebaseAuth mAuth;
    EditText etUserEmail,etUserPassword;
    DatabaseReference mDataRef;


     CustumProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();






                // OneSignal Initialization
                OneSignal.startInit(this)
                        .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                        .unsubscribeWhenNotificationsAreDisabled(true)
                        .init();


        dialog = new CustumProgress(MainActivity.this);

        etUserEmail = findViewById(R.id.et_user_email);
        etUserPassword = findViewById(R.id.et_user_password);

        setAdvertisementSlides();



//        FloatingActionButton fab = findViewById(R.id.fab);


//        drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
////        navigationView.setNavigationItemSelectedListener(this);
//
//
//        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawer,toolbar,
//                R.string.navigation_drawer_open,R.string.navigation_drawer_open);
//        drawer.setDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();

        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
            sendToActivity();
        }
    }





    private void sendToActivity()
    {
        mDataRef.child("appUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(mAuth.getUid()).exists()) {
                    String type = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(String.class);
                    Log.v("TAG",type);
                    Intent intent;
                    if(type.equals("user")) {
                        intent = new Intent(MainActivity.this,HomePage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                        else {
                        intent = new Intent(MainActivity.this, SellerHomePage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("sellerCategory", type);
                        startActivity(intent);
                    }


                        }




                else
                {
                    dialog.closeProgressBar();
                    Toast.makeText(MainActivity.this,"you can't Sign In",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

        List<SliderItem>list=new ArrayList<>();
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


    //    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//        switch (menuItem.getItemId())
//        {
//            case R.id.nav_clothes:
//                Intent intent1=new Intent(MainActivity.this, SellerRegisterActivity.class);
//                intent1.putExtra(Prevlent.catagory,"clothes");
//                startActivity(intent1);
//                //Toast.makeText(MainActivity.this,"clothes",Toast.LENGTH_LONG).show();
//                break;
//            case R.id.nav_general_store:
//                Intent intent2=new Intent(MainActivity.this, SellerRegisterActivity.class);
//                intent2.putExtra(Prevlent.catagory,"general_store");
//                startActivity(intent2);
//                break;
//            case R.id.nav_medical_store:
//                Intent intent3=new Intent(MainActivity.this, SellerRegisterActivity.class);
//                intent3.putExtra(Prevlent.catagory,"medical_store");
//                startActivity(intent3);
//                break;
//            case R.id.nav_taksi:
//                Toast.makeText(MainActivity.this,"taksi",Toast.LENGTH_LONG).show();
//                break;
//            case R.id.nav_raj_mistri:
//                Toast.makeText(MainActivity.this,"rajmistri",Toast.LENGTH_LONG).show();
//                break;
//            case R.id.nav_tent:
//                Toast.makeText(MainActivity.this,"tent",Toast.LENGTH_LONG).show();
//                break;
//            case R.id.nav_bus:
//                Toast.makeText(MainActivity.this,"bus",Toast.LENGTH_LONG).show();
//                break;
//            case R.id.nav_train:
//                Toast.makeText(MainActivity.this,"train",Toast.LENGTH_LONG).show();
//                break;
//            case R.id.nav_admin:
//                Toast.makeText(MainActivity.this,"admin",Toast.LENGTH_LONG).show();
//                break;
//
//        }
//        drawer.closeDrawers();
//        return true;
//    }
    public void signIn(View view)
    {
        final String email=etUserEmail.getText().toString();
        final String password=etUserPassword.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "email can't be empty", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "password can't be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            dialog.startProgressBar("login please wait...");
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        if(mAuth.getCurrentUser().isEmailVerified())
                        {
                             sendToActivity();
                        }
                        else {
                            Intent intent=new Intent(MainActivity.this, VerifyUserEmail.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else
                    {
                        dialog.closeProgressBar();
                        Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public  void goRegisterActivity(View view)
    {
            Intent intent=new Intent(MainActivity.this,UserRegistration.class);
            startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.as_a_seller:
                Intent intent=new Intent(MainActivity.this, SellerCatagroy.class);
                startActivity(intent);
                break;
            case R.id.contacts:
                Toast.makeText(MainActivity.this,"contects",Toast.LENGTH_LONG).show();
                break;
            case R.id.about_us:
                Toast.makeText(MainActivity.this,"about us",Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    public void resetPassword(View view)
    {

    }
}
