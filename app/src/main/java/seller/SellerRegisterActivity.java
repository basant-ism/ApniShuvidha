package seller;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apnishuvidha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import model.CustumProgress;
import model.Prevlent;

public class SellerRegisterActivity extends AppCompatActivity {
    TextView tvExist;
    EditText etSellerName,etShopAddress,etSellerPhone,etSellerPassword,etSellerEmail;

    String SELLER_CATAGORY="";

    FirebaseAuth mAuth;
    DatabaseReference mDataRef;

    CustumProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        dialog=new CustumProgress(SellerRegisterActivity.this);

        tvExist=findViewById(R.id.tv_exist);
        etSellerName=findViewById(R.id.et_seller_name);
        etSellerPassword=findViewById(R.id.et_seller_password);
        etSellerPhone=findViewById(R.id.et_seller_phone);
        etShopAddress=findViewById(R.id.et_shop_adderss);
        etSellerEmail=findViewById(R.id.et_seller_email);

        SELLER_CATAGORY=getIntent().getStringExtra(Prevlent.catagory);

        mAuth=FirebaseAuth.getInstance();
        mDataRef= FirebaseDatabase.getInstance().getReference();

    }
    public  void sellerSignUp(View view)
    {
        final String name=etSellerName.getText().toString();
        final  String password=etSellerPassword.getText().toString();
        final String phone=etSellerPhone.getText().toString();
        final String address=etShopAddress.getText().toString();
        final String sellerEmail=etSellerEmail.getText().toString();
        if(TextUtils.isEmpty(name))
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
                       DatabaseReference mChildRef=mDataRef.child("sellers").child(mAuth.getCurrentUser().getUid()) ;
                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("shopCatagory",SELLER_CATAGORY);
                        hashMap.put("shopStatus","open");
                        hashMap.put("sellerName",name);
                        hashMap.put("shopAddress",address);
                        hashMap.put("sellerPhone",phone);
                        hashMap.put("sellerEmail",sellerEmail);
                        hashMap.put("sellerUid",mAuth.getCurrentUser().getUid());
                        mChildRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Intent intent=new Intent(SellerRegisterActivity.this,SellerLoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra(Prevlent.catagory,SELLER_CATAGORY);
                                    dialog.closeProgressBar();
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
        Intent intent=new Intent(SellerRegisterActivity.this, SellerLoginActivity.class);
        intent.putExtra(Prevlent.catagory,SELLER_CATAGORY);
        startActivity(intent);

    }
}
