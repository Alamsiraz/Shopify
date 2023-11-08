package com.example.clickit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.clickit.Model.Users;
import com.example.clickit.Prevalent.Prevalent;
import com.example.clickit.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding activityLoginBinding;
    private ProgressDialog loadingBar;
    private  String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);

        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());
        loadingBar = new ProgressDialog(this);
        activityLoginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
        activityLoginBinding.adminpanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityLoginBinding.loginBtn.setText("Login Admin");
                activityLoginBinding.adminpanel.setVisibility(View.INVISIBLE);
                activityLoginBinding.nonadminpanel.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });
        activityLoginBinding.nonadminpanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityLoginBinding.loginBtn.setText("Login");
                activityLoginBinding.adminpanel.setVisibility(View.VISIBLE);
                activityLoginBinding.nonadminpanel.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private void LoginUser() {
        String number = activityLoginBinding.loginNumber.getText().toString();
        String password = activityLoginBinding.loginPAssword.getText().toString();
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "Please enter your number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login  Account");
            loadingBar.setMessage("Please wait while we are checking your Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            AllowAccessToAccount(number, password);

        }
    }

    private void AllowAccessToAccount(String number, String password) {
        if (activityLoginBinding.cbRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserNumberKey,number);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(number).exists())
                {
                    Users userData = snapshot.child(parentDbName).child(number).getValue(Users.class);
                    if (userData.getNumber().equals(number))
                    {
                        if (userData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Admins")) {
                                Toast.makeText(LoginActivity.this, "Welcome Admin,Lets add", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users")) {

                                Toast.makeText(LoginActivity.this, "Log In Succesfull", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }

                        }

                        else {
                            Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
            }
else {
                    Toast.makeText(LoginActivity.this, "Account with this"+number+"number dosen't exists", Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this, "Create a new Account", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}