package com.papb.pa_kulinerin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText etEmail, etPassword;
    public String Uid = "";
    ProgressBar pbLogin;
    TextView tvSignUp, tvLogin;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        tvLogin = findViewById(R.id.tv_login);
        tvSignUp = findViewById(R.id.tv_Signup);

        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        etEmail.setText(sharedPreferences.getString("email",""));
        etPassword.setText(sharedPreferences.getString("password",""));

        pbLogin = findViewById(R.id.progressBar_login);
        pbLogin.setVisibility(View.INVISIBLE);
        tvLogin.setEnabled(true);
        tvSignUp.setEnabled(true);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
                finish();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if(email.equals("")){
                    etEmail.setError("Masukkan email anda");
                    etEmail.requestFocus();
                    Toast.makeText(LoginActivity.this, "Email kosong", Toast.LENGTH_SHORT).show();
                }else if(password.equals("")){
                    etPassword.setError("Masukkan password anda");
                    etEmail.requestFocus();
                    Toast.makeText(LoginActivity.this, "Password kosong", Toast.LENGTH_SHORT).show();
                }else{
                    tvLogin.setEnabled(false);
                    tvSignUp.setEnabled(false);
                    pbLogin.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                editor.clear();
                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.apply();
                                Log.d("Login", "Suksess");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                                Uid = user.getUid();
                                Intent home = new Intent(LoginActivity.this, MainActivity.class);
                                home.putExtra("Uid", Uid);
                                startActivity(home);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                                pbLogin.setVisibility(View.INVISIBLE);
                                tvLogin.setEnabled(true);
                                tvSignUp.setEnabled(true);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    @Override
    public void onBackPressed() {
        Intent goHome = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(goHome);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }
}