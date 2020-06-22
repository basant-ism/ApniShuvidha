package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apnishuvidha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import model.Prevlent;
import model.User;

public class VerifyPhoneNo extends AppCompatActivity {
    EditText etVerifyCode;
    ProgressBar progressBar;
    String veryficationCodeBySystem;
    String phoneNo,name,password;
    SharedPreferences sp;
    DatabaseReference mDataRef;
    TextView tvOtpPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);

        phoneNo=getIntent().getStringExtra("phoneNo");
        name=getIntent().getStringExtra("name");
        password=getIntent().getStringExtra("password");


        etVerifyCode=findViewById(R.id.et_verify_code);
        progressBar=findViewById(R.id.progress_bar);
        tvOtpPhone=findViewById(R.id.tv_otp_number);
        tvOtpPhone.setText("An OTP sent to "+phoneNo);
        tvOtpPhone.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        sp=getSharedPreferences("MyPreferance",MODE_PRIVATE);

        mDataRef= FirebaseDatabase.getInstance().getReference();


        sendVerificationCodeToUser(phoneNo);

    }

    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNo,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            veryficationCodeBySystem=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }



        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneNo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    private void verifyCode(String code)
    {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(veryficationCodeBySystem,code);
        signInToUser(credential);
    }

    private void signInToUser(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(VerifyPhoneNo.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    final User user=new User();
                    HashMap<String,Object>hashMap=new HashMap<>();
                    hashMap.put("name",name);
                    hashMap.put("phone",phoneNo);
                    hashMap.put("password",password);
                    mDataRef.child("users").child(phoneNo).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {

                                sp.edit().putBoolean("logged",true).apply();
                                sp.edit().putString("name",name).apply();
                                sp.edit().putString("phone",phoneNo).apply();
                                sp.edit().putString("password",password).apply();
                                Prevlent.currentUser=user;
                                Intent intent=new Intent(VerifyPhoneNo.this,HomePage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else
                            {
                                progressBar.setVisibility(View.GONE);
                                tvOtpPhone.setVisibility(View.VISIBLE);
                                Toast.makeText(VerifyPhoneNo.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    tvOtpPhone.setVisibility(View.VISIBLE);
                    Toast.makeText(VerifyPhoneNo.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }


        });
    }
    public void verifyInputCode(View view)
    {

        String code=etVerifyCode.getText().toString();
        if(TextUtils.isEmpty(code))
        {
            Toast.makeText(VerifyPhoneNo.this,"enter the OTP",Toast.LENGTH_LONG).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        tvOtpPhone.setVisibility(View.GONE);
         progressBar.setVisibility(View.VISIBLE);
        verifyCode(code);
    }
    public void reSendOTP(View view)
    {
        sendVerificationCodeToUser(phoneNo);
        Toast.makeText(VerifyPhoneNo.this,"otp sent to your phone number",Toast.LENGTH_LONG).show();
    }
}
