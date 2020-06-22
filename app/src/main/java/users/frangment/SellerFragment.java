package users.frangment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apnishuvidha.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import model.Prevlent;
import model.Seller;
import users.ShopProducts;
import viewHolder.SellerViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerFragment extends Fragment {
    FirebaseRecyclerAdapter<Seller, SellerViewHolder> adapter;
    RecyclerView recyclerView;

    DatabaseReference mDataRef;

    String SELECTED_CATAGROY="clothes";

    public SellerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_seller, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mDataRef= FirebaseDatabase.getInstance().getReference();
        try {
            SELECTED_CATAGROY = getArguments().getString(Prevlent.catagory);
        }
        catch (Exception e)
        {
            Log.v("EXCEPTION",e.getMessage());
        }

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mDataRef.child("sellers").orderByChild("shopCatagory").equalTo(SELECTED_CATAGROY);

        FirebaseRecyclerOptions<Seller> options = new FirebaseRecyclerOptions.Builder<Seller>()
                .setQuery(query, Seller.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Seller, SellerViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull SellerViewHolder holder, int position, @NonNull final Seller model) {
                holder.setTvShopStatus(model.getShopStatus());
                holder.setTvSellerName(model.getSellerName());
                holder.setTvSellerEmail(model.getSellerEmail());
                holder.setTvSellerPhone(model.getSellerPhone());
                holder.setTvShopAddress(model.getShopAddress());
                holder.tvSellerPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+model.getSellerPhone()));
                        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},0);
                        }
                        else{
                            try {
                                startActivity(intent);
                            }
                               catch (Exception e){
                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
                holder.tvSellerEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_SEND);

                        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{model.getSellerEmail()});
                        intent.putExtra(Intent.EXTRA_SUBJECT,"Email by ApniShuvidha");
                        intent.setType("message/rfc822");
                        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},0);
                        }
                        else{
                            try {
                                startActivity(Intent.createChooser(intent,"Chooses an email client"));
                            }
                            catch (Exception e){
                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                holder.tvShopAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url="http://maps.google.com/maps?daddr="+model.getShopAddress();
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                        startActivity(intent);
                    }
                });

                holder.imgPressHere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(), ShopProducts.class);
                        intent.putExtra(Prevlent.sellerUid, model.getSellerUid());
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_layout, parent, false);

                return new SellerViewHolder(view);
            }


        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
