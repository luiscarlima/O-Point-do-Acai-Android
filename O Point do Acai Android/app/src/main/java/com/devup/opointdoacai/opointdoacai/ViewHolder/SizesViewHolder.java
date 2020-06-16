package com.devup.opointdoacai.opointdoacai.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class SizesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView size_name;
    public TextView size_price;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SizesViewHolder(@NonNull View itemView) {
        super(itemView);

        size_name = itemView.findViewById(R.id.txt_size_name);
        size_price = itemView.findViewById(R.id.txt_size_price);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
