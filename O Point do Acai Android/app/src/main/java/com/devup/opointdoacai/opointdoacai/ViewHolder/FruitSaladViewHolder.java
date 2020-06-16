package com.devup.opointdoacai.opointdoacai.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class FruitSaladViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView fruit_cobertura;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FruitSaladViewHolder(@NonNull View itemView) {
        super(itemView);

        fruit_cobertura = itemView.findViewById(R.id.txt_cobertura);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
