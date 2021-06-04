package com.papb.pa_kulinerin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Fragment 1
public class HomeFragment extends Fragment {

    //Inisialisasi RecyclerView
    RecyclerView restoRecycler, restoRecycler2, restoRecycler3;
    RecyclerView.Adapter adapter,adapter2,adapter3;
    ArrayList<RestoModel> resto, resto2, resto3;

    TextView tv_kategoriResto, tv_kategoriResto2, tv_kategoriResto3;
    String a1, a2, a3;
    ImageView btnProfilUser;
    SearchView svSearch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        btnProfilUser = v.findViewById(R.id.btn_profilHome);
        restoRecycler = v.findViewById(R.id.my_recycler);
        restoRecycler2 = v.findViewById(R.id.my_recycler2);
        restoRecycler3 = v.findViewById(R.id.my_recycler3);
        tv_kategoriResto = v.findViewById(R.id.tv_kategori);
        tv_kategoriResto2 = v.findViewById(R.id.tv_kategori2);
        tv_kategoriResto3 = v.findViewById(R.id.tv_kategori3);

        //Ambil data dari sharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("Profil", Context.MODE_PRIVATE);
        String uid = preferences.getString("uidUser","");

        if(!uid.equals("NULL")){
            String image = preferences.getString("imageUser", "");

            if(!image.equals("")){
                Glide.with(HomeFragment.this).load(image).into(btnProfilUser);
            }
        }

        svSearch = v.findViewById(R.id.sv_search);
        svSearch.setVisibility(View.INVISIBLE);

        resto = new ArrayList<>();
        resto2 = new ArrayList<>();
        resto3 = new ArrayList<>();

        a1 = "Healty";
        a2 = "Spicy";
        a3 = "Junk";

        tv_kategoriResto.setText(a1);
        tv_kategoriResto2.setText(a2);
        tv_kategoriResto3.setText(a3);

        makeKategori(a1);
        makeKategori(a2);
        makeKategori(a3);

        final Fragment fragment2 = new ProfileFragment();

        btnProfilUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment, fragment2).commit();
            }
        });

        return v;
    }



    private void makeKategori(String param){

        //Ambil data dari firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRefDB = database.getReference();
        myRefDB.child("Kategori").child(param).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String idResto = snapshot1.child("idresto").getValue(String.class);
                    String imageResto = snapshot1.child("image").getValue(String.class);

                    if(param.equalsIgnoreCase("Healty")){
                        resto.add(new RestoModel(idResto, imageResto));
                    } else if(param.equalsIgnoreCase("Spicy")){
                        resto2.add(new RestoModel(idResto, imageResto));
                    }else if(param.equalsIgnoreCase("Junk")){
                        resto3.add(new RestoModel(idResto, imageResto));
                    }

                    Log.d("Read Database", "idResto is: " + idResto);
                    Log.d("Read Database", "imgResto is: " + imageResto);
                } restoRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Database Error", "Failed to read value.", error.toException());
            }

        });

    }

    private void restoRecycler() {

        //Isi data ke recyclerView
        restoRecycler.setHasFixedSize(true);
        restoRecycler2.setHasFixedSize(true);
        restoRecycler3.setHasFixedSize(true);
        restoRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        restoRecycler2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        restoRecycler3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new RestoAdapter(resto,this.getActivity());
        adapter2 = new RestoAdapter(resto2, this.getActivity());
        adapter3 = new RestoAdapter(resto3, this.getActivity());
        restoRecycler.setAdapter(adapter);
        restoRecycler2.setAdapter(adapter2);
        restoRecycler3.setAdapter(adapter3);

    }
}