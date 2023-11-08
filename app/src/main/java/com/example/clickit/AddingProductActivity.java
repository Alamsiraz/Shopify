package com.example.clickit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.clickit.databinding.ActivityAddingProductBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddingProductActivity extends AppCompatActivity {
    ActivityAddingProductBinding addingProductBinding;
    private   static  final  int GalleryPick = 1;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    private Uri ImageUri;
public String CategoryName, Description,Price,Pname,saveCurrentDate,saveCurrentTime,productRandomKey,downloadImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addingProductBinding = ActivityAddingProductBinding.inflate(getLayoutInflater());
        setContentView(addingProductBinding.getRoot());
        loadingBar = new ProgressDialog(this);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");


        addingProductBinding.selectProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
addingProductBinding.addNewProduct.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        ValidateProductData();
    }
});


    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryPick&&resultCode == RESULT_OK&&data!=null)
        {
            ImageUri = data.getData();
            addingProductBinding.selectProductImage.setImageURI(ImageUri);

        }
    }
    private void ValidateProductData()
    {
        Description = addingProductBinding.productDiscription.getText().toString();
        Price = addingProductBinding.productPrice.getText().toString();
        Pname = addingProductBinding.productName.getText().toString();
        if (ImageUri ==null)
        {
            Toast.makeText(this, "Product Image is Required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Product Description is Required", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "Product Price is Required", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "Product Name is Required", Toast.LENGTH_SHORT).show();

        }
        else
        {
            StoreProductInformation();
        }


    }

    private void StoreProductInformation()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Please wait while we are adding new product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate =new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentdate.format(calendar.getTime());

        SimpleDateFormat currentTime =new SimpleDateFormat("HH :mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate+saveCurrentTime;

        StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment()+productRandomKey+".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddingProductActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddingProductActivity.this, "Image Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw  task.getException();

                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return  filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl  =task.getResult().toString();
                            Toast.makeText(AddingProductActivity.this, "Getting Product Image Url Succesfully", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDataBase();
                        }
                    }
                });
            }
        });


    }

    private void saveProductInfoToDataBase() {
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("discription", Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",CategoryName);
        productMap.put("price",Price);
        productMap.put("pname",Pname);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AddingProductActivity.this, AdminCategoryActivity.class);

                            loadingBar.dismiss();
                            Toast.makeText(AddingProductActivity.this, "Product is Added Succesfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddingProductActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                            
                        }
                    }
                });

    }
}