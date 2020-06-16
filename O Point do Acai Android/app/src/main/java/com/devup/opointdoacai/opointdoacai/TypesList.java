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
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.Model.Types;
import com.devup.opointdoacai.opointdoacai.ViewHolder.TypesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import de.mateware.snacky.Snacky;


public class TypesList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference typesList;

    FirebaseRecyclerAdapter<Types, TypesViewHolder> adapter;

    private android.support.v7.widget.Toolbar toolbar;

    private ProgressBar mProgressBar;


    @Override
    protected void onRestart() {
        super.onRestart();

        if (Common.isConnectedToInternet(getBaseContext())) {
            loadTypesList();
        }else{
            Snacky.builder()
                    .setActivity(TypesList.this)
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
        setContentView(R.layout.activity_types_list);

        //Setando Orientação de Retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.toolbar_types);
        toolbar.setTitle("Escolha o Tipo");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mProgressBar = findViewById(R.id.progress_bar_types);

        mProgressBar.setVisibility(View.VISIBLE);

        //Firebase Instance
        database = FirebaseDatabase.getInstance();
        typesList = database.getReference("Types");

        recyclerView = findViewById(R.id.recycler_types);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(getBaseContext())) {
            loadTypesList();
        }else{
            Snacky.builder()
                    .setActivity(TypesList.this)
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        }

    }

    private void loadTypesList() {

        Query search = typesList;
        FirebaseRecyclerOptions<Types> sizesOptions = new FirebaseRecyclerOptions.Builder<Types>()
                .setQuery(search, Types.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Types, TypesViewHolder>(sizesOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TypesViewHolder holder, int position, @NonNull final Types model) {

                holder.type_name.setText(model.getName());

                onReloadList(model.getName());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent intent = new Intent(TypesList.this, SizesList.class);
                        intent.putExtra("type_price", model.getPrice());
                        intent.putExtra("type_name", model.getName());
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public TypesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.types_item, parent, false);
                return new TypesViewHolder(itemView);
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
