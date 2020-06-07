package seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apnishuvidha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import model.CustumProgress;

public class AddNewProduct extends AppCompatActivity {
    EditText etProductName,etProductPrice,etProductDes;
    ImageView imgProduct;

    FirebaseAuth mAuth;
    DatabaseReference mDataRef;
    StorageReference mDataStore;

    int GALLARY_CODE=1;
    Uri uri=null;

    CustumProgress progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        etProductDes=findViewById(R.id.et_product_description);
        etProductName=findViewById(R.id.et_product_name);
        etProductPrice=findViewById(R.id.et_product_price);
        imgProduct=findViewById(R.id.img_product);

        mAuth=FirebaseAuth.getInstance();
        mDataRef= FirebaseDatabase.getInstance().getReference();
        mDataStore=FirebaseStorage.getInstance().getReference();

        progress=new CustumProgress(AddNewProduct.this);
    }
    public  void openGallery(View view)
    {
        Intent intent=new Intent();
        intent.setType("Image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,GALLARY_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==GALLARY_CODE)
            {
                uri=data.getData();
                imgProduct.setImageURI(uri);

            }
        }
    }

    public void addProduct(View view)
    {
        final String pname=etProductName.getText().toString();
        final String price=etProductPrice.getText().toString();
        final String pdes=etProductDes.getText().toString();
        if(uri==null)
        {
            Toast.makeText(AddNewProduct.this,"first select a product image",Toast.LENGTH_LONG).show();

        }
        else if(TextUtils.isEmpty(pname))
        {
            Toast.makeText(AddNewProduct.this,"product name can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(price))
        {
            Toast.makeText(AddNewProduct.this,"product price can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(pdes))
        {
            Toast.makeText(AddNewProduct.this,"product description can't be empty",Toast.LENGTH_LONG).show();
        }
        else {
            progress.startProgressBar("product adding...");
            final StorageReference filePath=mDataStore.child("images").child(mAuth.getCurrentUser().getUid()+uri.getLastPathSegment());
            filePath.putFile(uri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.closeProgressBar();
                    Toast.makeText(AddNewProduct.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String  PRODUCT_IMAGE = uri.toString();
                            HashMap<String,Object>hashMap=new HashMap<>();
                            hashMap.put("pname",pname);
                            hashMap.put("price",price);
                            hashMap.put("pdescription",pdes);
                            hashMap.put("sellerUid",mAuth.getCurrentUser().getUid());
                            hashMap.put("pimage",PRODUCT_IMAGE);
                            mDataRef.child("products").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        progress.closeProgressBar();
                                        Toast.makeText(AddNewProduct.this,"product uploaded successfully",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        progress.closeProgressBar();
                                        Toast.makeText(AddNewProduct.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }
                    });




                }
            });
        }
    }
}
