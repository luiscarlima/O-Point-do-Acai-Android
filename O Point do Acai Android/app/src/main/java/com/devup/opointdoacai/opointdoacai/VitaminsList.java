package com.devup.opointdoacai.opointdoacai;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.devup.opointdoacai.opointdoacai.Database.Database;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.Model.Order;
import com.devup.opointdoacai.opointdoacai.Model.Vitamins;
import com.devup.opointdoacai.opointdoacai.ViewHolder.VitaminsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.NumberFormat;
import java.util.Locale;
import de.mateware.snacky.Snacky;

public class VitaminsList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference vitaminsList;

    FirebaseRecyclerAdapter<Vitamins, VitaminsViewHolder> adapter;

    private android.support.v7.widget.Toolbar toolbar;

    private float valorIndividual = 0.00f;
    private String valor = "";
    private String quantidade = "";
    private String complementos = "";

    private ProgressBar mProgressBar;

    @Override
    protected void onRestart() {
        super.onRestart();

        if (Common.isConnectedToInternet(getBaseContext())) {
            loadListVitamins();
        }else{
            Snacky.builder()
                    .setActivity(VitaminsList.this)
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitamins_list);

        //Setando Orientação de Retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.vitamins_toolbar);
        toolbar.setTitle("Vitaminas");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                valorIndividual = 00.00f;
                quantidade = "";
                complementos = "";
                finish();

            }
        });

        mProgressBar = findViewById(R.id.progress_bar_vitamins);

        mProgressBar.setVisibility(View.VISIBLE);

        //Firebase Instance
        database = FirebaseDatabase.getInstance();
        vitaminsList = database.getReference("Vitamins");

        recyclerView = findViewById(R.id.recycler_vitamins);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(getBaseContext())) {
            loadListVitamins();
        }else{
            Snacky.builder()
                    .setActivity(VitaminsList.this)
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        }

    }

    private void loadListVitamins() {

        Query search = vitaminsList;
        FirebaseRecyclerOptions<Vitamins> vitaminsOptions = new FirebaseRecyclerOptions.Builder<Vitamins>()
                .setQuery(search, Vitamins.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Vitamins, VitaminsViewHolder>(vitaminsOptions) {
            @Override
            protected void onBindViewHolder(@NonNull VitaminsViewHolder holder, int position, @NonNull Vitamins model) {

                holder.vita_name.setText(model.getName());
                holder.vita_description.setText(model.getDescription());
                holder.vita_price.setText(model.getPrice());

                onReloadList(model.getName());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        final String select_key = adapter.getRef(position).getKey();

                        vitaminsList.child(select_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Vitamins vitamins = dataSnapshot.getValue(Vitamins.class);

                                valorIndividual = Float.parseFloat(vitamins.getPrice());
                                quantidade = vitamins.getName();
                                complementos = vitamins.getDescription();

                                Locale locale = new Locale("PT","BR");
                                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                                valor = fmt.format(valorIndividual);

                                String key_id = String.valueOf(System.currentTimeMillis());

                                new Database(getBaseContext()).addToCart(new Order(
                                        key_id,
                                        quantidade,
                                        complementos,
                                        valor
                                ));

                                Intent intent = new Intent(VitaminsList.this, Cart.class);
                                startActivity(intent);
                                finish();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                Snacky.builder()
                                        .setActivity(VitaminsList.this)
                                        .setText("Error:"+ databaseError.getMessage())
                                        .setDuration(Snacky.LENGTH_LONG)
                                        .error()
                                        .show();

                            }
                        });

                    }
                });

            }

            @NonNull
            @Override
            public VitaminsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.vitamins_item, parent, false);
                return new VitaminsViewHolder(itemView);

            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

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

    private void onReloadList(String flag) {

        if (flag.equals(null)){

        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onBackPressed() {

        valorIndividual = 00.00f;
        quantidade = "";
        complementos = "";
        finish();

    }
}
