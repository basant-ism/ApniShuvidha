package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.apnishuvidha.MainActivity;
import com.example.apnishuvidha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import model.CustumProgress;
import model.Prevlent;
import seller.SellerLoginActivity;
import seller.SellerRegisterActivity;

public class UserRegistration extends AppCompatActivity {
    EditText etUserName,etUserEmail,etUserPassword;
    TextView tvExit;

    DatabaseReference mDataRef;
    FirebaseAuth mAuth;

    boolean TAG=false;

    CustumProgress dialog;

    Spinner countrySpinner,stateSpinner,citySpinner;
    String userCountry="Choose your county",userState="Choose your state",userCity="Choose your city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);


        countrySpinner=findViewById(R.id.country_spinner);
        stateSpinner=findViewById(R.id.state_spinner);
        citySpinner=findViewById(R.id.city_spinner);


        dialog=new CustumProgress(UserRegistration.this);
        mAuth=FirebaseAuth.getInstance();

        setInSpinner();

        etUserName=findViewById(R.id.et_user_name);
        etUserPassword=findViewById(R.id.et_user_password);
        etUserEmail=findViewById(R.id.et_user_email);
        tvExit=findViewById(R.id.tv_exist);

        mDataRef= FirebaseDatabase.getInstance().getReference();

        tvExit.setVisibility(View.GONE);


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
                userCountry=countryName[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                userState=stateName[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                userCity=cityName[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void userSignUp(View view)
    {
        final String name=etUserName.getText().toString();
        final  String password=etUserPassword.getText().toString();
        final String userEmail=etUserEmail.getText().toString();
        if(userCountry.equals("Choose your country"))
        {
            Toast.makeText(UserRegistration.this,"choose country name first",Toast.LENGTH_LONG).show();
        }
        else if(userState.equals("Choose your state"))
        {
            Toast.makeText(UserRegistration.this,"choose state name first",Toast.LENGTH_LONG).show();
        }
        else if(userCity.equals("Choose your city"))
        {
            Toast.makeText(UserRegistration.this,"choose city name first",Toast.LENGTH_LONG).show();
        }
       else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(UserRegistration.this,"name can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(userEmail))
        {
            Toast.makeText(UserRegistration.this,"email can't be empty",Toast.LENGTH_LONG).show();
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(UserRegistration.this,"password can't be empty",Toast.LENGTH_LONG).show();
        }
        else
        {
            dialog.startProgressBar("account creating please wait...");
            mAuth.createUserWithEmailAndPassword(userEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            etUserEmail.setText("");
                                            etUserName.setText("");
                                            etUserPassword.setText("");

                                            DatabaseReference mChildRef=mDataRef.child("users").child(mAuth.getCurrentUser().getUid()) ;
                                            HashMap<String,Object> hashMap=new HashMap<>();
                                            hashMap.put("userName",name);
                                            hashMap.put("userEmail",userEmail);
                                            hashMap.put("userCountry",userCountry);
                                            hashMap.put("userState",userState);
                                            hashMap.put("userCity",userCity);
                                            hashMap.put("userUid",mAuth.getCurrentUser().getUid());
                                            //dialog.closeProgressBar();
                                            Toast.makeText(UserRegistration.this,"An confirmation email link sent to your email id ",Toast.LENGTH_LONG).show();
                                            mDataRef.child("appUsers").child(mAuth.getUid()).setValue("user");
                                            mChildRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        Intent intent=new Intent(UserRegistration.this, MainActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        dialog.closeProgressBar();
                                                        Toast.makeText(UserRegistration.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                    }
                    else
                    {
                        dialog.closeProgressBar();
                        Toast.makeText(UserRegistration.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
    public void goToLoginActivity(View view)
    {
        Intent intent=new Intent(UserRegistration.this, MainActivity.class);
        startActivity(intent);
    }
}
