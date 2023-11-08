package com.example.clickit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.clickit.databinding.ActivityAdminCategoryBinding;

public class AdminCategoryActivity extends AppCompatActivity {
ActivityAdminCategoryBinding adminCategoryBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminCategoryBinding = ActivityAdminCategoryBinding.inflate(getLayoutInflater());
        setContentView(adminCategoryBinding.getRoot());
        adminCategoryBinding.tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddingProductActivity.class);
                intent.putExtra("category","tshirsts");
                startActivity(intent);
            }
        });
        adminCategoryBinding.sportsTShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddingProductActivity.class);
                intent.putExtra("category","Sports thirsts");
                startActivity(intent);
            }
        });
        adminCategoryBinding.femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddingProductActivity.class);
                intent.putExtra("category","female dresses");
                startActivity(intent);
            }
        });
        adminCategoryBinding.sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddingProductActivity.class);
                intent.putExtra("category","sweaters");
                startActivity(intent);
            }
        });
        adminCategoryBinding.glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddingProductActivity.class);
                intent.putExtra("category","glasses");
                startActivity(intent);
            }
        });
        adminCategoryBinding.hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddingProductActivity.class);
                intent.putExtra("category","hats");
                startActivity(intent);
            }
        });
        adminCategoryBinding.purses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddingProductActivity.class);
                intent.putExtra("category","purses");
                startActivity(intent);
            }
        });
        adminCategoryBinding.shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddingProductActivity.class);
                intent.putExtra("category","shoes");
                startActivity(intent);
            }
        });
        adminCategoryBinding.headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddingProductActivity.class);
                intent.putExtra("category","headphones");
                startActivity(intent);
            }
        });
        adminCategoryBinding.laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddingProductActivity.class);
                intent.putExtra("category","laptops");
                startActivity(intent);
            }
        });
        adminCategoryBinding.watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddingProductActivity.class);
                intent.putExtra("category","watches");
                startActivity(intent);
            }
        });
        adminCategoryBinding.mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddingProductActivity.class);
                intent.putExtra("category","mobiles");
                startActivity(intent);
            }
        });












    }
}