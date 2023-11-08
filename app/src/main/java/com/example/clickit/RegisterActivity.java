package com.example.clickit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.TextUtilsCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.clickit.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private ProgressDialog loadingBar;

ActivityRegisterBinding activityRegisterBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterBinding.getRoot());
        loadingBar = new ProgressDialog( this);
        activityRegisterBinding.CreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
        
    }

    private void CreateAccount() {
        String name = activityRegisterBinding.RegisterName.getText().toString();
        String number = activityRegisterBinding.RegisterNumber.getText().toString();
        String password = activityRegisterBinding.RegisterPassword.getText().toString();
        String confirm_password = activityRegisterBinding.ConfirmPassword.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(number))
        {
            Toast.makeText(this, "Please enter your number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else if (password.length()<5) {
            Toast.makeText(this, "Password Should not be less than 5 letters", Toast.LENGTH_SHORT).show();

        }
        else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while we are checking your Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidatephoneNumber(name,number,password);

        }
    }

    private void ValidatephoneNumber(String name, String number, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(number).exists())){
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("number",number);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name) ;
                    RootRef.child("Users").child(number).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Congratulations your account has been Succesfully Created", Toast.LENGTH_SHORT).show();
                                   loadingBar.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Error 404 : Try Again After Some Time", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This "+number+"is already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


