package com.devup.opointdoacai.opointdoacai;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.Model.Request;
import com.devup.opointdoacai.opointdoacai.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import de.mateware.snacky.Snacky;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;
    FirebaseAuth mAuth;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    private android.support.v7.widget.Toolbar toolbar;

    private ProgressBar mProgressBar;

    private TextView txt_info;

    @Override
    protected void onRestart() {
        super.onRestart();

        String uid = mAuth.getCurrentUser().getUid();

        if (Common.isConnectedToInternet(getBaseContext())) {
            loadOrders(uid);
        }else{
            Snacky.builder()
                    .setActivity(OrderStatus.this)
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
            txt_info.setText("Sem conexão com a internet");
            return;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        txt_info = findViewById(R.id.txt_info_orders);

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.toolbar_id_orders);
        toolbar.setTitle("Pedidos");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mProgressBar = findViewById(R.id.progress_bar_orders);

        mProgressBar.setVisibility(View.VISIBLE);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Pedidos");
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String uid = mAuth.getCurrentUser().getUid();

        if (Common.isConnectedToInternet(getBaseContext())) {
            onReloadList("");
            loadOrders(uid);
        }else{
            Snacky.builder()
                    .setActivity(OrderStatus.this)
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
            txt_info.setText("Sem conexão com a internet");
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        }

    }

    private void loadOrders(final String uid) {

        Query getOrderByUser = requests.orderByChild("id")
                .equalTo(uid);

        FirebaseRecyclerOptions<Request> orderOptions = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(getOrderByUser, Request.class)
                .build();

       adapter  = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(orderOptions) {
           @Override
           protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, final int position, @NonNull final Request model) {
               viewHolder.txtOrderId.setText("Numero do Pedido " + adapter.getRef(position).getKey());
               viewHolder.txtOrderStatus.setText("Status: " + Common.convertCodeToStatus(model.getStatus()));
               viewHolder.txtOrderAddress.setText("Endereço: " + model.getEndereco());
               viewHolder.txtOrderPhone.setText(model.getShipping());
               viewHolder.setItemClickListener(new ItemClickListener() {
                   @Override
                   public void onClick(View view, int position, boolean isLongClick) {

                       if (!isLongClick){

                           Intent orderDetailIntent = new Intent(OrderStatus.this, OrderDetail.class);
                           Common.currentRequest = model;
                           orderDetailIntent.putExtra("OrderId", adapter.getRef(position).getKey());
                           startActivity(orderDetailIntent);

                       }else{

                       }
                   }
               });

               onReloadList(model.getNome());

               viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       if (adapter.getItem(position).getStatus().equals("0") || adapter.getItem(position).getStatus().equals("1")){

                           deleteOrder(adapter.getRef(position).getKey());

                           loadOrders(uid);

                       }else{

                           Snacky.builder()
                                   .setActivity(OrderStatus.this)
                                   .setText("Não é possível cancelar um pedido que já saiu para entrega.")
                                   .setDuration(Snacky.LENGTH_LONG)
                                   .error()
                                   .show();
                       }

                   }
               });

           }

           @NonNull
           @Override
           public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View itemView = LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.order_layout, parent, false);
               return new OrderViewHolder(itemView);
           }
       };

       adapter.startListening();
       recyclerView.setAdapter(adapter);

    }

    private void deleteOrder(final String key) {

        requests.child(key)
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Snacky.builder()
                        .setActivity(OrderStatus.this)
                        .setText("O Pedido " + key + " foi cancelado com sucesso!")
                        .setDuration(Snacky.LENGTH_LONG)
                        .error()
                        .show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snacky.builder()
                        .setActivity(OrderStatus.this)
                        .setText("Erro: " + e.getMessage())
                        .setDuration(Snacky.LENGTH_LONG)
                        .error()
                        .show();

            }
        });

    }

    private void onReloadList(String flag) {

        if (flag.equals("")){

            txt_info.setText("Sem pedidos até o momento...");

            callObjAnimator();

        }else{

            txt_info.setText("");

            callObjAnimator();

        }

    }

    private void callObjAnimator() {

        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100);
        animation.setDuration(3000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { }

            @Override
            public void onAnimationEnd(Animator animator) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) { }

            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
        animation.start();

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (Common.isConnectedToInternet(getBaseContext())) {
            adapter.stopListening();
        }else{
            return;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(OrderStatus.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
