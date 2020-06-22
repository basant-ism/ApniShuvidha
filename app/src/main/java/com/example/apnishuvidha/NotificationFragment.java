package com.example.apnishuvidha;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    SwitchCompat switchYes;
    DatabaseReference mDataRef;
    FirebaseAuth mAuth;
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        mDataRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        setButton();

        switchYes=view.findViewById(R.id.switch_yes);
        switchYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchYes.isChecked())
                {
                    OneSignal.setSubscription(true);
                    mDataRef.child("users").child(mAuth.getUid()).child("isSubscribed").setValue(true);
                }
                else
                {
                    OneSignal.setSubscription(false);
                    mDataRef.child("users").child(mAuth.getUid()).child("isSubscribed").setValue(false);
                }
            }
        });
        return view;
    }

    private void setButton(){
        mDataRef.child("users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("isSubscribed").exists())
                {
                    if(dataSnapshot.child("isSubscribed").getValue(boolean.class))
                    {
                        OneSignal.setSubscription(true);
                        switchYes.setChecked(true);
                    }
                    else
                    {
                        OneSignal.setSubscription(false);
                        switchYes.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
