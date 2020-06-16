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
import com.devup.opointdoacai.opointdoacai.Model.Salads;
import com.devup.opointdoacai.opointdoacai.ViewHolder.FruitSaladViewHolder;
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

public class FruitsSalad extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference fruitsSaladaList;

    FirebaseRecyclerAdapter<Salads, FruitSaladViewHolder> adapter;

    private android.support.v7.widget.Toolbar toolbar;

    private float valorIndividual = 0.00f;
    private String valor = "";
    private String quantidade = "";
    private String complementos = "";

    private float tfs_price = 0.00f;
    private String tfs_name = "";

    private ProgressBar mProgressBar;

    @Override
    protected void onRestart() {
        super.onRestart();

        if (Common.isConnectedToInternet(getBaseContext())) {
            loadListFruitSalad(tfs_name, tfs_price);
        }else{
            Snacky.builder()
                    .setActivity(FruitsSalad.this)
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
            return;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits_salad);

        //Setando Orientação de Retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.fruit_salad_toolbar);
        toolbar.setTitle("Selecione uma Cobertura");
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

        mProgressBar = findViewById(R.id.progress_bar_fruits_salads);

        mProgressBar.setVisibility(View.VISIBLE);

        //Firebase Instance
        database = FirebaseDatabase.getInstance();
        fruitsSaladaList = database.getReference("Salads");

        recyclerView = findViewById(R.id.recycler_fruit_salad);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null){

            tfs_price = Float.parseFloat(getIntent().getStringExtra("tfs_price"));
            tfs_name = getIntent().getStringExtra("tfs_name");

            if (Common.isConnectedToInternet(getBaseContext())) {
                loadListFruitSalad(tfs_name, tfs_price);
            }else{
                Snacky.builder()
                        .setActivity(FruitsSalad.this)
                        .setText("Sem conexão com a Internet")
                        .setDuration(Snacky.LENGTH_LONG)
                        .error()
                        .show();
                mProgressBar.setVisibility(View.INVISIBLE);
                return;
            }

        }

    }

    private void loadListFruitSalad(final String tfs_name, final float tfs_price) {

        Query search = fruitsSaladaList;
        FirebaseRecyclerOptions<Salads> fruitSaladOptions = new FirebaseRecyclerOptions.Builder<Salads>()
                .setQuery(search, Salads.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Salads, FruitSaladViewHolder>(fruitSaladOptions) {
            @Override
            protected void onBindViewHolder(@NonNull FruitSaladViewHolder viewHolder, int position, @NonNull Salads model) {

                viewHolder.fruit_cobertura.setText(model.getDescription());

                onReloadList(model.getDescription());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        final String select_key = adapter.getRef(position).getKey();

                        fruitsSaladaList.child(select_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Salads salads = dataSnapshot.getValue(Salads.class);

                                valorIndividual = tfs_price;
                                quantidade = salads.getName() + " " + tfs_name;
                                complementos = salads.getDescription();

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

                                Intent intent = new Intent(FruitsSalad.this, Cart.class);
                                startActivity(intent);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Snacky.builder()
                                        .setActivity(FruitsSalad.this)
                                        .setText("Error:"+ databaseError.getMessage())
                                        .setDuration(Snacky.LENGTH_LONG)
                                        .error()
                                        .show();
                                return;
                            }
                        });
                    }
                });

            }

            @NonNull
            @Override
            public FruitSaladViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fruit_salad_item, parent, false);
                return new FruitSaladViewHolder(itemView);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void onReloadList(String flag) {

        if (flag.equals(null)){

        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
        }

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

        valorIndividual = 00.00f;
        quantidade = "";
        complementos = "";
        finish();

    }
}
