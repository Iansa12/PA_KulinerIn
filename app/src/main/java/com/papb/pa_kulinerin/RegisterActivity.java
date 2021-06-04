package com.papb.pa_kulinerin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    TextView tvCreateUser, tvSignIn;
    EditText etEmail, etPassword, etUsername;

    private FirebaseAuth mAuth;
    private String Uid = "";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefDB = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        tvCreateUser = findViewById(R.id.tv_createUser);
        tvSignIn = findViewById(R.id.txtSignin);
        etUsername = findViewById(R.id.et_usernameRegister);
        etEmail = findViewById(R.id.et_emailRegister);
        etPassword = findViewById(R.id.et_passwordRegister);
        tvSignIn.setEnabled(true);
        tvCreateUser.setEnabled(true);

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(goLogin);
                finish();
            }
        });

        tvCreateUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if(username.equals("")){
                    etUsername.setError("Masukkan username anda");
                    etUsername.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else if(email.equals("")){
                    etEmail.setError("Masukkan email anda");
                    etEmail.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else if(password.equals("")){
                    etPassword.setError("Masukkan password anda");
                    etPassword.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else{
                    tvCreateUser.setEnabled(false);
                    tvSignIn.setEnabled(false);
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d("Buat Akun : ","CreateSuccess");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(RegisterActivity.this, "Berhasil dibuat", Toast.LENGTH_SHORT).show();
                                assert user != null;
                                Uid = user.getUid();
                                SubmitUser(new RegisterUserDB(username,"", email, "" ));
                                Intent login = new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(login);
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                tvSignIn.setEnabled(true);
                                tvCreateUser.setEnabled(true);
                            }
                        }
                    });
                }
            }
        });
    }

    private void SubmitUser(RegisterUserDB registerUserDB){
        myRefDB.child("Users")
                .child(Uid)
//                .push()
                .setValue(registerUserDB)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        etUsername.setText("");
                        etEmail.setText("");
                        etPassword.setText("");

                        Toast.makeText(RegisterActivity.this, "User telah ditambahkan", Toast.LENGTH_SHORT).show();
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
        Intent goLogin = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(goLogin);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }
}