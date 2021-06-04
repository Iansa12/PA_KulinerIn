package com.papb.pa_kulinerin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UbahProfilActivity extends AppCompatActivity {
    EditText gantiUsername, gantiNoHp;
    String uid, username, nohp, image, email;
    Button btGanti;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefDB = database.getReference();

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil);
        gantiUsername = findViewById(R.id.et_gantiUsername);
        gantiNoHp = findViewById(R.id.et_gantiNoHp);
        btGanti = findViewById(R.id.btn_ganti);

        sharedPreferences = getSharedPreferences("Profil", MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();

        uid      = extras.getString("uid");
        username = extras.getString("nama");
        nohp     = extras.getString("nohp");
        email    = extras.getString("email");
        image    = extras.getString("image");
        gantiUsername.setText(username);
        gantiNoHp.setText(nohp);

        btGanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitUser(new RegisterUserDB(gantiUsername.getText().toString(),gantiNoHp.getText().toString(), email, image));
                Intent intent = new Intent(UbahProfilActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void SubmitUser(RegisterUserDB registerUserDB) {

        //Set SharedPreference baru
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        editor.putString("uidUser", uid);
        editor.putString("emailUser", email);
        editor.putString("namaUser", username);
        editor.putString("noHpUser", nohp);
        editor.putString("imageUser", image);
        editor.apply();

        myRefDB.child("Users")
                .child(uid)
//                .push()
                .setValue(registerUserDB)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UbahProfilActivity.this, "User telah diubah", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}