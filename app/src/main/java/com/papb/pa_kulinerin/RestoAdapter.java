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

public class RestoAdapter extends RecyclerView.Adapter<RestoAdapter.RestoViewHold>  {
    Context context;

    ArrayList<RestoModel> resto;
//    final private ListItemClickListener mOnClickListener;

    public RestoAdapter(ArrayList<RestoModel> resto /*, ListItemClickListener listener*/, Context context) {
        this.resto = resto;
//        mOnClickListener = listener;
        this.context = context;
    }

    @NonNull

    @Override
    public RestoViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle, parent, false);
        return new RestoViewHold(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RestoViewHold holder, int position) {

        RestoModel restoModel = resto.get(position);
//        holder.image.setImageResource(restoModel.getImage());
//        assert context != null;
        holder.namaResto.setText(restoModel.getIdresto());
        Glide.with(context).load(restoModel.getImage()).into(holder.image);


//        holder.relativeLayout.setBackground(phonehelper.getgradient());
    }

    @Override
    public int getItemCount() {
        return resto.size();

    }
//
//    public interface ListItemClickListener {
//        void onrestoListClick(int clickedItemIndex);
//    }

    public class RestoViewHold extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        ImageView image;
        TextView namaResto;
        String idresto;

        public RestoViewHold(@NonNull View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);
            //hooks
            image = itemView.findViewById(R.id.phone_image);
            namaResto = itemView.findViewById(R.id.tv_namaResto);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent  = new Intent(context, RestoranActivity.class);
                    mIntent.putExtra("idresto", namaResto.getText());
                    Toast.makeText(context, namaResto.getText(), Toast.LENGTH_SHORT).show();
                    context.startActivity(mIntent);
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