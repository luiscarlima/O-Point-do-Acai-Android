package com.devup.opointdoacai.opointdoacai.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class LimitedEdtionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView ltd_name;
    public TextView ltd_price;
    public TextView ltd_description;
    public CheckBox ltd_select;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public LimitedEdtionViewHolder(@NonNull View itemView) {
        super(itemView);

        ltd_name = itemView.findViewById(R.id.txt_ltd_name);
        ltd_price = itemView.findViewById(R.id.txt_ltd_price);
        ltd_description = itemView.findViewById(R.id.txt_ltd_description);
        ltd_select = itemView.findViewById(R.id.checkbox_ltd);

        itemView.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

     // itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
