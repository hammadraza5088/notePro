package com.example.notepro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText,passwordEditText;
    Button loginBtn;
    ProgressBar progressbar;
    TextView createAccountBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText=findViewById(R.id.email_edit_text);
        passwordEditText=findViewById(R.id.password_edit_text);

        loginBtn=findViewById(R.id.login_btn);
        progressbar=findViewById(R.id.progress_bar);
        createAccountBtnTextView= findViewById(R.id.create_account_text_view_btn);
    loginBtn.setOnClickListener((v)-> loginUser( ));
createAccountBtnTextView.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class
)));

    }
    void loginUser(){
        String email = emailEditText.getText().toString();
        String password = emailEditText.getText().toString();

        boolean isValidated = validateData(email,password);

        if(!isValidated){
            return;
        }
        loginAccountInFirebase(email,password);

    }
    void loginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
        channgeInprogress(true);
       firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
channgeInprogress(false);
if(task.isSuccessful()){
    //login success
    if(firebaseAuth.getCurrentUser().isEmailVerified()){
        //go to main activity
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    } else {
      Utility.showToast(LoginActivity.this,"Email is not verified, please verify your email");

    }
} else {
     Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
}
           }
       });
    }
    void channgeInprogress(boolean inProgress){
        if(inProgress){
            progressbar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }
        else {
            progressbar.setVisibility(View.GONE);
           loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String passsword ){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if(passsword.length()<6){
            passwordEditText.setError("Password length is invalid");
            return false;
        }

        return true;
    }
}