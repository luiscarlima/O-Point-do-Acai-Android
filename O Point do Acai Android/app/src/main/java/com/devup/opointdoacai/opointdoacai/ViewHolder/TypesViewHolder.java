package com.devup.opointdoacai.opointdoacai.ViewHolder;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class TypesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView type_name;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public TypesViewHolder(@NonNull View itemView) {
        super(itemView);

        type_name = itemView.findViewById(R.id.txt_type_name);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
