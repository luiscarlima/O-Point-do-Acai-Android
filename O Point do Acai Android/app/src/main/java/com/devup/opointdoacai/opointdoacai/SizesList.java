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
import com.devup.opointdoacai.opointdoacai.Model.Sizes;
import com.devup.opointdoacai.opointdoacai.ViewHolder.SizesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import de.mateware.snacky.Snacky;

public class SizesList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference sizesList;

    FirebaseRecyclerAdapter<Sizes, SizesViewHolder> adapter;

    private android.support.v7.widget.Toolbar toolbar;

    private float type_price = 0.00f;
    private String type_name = "";

    private ProgressBar mProgressBar;


    @Override
    protected void onRestart() {
        super.onRestart();

        if (Common.isConnectedToInternet(getBaseContext())) {
            loadSizesList(type_name, type_price);
        }else{
            Snacky.builder()
                    .setActivity(SizesList.this)
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
        setContentView(R.layout.activity_sizes_list);

        //Setando Orientação de Retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.toolbar_sizes);
        toolbar.setTitle("Escolha o Tamanho do Copo");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mProgressBar = findViewById(R.id.progress_bar_sizes);

        mProgressBar.setVisibility(View.VISIBLE);

        //Firebase Instance
        database = FirebaseDatabase.getInstance();
        sizesList = database.getReference("Sizes");

        recyclerView = findViewById(R.id.recycler_sizes);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null){

            type_price = Float.parseFloat(getIntent().getStringExtra("type_price"));
            type_name = getIntent().getStringExtra("type_name");

            if (Common.isConnectedToInternet(getBaseContext())) {
                loadSizesList(type_name, type_price);
            }else{
                Snacky.builder()
                        .setActivity(SizesList.this)
                        .setText("Sem conexão com a Internet")
                        .setDuration(Snacky.LENGTH_LONG)
                        .error()
                        .show();
                mProgressBar.setVisibility(View.INVISIBLE);
                return;
            }

        }

    }

    private void loadSizesList(final String type_name, final float type_price) {

        Query search = sizesList;
        FirebaseRecyclerOptions<Sizes> sizesOptions = new FirebaseRecyclerOptions.Builder<Sizes>()
                .setQuery(search, Sizes.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Sizes, SizesViewHolder>(sizesOptions) {
            @Override
            protected void onBindViewHolder(@NonNull SizesViewHolder holder, int position, @NonNull final Sizes model) {

                holder.size_name.setText(model.getName());

                onReloadList(model.getName());

                if (type_name.equals("Açaí")){

                    holder.size_price.setText(model.getPrice());

                }else if (type_name.equals("Açaí Diet")){

                    float get_price = Float.parseFloat(model.getPrice());
                    String result = String.valueOf(get_price + type_price);

                    holder.size_price.setText(result);

                }else if (type_name.equals("Açaí Batido com Frutas")){

                    float get_price = Float.parseFloat(model.getPrice());
                    String result = String.valueOf(get_price + type_price);

                    holder.size_price.setText(result);

                }

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        if (type_name.equals("Açaí")){

                            Intent intent = new Intent(SizesList.this, CompList.class);
                            intent.putExtra("size_price", model.getPrice());
                            intent.putExtra("size_name", type_name + " de " + model.getName());
                            intent.putExtra("size_choice", model.getName());
                            startActivity(intent);

                        }else if (type_name.equals("Açaí Diet")){

                            Intent intent = new Intent(SizesList.this, CompList.class);
                            float get_price = Float.parseFloat(model.getPrice());
                            String result = String.valueOf(get_price + type_price);
                            intent.putExtra("size_price", result);
                            intent.putExtra("size_name", type_name + " de " + model.getName());
                            intent.putExtra("size_choice", model.getName());
                            startActivity(intent);

                        }else if (type_name.equals("Açaí Batido com Frutas")){

                            Intent intent = new Intent(SizesList.this, FruitsList.class);
                            float get_price = Float.parseFloat(model.getPrice());
                            String result = String.valueOf(get_price + type_price);
                            intent.putExtra("size_price", result);
                            intent.putExtra("size_name", type_name + " de " + model.getName());
                            intent.putExtra("size_choice", model.getName());
                            startActivity(intent);

                        }

                    }
                });

            }

            @NonNull
            @Override
            public SizesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.sizes_item, parent, false);
                return new SizesViewHolder(itemView);
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
