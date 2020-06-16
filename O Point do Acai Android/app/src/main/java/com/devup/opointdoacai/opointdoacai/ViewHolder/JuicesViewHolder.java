package com.devup.opointdoacai.opointdoacai.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class JuicesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView juice_name;
    public TextView juice_description;
    public TextView juice_price;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public JuicesViewHolder(@NonNull View itemView) {
        super(itemView);

        juice_name = itemView.findViewById(R.id.txt_juice_name);
        juice_description = itemView.findViewById(R.id.txt_juice_description);
        juice_price = itemView.findViewById(R.id.txt_juice_price);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
