package com.example.covidtracking.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.covidtracking.R;
import com.example.covidtracking.databinding.ActivitySignUpBinding;
import com.example.covidtracking.model.UserProfileInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.covidtracking.Config.database;
import static com.example.covidtracking.Config.mAuth;
import static com.example.covidtracking.Config.myRef;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initFunction();
        initListener();
    }

    private void initFunction() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    private void initListener() {

        binding.userConfirmPasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!binding.userPasswordET.getText().toString().equals(s)) {
                    binding.userConfirmPasswordET.setError("Wrong");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verification()) {
                    boolean check = binding.rbCategoryPatient.isChecked();

                    if (check) {
                        questionDialog();
                    } else {

                            registerAccount();
                    }
                }
            }
        });

        binding.seeTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("Term and Condition");
                builder.setMessage("Terms and Conditions agreements act as a legal contract between you (the company) who has the website or mobile app and the user who access your website and mobile app. Having a Terms and Conditions agreement is completely optional. No laws require you to have one. Not even the super-strict and wide-reaching General Data Protection Regulation .");
                builder.show();
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                    }
                });
                //   builder.setTitle("Custom AlertDialog");

            }
        });
    }

    private void questionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);

        //   builder.setTitle("Custom AlertDialog");


/*
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SignUpActivity.this,"Get Started!",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });*/

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.question_alertdialog, null);
        builder.setView(dialoglayout);

        AlertDialog dialog = builder.create();


        Button ok, cancel;

        ok = dialoglayout.findViewById(R.id.ok_custom_alart);
        cancel = dialoglayout.findViewById(R.id.cancel_custom_alart);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //  builder.show();
        dialog.show();
    }

    private void registerAccount() {
        mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            String userId = userEmail.toString().split("@")[0];
                            String gender = "male", category;
                            if (binding.maleRB.isSelected()) {
                                gender = "male";
                            } else if (binding.femaleRB.isSelected()) {
                                gender = "female";
                            } else if (binding.otherRB.isSelected()) {
                                gender = "other";
                            }

                            if (binding.rbCategoryPatient.isSelected()) {
                                category = "patient";

                            } else {
                                category = "normal";
                            }


                            UserProfileInfo userInfo = new UserProfileInfo(userId, userName, "", binding.userPhoneET.getText().toString(), gender, category);
                            myRef.child("User").child(userId).setValue(userInfo);

                            // Sign in success, update UI with the signed-in user's information
                            // Log.d("Signup", "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                finish();
                            }
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                          ///  Log.w("Signup", task.getResult().toString()+" "+task.getException().toString(), task.getException());
                       // Log.d("Signup", task.getException().getMessage());
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            //   updateUI(null);
                        }
                    }
                });
    }

    String userName, userPass, userEmail, userPhone, userConfirmPass;

    private boolean verification() {
        userName = binding.userNameET.getText().toString();
        userPass = binding.userPasswordET.getText().toString();
        userEmail = binding.userEmailET.getText().toString();
        userPhone = binding.userPhoneET.getText().toString();
        userConfirmPass = binding.userConfirmPasswordET.getText().toString();

        boolean condition = binding.userCondition.isChecked();

        if (userName.isEmpty()) {
            binding.userNameET.setError("Please Enter Your Name");
            binding.userNameET.requestFocus();
            return false;
        } else if (userEmail.isEmpty()) {
            binding.userEmailET.setError("Please Enter Your Email");
            binding.userEmailET.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            binding.userEmailET.setError("Please Enter your Valid Email ");
            binding.userEmailET.requestFocus();
            return false;
        } else if (userPhone.isEmpty()) {
            binding.userPhoneET.setError("Please Enter Your  Phone Number");
            binding.userPhoneET.requestFocus();
            return false;
        } else if (userPhone.length() < 11) {
            binding.userPhoneET.setError("Please Enter Your valid Phone Number");
            binding.userPhoneET.requestFocus();
            return false;
        } else if (userPass.isEmpty()) {
            binding.userPasswordET.setError("Please Enter Password");
            binding.userPasswordET.requestFocus();
            return false;
        } else if (userPass.length() < 6) {
            binding.userPasswordET.setError("Password must contain 6 characters");
            binding.userPasswordET.requestFocus();
            return false;
        } else if (userConfirmPass.isEmpty()) {
            binding.userConfirmPasswordET.setError("please Enter Confirm Password");
            binding.userConfirmPasswordET.requestFocus();
            return false;
        } else if (!userPass.equals(binding.userConfirmPasswordET.getText().toString())) {
            binding.userConfirmPasswordET.setError("Password Not Matched");
            binding.userConfirmPasswordET.requestFocus();
            return false;
        }
        else if(!condition)
        {
            Toast.makeText(this, "Please Confirm Term Condition", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (userEmail.isEmpty() || userPass.isEmpty()) {
            Toast.makeText(this, "Please fil up the field !!", Toast.LENGTH_SHORT).show();
            return false;
        }
       /* else if (!userPass.equals(binding.userConfirmPasswordET.getText().toString()))
        {
            Toast.makeText(this, "Please Confirm the password !!", Toast.LENGTH_SHORT).show();

            return false;
        }*/

        return true;
    }


}