package com.devup.opointdoacai.opointdoacai.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class VitaminsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView vita_name;
    public TextView vita_description;
    public TextView vita_price;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public VitaminsViewHolder(@NonNull View itemView) {
        super(itemView);

        vita_name = itemView.findViewById(R.id.txt_vita_name);
        vita_description = itemView.findViewById(R.id.txt_vita_description);
        vita_price = itemView.findViewById(R.id.txt_vita_price);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
