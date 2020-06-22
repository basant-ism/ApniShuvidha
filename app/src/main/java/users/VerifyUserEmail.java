package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apnishuvidha.MainActivity;
import com.example.apnishuvidha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.GenericDeclaration;

public class VerifyUserEmail extends AppCompatActivity {
    TextView tvSetEmail;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user_email);
        mAuth=FirebaseAuth.getInstance();

        tvSetEmail=findViewById(R.id.tv_verification_line2);
        tvSetEmail.setText("A verifiaction link send to your email"+mAuth.getCurrentUser().getEmail());
    }
    public  void resendLink(View view)
    {
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(VerifyUserEmail.this,"An email sent to your email id please verify it...",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(VerifyUserEmail.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void goToLoginActivity(View view)
    {
        Intent intent=new Intent(VerifyUserEmail.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
