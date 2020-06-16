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
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.Model.Comps;
import com.devup.opointdoacai.opointdoacai.ViewHolder.FruitsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import de.mateware.snacky.Snacky;

public class FruitsList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference fruitsList;

    FirebaseRecyclerAdapter<Comps, FruitsViewHolder> adapter;

    private android.support.v7.widget.Toolbar toolbar;

    float size_price = 0.00f;
    String size_name = "";
    String size_choice = "";

    private ProgressBar mProgressBar;

    @Override
    protected void onRestart() {
        super.onRestart();

        if (Common.isConnectedToInternet(getBaseContext())) {
            loadListFruits(size_price, size_name, size_choice);
        }else{
            Snacky.builder()
                    .setActivity(FruitsList.this)
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
        setContentView(R.layout.activity_fruits_list);

        //Setando Orientação de Retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.toolbar_fruits);
        toolbar.setTitle("Escolha a Fruta");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mProgressBar = findViewById(R.id.progress_bar_fruits);

        mProgressBar.setVisibility(View.VISIBLE);

        //Firebase Instance
        database = FirebaseDatabase.getInstance();
        fruitsList = database.getReference("Comps");

        recyclerView = findViewById(R.id.recycler_fruits);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null){

            size_price = Float.parseFloat(getIntent().getStringExtra("size_price"));
            size_name = getIntent().getStringExtra("size_name");
            size_choice = getIntent().getStringExtra("size_choice");

            if (Common.isConnectedToInternet(getBaseContext())) {
                loadListFruits(size_price, size_name, size_choice);
            }else{
                Snacky.builder()
                        .setActivity(FruitsList.this)
                        .setText("Sem conexão com a Internet")
                        .setDuration(Snacky.LENGTH_LONG)
                        .error()
                        .show();
                mProgressBar.setVisibility(View.INVISIBLE);
                return;
            }

        }

    }

    private void loadListFruits(final float size_price, final String size_name, final String size_choice) {

        Query search = fruitsList.orderByChild("type").equalTo("fruta");
        FirebaseRecyclerOptions<Comps> fruitOptions = new FirebaseRecyclerOptions.Builder<Comps>()
                .setQuery(search, Comps.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Comps, FruitsViewHolder>(fruitOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final FruitsViewHolder holder, final int position, @NonNull final Comps model) {

                holder.fruit_name.setText(model.getName());
                holder.fruit_price.setText(model.getPrice());

                onReloadList(model.getName());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent intent = new Intent(FruitsList.this, CompList.class);

                        float get_price = Float.parseFloat(model.getPrice());
                        String result = String.valueOf(get_price + size_price);

                        intent.putExtra("size_price", result);

                        String replaced = size_name.replace("Frutas", model.getName());

                        intent.putExtra("size_name", replaced);
                        intent.putExtra("size_choice", size_choice);
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public FruitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fruits_item, parent, false);
                return new FruitsViewHolder(itemView);
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

        finish();

    }

}
