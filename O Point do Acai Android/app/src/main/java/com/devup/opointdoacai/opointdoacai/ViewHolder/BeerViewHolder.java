package com.devup.opointdoacai.opointdoacai.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class BeerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView beer_name;
    public TextView beer_description;
    public TextView beer_price;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public BeerViewHolder(@NonNull View itemView) {
        super(itemView);

        beer_name = itemView.findViewById(R.id.txt_name_beer);
        beer_description = itemView.findViewById(R.id.txt_description_beer);
        beer_price = itemView.findViewById(R.id.txt_beer_price);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
