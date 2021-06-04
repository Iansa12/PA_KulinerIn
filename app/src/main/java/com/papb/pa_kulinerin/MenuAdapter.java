package com.papb.pa_kulinerin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHold>  {
    Context context;

    ArrayList<MenuModel> menu;
//    final private ListItemClickListener mOnClickListener;

    public MenuAdapter(ArrayList<MenuModel> menu /*, ListItemClickListener listener*/, Context context) {
        this.menu = menu;
//        mOnClickListener = listener;
        this.context = context;
    }

    @NonNull

    @Override
    public MenuViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_menu, parent, false);
        return new MenuViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHold holder, int position) {

        MenuModel menuModel = menu.get(position);
//        holder.image.setImageResource(restoModel.getImage());
//        assert context != null;
        holder.nama.setText(menuModel.getNama());
        Glide.with(context).load(menuModel.getImage()).into(holder.image);
        holder.harga.setText("Rp. " + menuModel.getHarga() + ",-");


//        holder.relativeLayout.setBackground(phonehelper.getgradient());
    }

    @Override
    public int getItemCount() {
        return menu.size();

    }
//
//    public interface ListItemClickListener {
//        void onrestoListClick(int clickedItemIndex);
//    }

    public class MenuViewHold extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        ImageView image;
        TextView nama, harga;

        public MenuViewHold(@NonNull View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);
            //hooks
            image = itemView.findViewById(R.id.menu_image);
            nama = itemView.findViewById(R.id.menu_title);
            harga = itemView.findViewById(R.id.tv_hargaMenu);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, nama.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }
//
//        @Override
//        public void onClick(View v) {
//            int clickedPosition = getAdapterPosition();
//            mOnClickListener.onrestoListClick(clickedPosition);
//        }
    }

}