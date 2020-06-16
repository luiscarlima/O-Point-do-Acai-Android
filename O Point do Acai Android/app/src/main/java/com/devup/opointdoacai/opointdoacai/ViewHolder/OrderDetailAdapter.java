package com.devup.opointdoacai.opointdoacai.ViewHolder;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Model.Order;
import com.devup.opointdoacai.opointdoacai.R;

import java.util.List;

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView complementos, quantidade, preco;

    public MyViewHolder(View itemView) {
        super(itemView);

        quantidade = itemView.findViewById(R.id.product_name);
        complementos = itemView.findViewById(R.id.product_comp);
        preco = itemView.findViewById(R.id.product_price);

    }

}

public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder>{

    List<Order> myOrders;

    public OrderDetailAdapter(List<Order> myOrders) {
        this.myOrders = myOrders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Order order = myOrders.get(position);
        holder.quantidade.setText(String.format("Produto: %s", order.getQuantidade()));
        holder.complementos.setText(String.format("Complementos: %s", order.getComplementos()));
        holder.preco.setText(String.format("Preço: %s", order.getPreco() ));


    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}