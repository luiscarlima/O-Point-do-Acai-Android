package com.devup.opointdoacai.opointdoacai;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.devup.opointdoacai.opointdoacai.Database.Database;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.Model.Beers;
import com.devup.opointdoacai.opointdoacai.Model.Order;
import com.devup.opointdoacai.opointdoacai.ViewHolder.BeerViewHolder;
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

public class BeersList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference beersList;

    FirebaseRecyclerAdapter<Beers, BeerViewHolder> adapter;

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
            loadListBeers();
        }else{
            Snacky.builder()
                    .setActivity(BeersList.this)
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
        setContentView(R.layout.activity_beers_list);

        //Setando Orientação de Retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.beers_toolbar);
        toolbar.setTitle("Bebidas");
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

        mProgressBar = findViewById(R.id.progress_bar_beers);

        mProgressBar.setVisibility(View.VISIBLE);

        //Firebase Instance
        database = FirebaseDatabase.getInstance();
        beersList = database.getReference("Beers");

        recyclerView = findViewById(R.id.recycler_beers);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(getBaseContext())) {
            loadListBeers();
        }else{
            Snacky.builder()
                    .setActivity(BeersList.this)
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        }

    }

    private void loadListBeers() {

        Query search = beersList;

        FirebaseRecyclerOptions<Beers> beersOptions = new FirebaseRecyclerOptions.Builder<Beers>()
                .setQuery(search, Beers.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Beers, BeerViewHolder>(beersOptions) {
            @Override
            protected void onBindViewHolder(@NonNull BeerViewHolder holder, int position, @NonNull Beers model) {

                holder.beer_name.setText(model.getName());
                holder.beer_description.setText(model.getDescription());
                holder.beer_price.setText(model.getPrice());

                onReloadList(model.getName());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        final String select_key = adapter.getRef(position).getKey();

                        beersList.child(select_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Beers beers = dataSnapshot.getValue(Beers.class);

                                valorIndividual = Float.parseFloat(beers.getPrice());
                                quantidade = beers.getName();
                                complementos = beers.getDescription();

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

                                Intent intent = new Intent(BeersList.this, Cart.class);
                                startActivity(intent);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                Snacky.builder()
                                        .setActivity(BeersList.this)
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
            public BeerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.beer_item, viewGroup, false);
                return new BeerViewHolder(itemView);

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
