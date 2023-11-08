package com.example.clickit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clickit.Model.Users;
import com.example.clickit.Prevalent.Prevalent;
import com.example.clickit.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.mainLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        binding.mainJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        String UserNumberKey = Paper.book().read(Prevalent.UserNumberKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if (UserNumberKey != "" && UserPasswordKey !="")
        {
            if (!TextUtils.isEmpty(UserNumberKey)&& !TextUtils.isEmpty(UserPasswordKey) )
            {
                AllowAccess(UserNumberKey,UserPasswordKey);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please wait... ");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }

    private void AllowAccess(final String number, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(number).exists())
                {
                    Users userData = snapshot.child("Users").child(number).getValue(Users.class);
                    if (userData.getNumber().equals(number))
                    {
                        if (userData.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this, "Logging In", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent );
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Account with this"+number+"number dosen't exists", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Create a new Account", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}