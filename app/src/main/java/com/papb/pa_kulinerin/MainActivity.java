package com.papb.pa_kulinerin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String Uid = "NULL";
    BottomNavigationView nav;
    BottomNavigationView.OnNavigationItemSelectedListener navigation;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //SharedPreferences
        sharedPreferences = getSharedPreferences("Profil", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uidUser", Uid);

        if(user==null) {
            editor.apply();

            //Notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,"notif")
                    .setContentTitle("New Notification")
                    .setContentText("Silakan login terlebih dahulu")
                    .setSmallIcon(R.drawable.ic_person)
                    .setAutoCancel(true);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
            managerCompat.notify(1, builder.build());

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel("notif", "notif", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
                manager.notify(1, builder.build());
            }else{
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
            }

        }else{
            Uid = user.getUid();
            if(!sharedPreferences.getString("uidUser", "").equals(Uid)){
                editor.clear();
                editor.apply();
                editor.putString("uidUser", Uid);

                DatabaseReference myRefDB = database.getReference();

                myRefDB.child("Users").child(Uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String nama = dataSnapshot.child("username").getValue(String.class);
                        String NoHp = dataSnapshot.child("nohp").getValue(String.class);
                        String image = dataSnapshot.child("image").getValue(String.class);
                        assert NoHp != null;
                        if(NoHp.equals("")){
                            NoHp += "Belum disetting";
                        }
                        //set sharedPreferences
                        editor.putString("emailUser", email);
                        editor.putString("namaUser", nama);
                        editor.putString("noHpUser", NoHp);
                        editor.putString("imageUser", image);
                        editor.apply();

                        Log.d("Read Database", "email is: " + email);
                        Log.d("Read Database", "nama is: " + nama);
                        Log.d("Read Database", "nohp is: " + NoHp);
                        Log.d("Read Database", "image is: " + image);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("Database Error", "Failed to read value.", error.toException());
                    }
                });
            }
        }

        nav = findViewById(R.id.bottomNavigationView);

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
        }

        navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                //Fragment
                Fragment f;

                switch (item.getItemId()){
                    case R.id.homeFragment:
                        f = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, f).commit();
                        return true;
                    case R.id.profileFragment:
                        f = new ProfileFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, f).commit();
                        return true;
                }
                return false;
            }
        };
        nav.setOnNavigationItemSelectedListener(navigation);
        nav.setSelectedItemId(R.id.homeFragment);
    }
    @Override
    public void onBackPressed() {
        if(nav.getSelectedItemId() == R.id.profileFragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
            nav.setSelectedItemId(R.id.homeFragment);
        }else{
            finish();
        }
    }

}