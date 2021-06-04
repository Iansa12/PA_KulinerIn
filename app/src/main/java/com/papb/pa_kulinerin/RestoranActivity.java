package com.papb.pa_kulinerin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestoranActivity extends AppCompatActivity {

    RecyclerView makananRecycler, minumanRecycler;
    RecyclerView.Adapter adapter,adapter2;
    ArrayList<MenuModel> listMakanan, listMinuman;
    String a1, a2, a3, telpResto;
    TextView namaResto, alamatResto, notelpResto, nickResto;
    ImageView fotoResto, logoResto;
    FirebaseDatabase database;
    FloatingActionButton fabTelpResto;

    DatabaseReference myRefDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restoran);
        database = FirebaseDatabase.getInstance();
        namaResto = findViewById(R.id.tv_namaRestoran2);
        alamatResto = findViewById(R.id.tv_alamat2);
        notelpResto = findViewById(R.id.tv_nohp2);
        nickResto = findViewById(R.id.tv_nick);
        fotoResto = findViewById(R.id.iv_fotoResto);
        logoResto = findViewById(R.id.iv_logoresto);
        fabTelpResto = findViewById(R.id.fab_telpResto);
        fabTelpResto.setEnabled(false);

        a1 = "menumakanan";
        a2 = "menuminuman";
        a3 = getIntent().getExtras().getString("idresto");
        nickResto.setText(a3);

        myRefDB = database.getReference().child("Restoran").child(a3);

        //ambil data dari firebase
        myRefDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nama = snapshot.child("namaresto").getValue(String.class);
                String image = snapshot.child("image").getValue(String.class);
                String alamat = snapshot.child("alamat").getValue(String.class);
                String notelp = snapshot.child("notelp").getValue(String.class);
                String logoresto  = snapshot.child("logo").getValue(String.class);

                namaResto.setText(nama);
                alamatResto.setText(alamat);
                notelpResto.setText(notelp);
                telpResto = notelp;
                fabTelpResto.setEnabled(true);

                if(!image.equals("")){
                    Glide.with(RestoranActivity.this).load(image).into(fotoResto);
                }
                if(!logoresto.equals("")){
                    Glide.with(RestoranActivity.this).load(logoresto).into(logoResto);
                }
                    Log.d("Read Database", "namaResto is: " + nama);
                    Log.d("Read Database", "imgResto is: " + image);
                    Log.d("Read Database", "alamatResto is: " + alamat);
                    Log.d("Read Database", "notelpResto is: " + notelp);
                    Log.d("Read Database", "logoResto is: " + logoresto);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Database Error", "Failed to read value.", error.toException());
            }

        });

        makananRecycler = findViewById(R.id.makananrecycler);
        minumanRecycler = findViewById(R.id.minumanrecycler);
        listMakanan = new ArrayList<>();
        listMinuman = new ArrayList<>();
        makeKategori(a1);
        makeKategori(a2);

        //Intent ke telepon
        fabTelpResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(telpResto != null){
                    Intent tlp = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+telpResto));
                    startActivity(tlp);
                }
            }
        });

    }

    //method ambil data menu dari firebase
    private void makeKategori(String param){

        myRefDB.child(param).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String image = snapshot1.child("image").getValue(String.class);
                    String nama = snapshot1.child("nama").getValue(String.class);
                    int harga = snapshot1.child("harga").getValue(int.class);
//                    RestoModel data = snapshot1.getValue(RestoModel.class);
                    if(param.equalsIgnoreCase("menumakanan")){
                        listMakanan.add(new MenuModel(nama, image, harga));
                    } else if(param.equalsIgnoreCase("menuminuman")){
                        listMinuman.add(new MenuModel(nama, image, harga));
                    }
                    Log.d("Read Database", "namaResto is: " + nama);
                    Log.d("Read Database", "imgResto is: " + image);
                } restoRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Database Error", "Failed to read value.", error.toException());
            }

        });

    }

    //Set data ke RecyclerView
    private void restoRecycler() {
        makananRecycler.setHasFixedSize(true);
        minumanRecycler.setHasFixedSize(true);
        makananRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        minumanRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MenuAdapter(listMakanan,this);
        adapter2 = new MenuAdapter(listMinuman, this);
        makananRecycler.setAdapter(adapter);
        minumanRecycler.setAdapter(adapter2);

    }
}
