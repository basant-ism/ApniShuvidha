package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apnishuvidha.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.CustumProgress;

public class UserRegistration extends AppCompatActivity {
    EditText etName,etPhone,etPassword;
    DatabaseReference mDataRef;
    TextView tvExit;
    boolean TAG=false;

    CustumProgress dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        dialog=new CustumProgress(UserRegistration.this);

        etName=findViewById(R.id.et_user_name);
        etPassword=findViewById(R.id.et_user_password);
        etPhone=findViewById(R.id.et_user_phone);
        tvExit=findViewById(R.id.tv_exist);

        mDataRef= FirebaseDatabase.getInstance().getReference();

        tvExit.setVisibility(View.GONE);


    }
    public void userSignUp(View view)
    {
        final String phoneNo=etPhone.getText().toString();
        final String name=etName.getText().toString();
        final String password=etPassword.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(UserRegistration.this,"enter user name",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(phoneNo))
        {
            Toast.makeText(UserRegistration.this,"enter phone number",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(UserRegistration.this,"enter password",Toast.LENGTH_LONG).show();
        }
        else {
            dialog.startProgressBar("please wait...");
            mDataRef.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(phoneNo).exists()) {
                        tvExit.setVisibility(View.VISIBLE);
                        dialog.closeProgressBar();

                    } else
                    {
                        Intent intent = new Intent(UserRegistration.this, VerifyPhoneNo.class);
                        intent.putExtra("phoneNo", phoneNo);
                        intent.putExtra("name",name);
                        intent.putExtra("password",password);
                        dialog.closeProgressBar();
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    dialog.closeProgressBar();
                }
            });

        }
    }
    public void goToLoginActivity(View view)
    {
        Intent intent=new Intent(UserRegistration.this,UserLogin.class);
        startActivity(intent);
    }
}
