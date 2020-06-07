package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import model.Prevlent;
import model.User;

import static android.view.View.GONE;

public class UserLogin extends AppCompatActivity {
    EditText etPhone,etPassword;
    TextView tvExist,tvWrongPassword;
    DatabaseReference mDataRef;
    SharedPreferences sp;

    CustumProgress dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        dialog=new CustumProgress(UserLogin.this);

        etPhone=findViewById(R.id.et_user_phone);
        etPassword=findViewById(R.id.et_user_password);
        tvExist=findViewById(R.id.tv_exist);
        tvWrongPassword=findViewById(R.id.tv_wrong_password);

        sp=getSharedPreferences("MyPreferance",MODE_PRIVATE);

        tvExist.setVisibility(GONE);
        tvWrongPassword.setVisibility(GONE);

        mDataRef= FirebaseDatabase.getInstance().getReference();
    }
    public void userSignIn(View view)
    {
        tvWrongPassword.setVisibility(GONE);
        tvExist.setVisibility(GONE);
        final String phone=etPhone.getText().toString();
        final String password=etPassword.getText().toString();
        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(UserLogin.this,"enter phone number",Toast.LENGTH_LONG).show();
        }
        else  if(TextUtils.isEmpty(password))
        {
            Toast.makeText(UserLogin.this,"password can't not empty",Toast.LENGTH_LONG).show();
        }
        else {
            dialog.startProgressBar("user login please wait...");
            mDataRef.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(phone).exists()) {
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        if (password.equals(user.getPassword())) {
                            sp.edit().putBoolean("logged",true).apply();
                            sp.edit().putString("name",user.getName()).apply();
                            sp.edit().putString("phone",phone).apply();
                            sp.edit().putString("password",password).apply();
                            Prevlent.currentUser=user;
                            Intent intent=new Intent(UserLogin.this,HomePage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            dialog.closeProgressBar();
                            startActivity(intent);
                            finish();
                        } else {
                            tvWrongPassword.setVisibility(View.VISIBLE);
                            dialog.closeProgressBar();
                        }
                    } else {
                        tvExist.setVisibility(View.VISIBLE);
                        dialog.closeProgressBar();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                            dialog.closeProgressBar();
                }
            });
        }

    }
    public void resetPassword(View view)
    {

    }
}
