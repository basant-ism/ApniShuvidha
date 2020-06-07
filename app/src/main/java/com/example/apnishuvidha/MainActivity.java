package com.example.apnishuvidha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import model.CustumProgress;
import model.Prevlent;
import model.User;
import seller.SellerRegisterActivity;
import users.HomePage;
import users.UserLogin;
import users.UserRegistration;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


     DrawerLayout drawer;
     SharedPreferences sp;

     CustumProgress dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog=new CustumProgress(MainActivity.this);


        sp=getSharedPreferences("MyPreferance",MODE_PRIVATE);
        if(sp.getBoolean("logged",false))
        {
            //dialog.startProgressBar("app loading...");
            String name=sp.getString("name","XXXX");
            String phone=sp.getString("phone","999999999");
            String password=sp.getString("password","1111111");
            User user=new User(name,phone,password);
            Prevlent.currentUser=user;
            Intent intent=new Intent(MainActivity.this, HomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //dialog.closeProgressBar();
            finish();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);



        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_open);
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.nav_clothes:
                Intent intent1=new Intent(MainActivity.this, SellerRegisterActivity.class);
                intent1.putExtra(Prevlent.catagory,"clothes");
                startActivity(intent1);
                //Toast.makeText(MainActivity.this,"clothes",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_general_store:
                Intent intent2=new Intent(MainActivity.this, SellerRegisterActivity.class);
                intent2.putExtra(Prevlent.catagory,"general_store");
                startActivity(intent2);
                break;
            case R.id.nav_medical_store:
                Intent intent3=new Intent(MainActivity.this, SellerRegisterActivity.class);
                intent3.putExtra(Prevlent.catagory,"medical_store");
                startActivity(intent3);
                break;
            case R.id.nav_taksi:
                Toast.makeText(MainActivity.this,"taksi",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_raj_mistri:
                Toast.makeText(MainActivity.this,"rajmistri",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_tent:
                Toast.makeText(MainActivity.this,"tent",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_bus:
                Toast.makeText(MainActivity.this,"bus",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_train:
                Toast.makeText(MainActivity.this,"train",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_admin:
                Toast.makeText(MainActivity.this,"admin",Toast.LENGTH_LONG).show();
                break;

        }
        drawer.closeDrawers();
        return true;
    }
    public void signIn(View view)
    {
        Intent intent=new Intent(MainActivity.this, UserLogin.class);
        startActivity(intent);
    }
    public void registration(View view)
    {
        Intent intent=new Intent(MainActivity.this, UserRegistration.class);
        startActivity(intent);
    }
}
