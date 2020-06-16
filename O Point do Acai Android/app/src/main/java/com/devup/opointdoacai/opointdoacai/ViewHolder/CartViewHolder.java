package com.devup.opointdoacai.opointdoacai.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

    public TextView txt_cart_nome, txt_cart_complementos, txt_cart_preco;

    public RelativeLayout view_background;
    public RelativeLayout view_foreground;

    private ItemClickListener itemClickListener;

    public void setTxt_cart_nome(TextView txt_cart_nome) {
        this.txt_cart_nome = txt_cart_nome;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_nome = itemView.findViewById(R.id.cart_list_nome);
        txt_cart_complementos = itemView.findViewById(R.id.cart_list_complementos);
        txt_cart_preco = itemView.findViewById(R.id.cart_list_preco);
        view_background = itemView.findViewById(R.id.view_background);
        view_foreground = itemView.findViewById(R.id.view_foreground);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle("Selecione um Ação");
        contextMenu.add(0, 0, getAdapterPosition(), Common.DELETE);

    }
}
