package Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.covidtracking.MainActivity;
import com.example.covidtracking.R;
import com.example.covidtracking.activity.HomeActivity;
import com.example.covidtracking.activity.SignUpActivity;
import com.example.covidtracking.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import static com.example.covidtracking.Config.mAuth;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        initListener();

    }


    String userEmail,userPass;

    private void initListener()
    {
        binding.createAcLoginPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent( Login.this, SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });

        binding.loginBtnId.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                virafication();

            }
        });

    }

    private void virafication() {
        userEmail = binding.loginUserid.getText().toString().trim();
        userPass = binding.loginPassword.getText().toString().trim();


        if(userEmail.isEmpty()){
            binding.loginUserid.setError("Please Enter Your Mail");
           return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){

            binding.loginUserid.setError("Please Enter valid mail");
            binding.loginUserid.requestFocus();
            return;
        }



        else if(userPass.isEmpty()){
            binding.loginPassword.setError("please Enter Password");
            return;
        }else if(userPass.length()<6){
            binding.loginPassword.setError("Password must contain 6  characters");
            binding.loginPassword.requestFocus();
        }

        else {
            logInMethod();
        }

    }

    private void logInMethod() {
        mAuth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {



                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(Login.this, HomeActivity.class));
                            finish();
                         //   updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                           Toast.makeText(Login.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }
                    }
                });
    }
}