package com.devup.opointdoacai.opointdoacai.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class TFSViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tfs_name;
    public TextView tfs_price;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public TFSViewHolder(@NonNull View itemView) {
        super(itemView);

        tfs_name = itemView.findViewById(R.id.txt_tfs_name);
        tfs_price = itemView.findViewById(R.id.txt_tfs_price);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
