package com.devup.opointdoacai.opointdoacai.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class PotsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView pots_name;
    public TextView pots_description;
    public TextView pots_price;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public PotsViewHolder(@NonNull View itemView) {
        super(itemView);

        pots_name = itemView.findViewById(R.id.txt_pots_name);
        pots_description = itemView.findViewById(R.id.txt_pots_description);
        pots_price = itemView.findViewById(R.id.txt_pots_price);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
