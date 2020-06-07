package seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apnishuvidha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import model.CustumProgress;
import model.Prevlent;

public class SellerLoginActivity extends AppCompatActivity {
    String SELLER_CATAGORY;
    FirebaseAuth mAuth;
    EditText etSellerEmail,etSellerPassword;

    CustumProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        dialog=new CustumProgress(SellerLoginActivity.this);

        mAuth=FirebaseAuth.getInstance();
        SELLER_CATAGORY=getIntent().getStringExtra(Prevlent.catagory);

        etSellerEmail=findViewById(R.id.et_seller_email);
        etSellerPassword=findViewById(R.id.et_seller_password);
    }
    public  void sellerSignIn(View view)
    {
        final String email=etSellerEmail.getText().toString();
        final String password=etSellerPassword.getText().toString();
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
            dialog.startProgressBar("seller login please wait...");
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Intent intent=new Intent(SellerLoginActivity.this, SellerHomePage.class);
                        intent.putExtra(Prevlent.catagory,SELLER_CATAGORY);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        dialog.closeProgressBar();
                        startActivity(intent);
                    }
                    else
                    {
                        dialog.closeProgressBar();
                        Toast.makeText(SellerLoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    public  void resetPassword(View view)
    {

    }
}
