package com.devup.opointdoacai.opointdoacai.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class CompsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView comp_name;
    public TextView comp_price;
    public CheckBox comp_select;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CompsViewHolder(@NonNull View itemView) {
        super(itemView);

        comp_name = itemView.findViewById(R.id.txt_comp_name);
        comp_price = itemView.findViewById(R.id.txt_comp_price);
        comp_select = itemView.findViewById(R.id.checkbox_comp);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

//        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
