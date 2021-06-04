package com.papb.pa_kulinerin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    Button btLogin, btUbahFoto, btUbahUsername, btUbahNoHp;
    TextView tvNamaProfil, tvNoHpProfil, tvEmailProfil, tvAccount;
    ImageView ivFotoProfil;
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        btLogin = v.findViewById(R.id.btn_login);
        btUbahFoto = v.findViewById(R.id.btn_ubahProfil);
        btUbahUsername = v.findViewById(R.id.btn_ubahUsername);
        btUbahNoHp = v.findViewById(R.id.btn_ubahNoHp);
        tvNamaProfil = v.findViewById(R.id.tv_namaProfil);
        tvEmailProfil = v.findViewById(R.id.tv_emailProfil);
        tvNoHpProfil = v.findViewById(R.id.tv_nohpProfil);
        tvAccount = v.findViewById(R.id.tv_accountSetting);
        ivFotoProfil = v.findViewById(R.id.iv_fotoProfil);

        //Ambil SharedPreference dari mainActivity
        preferences = getActivity().getSharedPreferences("Profil", Context.MODE_PRIVATE);
        String uid = preferences.getString("uidUser","");

        //cek user apakah sudah login atau belum
        if(!uid.equals("NULL")){
            //Jika sudah login, ambil data dari sharedPreference
            String nama = preferences.getString("namaUser", "");
            String email = preferences.getString("emailUser","");
            String NoHp = preferences.getString("noHpUser", "");
            String image = preferences.getString("imageUser", "");

            if(!image.equals("")){
                Glide.with(ProfileFragment.this).load(image).into(ivFotoProfil);
            }else{
                Glide.with(ProfileFragment.this).load(R.drawable.ic_person).into(ivFotoProfil);
            }

            if(NoHp.equals("")){
                tvNoHpProfil.setText("No Hp belum diisi");
            }else{
                tvNoHpProfil.setText(NoHp);
            }

            tvEmailProfil.setVisibility(View.VISIBLE);
            tvNoHpProfil.setVisibility(View.VISIBLE);
            tvAccount.setVisibility(View.VISIBLE);
            btUbahNoHp.setVisibility(View.VISIBLE);
            btUbahUsername.setVisibility(View.VISIBLE);
            btUbahFoto.setVisibility(View.VISIBLE);

            tvEmailProfil.setText(email);
            tvNamaProfil.setText(nama);
            btLogin.setText("Logout");

        }else{
            //Jika belum ada datanya
            tvAccount.setVisibility(View.INVISIBLE);
            tvEmailProfil.setVisibility(View.INVISIBLE);
            tvNoHpProfil.setVisibility(View.INVISIBLE);
            btUbahFoto.setVisibility(View.INVISIBLE);
            btUbahNoHp.setVisibility(View.INVISIBLE);
            btUbahUsername.setVisibility(View.INVISIBLE);
            Glide.with(ProfileFragment.this).load(R.drawable.ic_person).into(ivFotoProfil);
            tvNamaProfil.setText("Silakan Login Terlebih dahulu");
            btLogin.setText("Login");
        }

        btUbahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getActivity(), LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(login);
                getActivity().finish();
            }
        });

        btUbahUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ubah = new Intent(getActivity(), UbahProfilActivity.class);
                ubah.putExtra("uid", uid);
                ubah.putExtra("nama", preferences.getString("namaUser", ""));
                ubah.putExtra("nohp", preferences.getString("noHpUser", ""));
                ubah.putExtra("email", preferences.getString("emailUser",""));
                ubah.putExtra("image", preferences.getString("imageUser", ""));
                startActivity(ubah);
            }
        });

        btUbahNoHp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ubah = new Intent(getActivity(), UbahProfilActivity.class);
                ubah.putExtra("uid", uid);
                ubah.putExtra("nama", preferences.getString("namaUser", ""));
                ubah.putExtra("nohp", preferences.getString("noHpUser", ""));
                ubah.putExtra("email", preferences.getString("emailUser",""));
                ubah.putExtra("image", preferences.getString("imageUser", ""));
                startActivity(ubah);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            Glide.with(ProfileFragment.this).load(bitmap).into(ivFotoProfil);
        }
    }
}