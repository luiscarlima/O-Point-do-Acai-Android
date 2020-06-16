package com.devup.opointdoacai.opointdoacai.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class TopChoicesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView top_name;
    public TextView top_description;
    public TextView top_number;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public TopChoicesViewHolder(@NonNull View itemView) {
        super(itemView);

        top_name = itemView.findViewById(R.id.txt_top_name_choice);
        top_description = itemView.findViewById(R.id.txt_top_complementos_choice);
        top_number = itemView.findViewById(R.id.txt_top_number_choice);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
