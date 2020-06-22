package seller;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apnishuvidha.MainActivity;
import com.example.apnishuvidha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import model.CustumProgress;
import model.Prevlent;
import users.UserRegistration;

public class SellerRegisterActivity extends AppCompatActivity {
    TextView tvExist,tvCategoryName;
    EditText etSellerName,etShopAddress,etSellerPhone,etSellerPassword,etSellerEmail;
    ImageView imgCategory;

    String SELLER_CATAGORY="",CATEGORY_IMAGE="";

    FirebaseAuth mAuth;
    DatabaseReference mDataRef;

    CustumProgress dialog;

    Spinner countrySpinner,stateSpinner,citySpinner;
    String sellerCountry="Choose your county",sellerState="Choose your state",sellerCity="Choose your city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        mAuth=FirebaseAuth.getInstance();
        mDataRef= FirebaseDatabase.getInstance().getReference();

        dialog=new CustumProgress(SellerRegisterActivity.this);

        tvExist=findViewById(R.id.tv_exist);
        etSellerName=findViewById(R.id.et_seller_name);
        etSellerPassword=findViewById(R.id.et_seller_password);
        etSellerPhone=findViewById(R.id.et_seller_phone);
        etShopAddress=findViewById(R.id.et_shop_adderss);
        etSellerEmail=findViewById(R.id.et_seller_email);
        tvCategoryName=findViewById(R.id.tv_category_name);
        imgCategory=findViewById(R.id.img_register);

        countrySpinner=findViewById(R.id.country_spinner);
        stateSpinner=findViewById(R.id.state_spinner);
        citySpinner=findViewById(R.id.city_spinner);

        setInSpinner();

        SELLER_CATAGORY=getIntent().getStringExtra("categoryName");
        CATEGORY_IMAGE=getIntent().getStringExtra("categoryImage");

        Picasso.get().load(CATEGORY_IMAGE).into(imgCategory);
        tvCategoryName.setText(SELLER_CATAGORY);



    }
    private void setInSpinner()
    {

        final String []countryName;
        final String []stateName;
        final String []cityName;
        countryName=getResources().getStringArray(R.array.countryName);
        stateName=getResources().getStringArray(R.array.stateName);
        cityName=getResources().getStringArray(R.array.cityName);

        ArrayAdapter countryadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,countryName);
        countryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter stateadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,stateName);
        stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter cityadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,cityName);
        cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryadapter);
        stateSpinner.setAdapter(stateadapter);
        citySpinner.setAdapter(cityadapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sellerCountry=countryName[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sellerState=stateName[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sellerCity=cityName[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public  void sellerSignUp(View view)
    {
        final String name=etSellerName.getText().toString();
        final  String password=etSellerPassword.getText().toString();
        final String phone=etSellerPhone.getText().toString();
        final String address=etShopAddress.getText().toString();
        final String sellerEmail=etSellerEmail.getText().toString();
        if(sellerCountry.equals("Choose your country"))
        {
            Toast.makeText(SellerRegisterActivity.this,"choose country name first",Toast.LENGTH_LONG).show();
        }
        else if(sellerState.equals("Choose your state"))
        {
            Toast.makeText(SellerRegisterActivity.this,"choose state name first",Toast.LENGTH_LONG).show();
        }
        else if(sellerCity.equals("Choose your city"))
        {
            Toast.makeText(SellerRegisterActivity.this,"choose city name first",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(SellerRegisterActivity.this,"name can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(sellerEmail))
        {
            Toast.makeText(SellerRegisterActivity.this,"email can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(SellerRegisterActivity.this,"phone can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(address))
        {
            Toast.makeText(SellerRegisterActivity.this,"address can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(SellerRegisterActivity.this,"password can't be empty",Toast.LENGTH_LONG).show();
        }
        else
        {
            dialog.startProgressBar("account creating please wait...");
            mAuth.createUserWithEmailAndPassword(sellerEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    DatabaseReference mChildRef=mDataRef.child("sellers").child(mAuth.getCurrentUser().getUid()) ;
                                    HashMap<String,Object>hashMap=new HashMap<>();
                                    hashMap.put("shopCatagory",SELLER_CATAGORY);
                                    hashMap.put("shopStatus","open");
                                    hashMap.put("sellerName",name);
                                    hashMap.put("shopAddress",address);
                                    hashMap.put("sellerPhone",phone);
                                    hashMap.put("sellerEmail",sellerEmail);
                                    hashMap.put("sellerUid",mAuth.getCurrentUser().getUid());
                                    hashMap.put("sellerCountry",sellerCountry);
                                    hashMap.put("sellerState",sellerState);
                                    hashMap.put("sellerCity",sellerCity);


                                    mDataRef.child("appUsers").child(mAuth.getUid()).setValue(SELLER_CATAGORY);

                                    Toast.makeText(SellerRegisterActivity.this,"An verification email said to your email id...",Toast.LENGTH_LONG).show();
                                    mChildRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Intent intent=new Intent(SellerRegisterActivity.this,MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                dialog.closeProgressBar();
                                                Toast.makeText(SellerRegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });


                                }
                                else {
                                    dialog.closeProgressBar();
                                    Toast.makeText(SellerRegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                        });

                    }
                    else
                    {
                        dialog.closeProgressBar();
                        Toast.makeText(SellerRegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
    public void goToLoginActivity(View view)
    {
        Intent intent=new Intent(SellerRegisterActivity.this, MainActivity.class);
        startActivity(intent);

    }
}
