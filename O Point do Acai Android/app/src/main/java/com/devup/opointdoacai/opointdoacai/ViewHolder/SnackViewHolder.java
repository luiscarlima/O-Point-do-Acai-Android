package com.devup.opointdoacai.opointdoacai.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class SnackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView snack_name;
    public TextView snack_description;
    public TextView snack_price;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SnackViewHolder(@NonNull View itemView) {
        super(itemView);

        snack_name = itemView.findViewById(R.id.txt_snack_name);
        snack_description = itemView.findViewById(R.id.txt_snack_description);
        snack_price = itemView.findViewById(R.id.txt_snack_price);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
