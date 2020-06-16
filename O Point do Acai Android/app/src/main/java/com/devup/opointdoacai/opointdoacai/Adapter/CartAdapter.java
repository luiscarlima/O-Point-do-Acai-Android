package com.devup.opointdoacai.opointdoacai.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devup.opointdoacai.opointdoacai.ViewHolder.CartViewHolder;
import com.devup.opointdoacai.opointdoacai.Model.Order;
import com.devup.opointdoacai.opointdoacai.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }


    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        Locale locale = new Locale("PT","BR");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        holder.txt_cart_nome.setText(listData.get(position).getQuantidade());
        holder.txt_cart_complementos.setText(listData.get(position).getComplementos());
        holder.txt_cart_preco.setText(listData.get(position).getPreco());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public Order getItem(int position){

        return listData.get(position);

    }

    public void removeItem(int position){

        listData.remove(position);
        notifyItemRemoved(position);

    }

    public void restoreItem(Order item, int position){

        listData.add(position, item);
        notifyItemInserted(position);

    }


}
