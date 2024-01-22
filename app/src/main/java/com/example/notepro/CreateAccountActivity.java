package com.example.notepro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {
    EditText emailEditText,passwordEditText,confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressbar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText=findViewById(R.id.email_edit_text);
        passwordEditText=findViewById(R.id.password_edit_text);
        confirmPasswordEditText=findViewById(R.id.confirm_password_edit_text);
        createAccountBtn=findViewById(R.id.create_account_btn);
        progressbar=findViewById(R.id.progress_bar);
        loginBtnTextView= findViewById(R.id.login_text_view_btn);


        createAccountBtn.setOnClickListener(v->createAccount());
        loginBtnTextView.setOnClickListener(v-> finish());


    }
    void createAccount(){
      String email = emailEditText.getText().toString();
        String password = emailEditText.getText().toString();
        String confirmPassword= emailEditText.getText().toString();
        boolean isValidated = validateData(email,password,confirmPassword);

        if(!isValidated){
            return;
        }
 createAccountInFirebase(email,password);


    }

void createAccountInFirebase(String email,String password){
channgeInprogress(true);

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this,
        new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                channgeInprogress(false);
if(task.isSuccessful()){
    Utility.showToast(CreateAccountActivity.this,"Succesfully created account. Check email to verify");


firebaseAuth.getCurrentUser().sendEmailVerification();
firebaseAuth.signOut();
finish();

} else {
Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());



}
            }
        }
);



}
void channgeInprogress(boolean inProgress){
        if(inProgress){
            progressbar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else {
            progressbar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
}

    boolean validateData(String email, String passsword , String confirmPassword){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if(passsword.length()<6){
            passwordEditText.setError("Password length is invalid");
            return false;
        }
        if(!passsword.equals(confirmPassword)){
            confirmPasswordEditText.setError("Password not matched");
            return false ;
        }
        return true;
    }
}