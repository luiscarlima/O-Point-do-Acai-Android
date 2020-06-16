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
import com.devup.opointdoacai.opointdoacai.Model.Tops;
import com.devup.opointdoacai.opointdoacai.ViewHolder.TopChoicesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import de.mateware.snacky.Snacky;

public class TopChoices extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference topChoicesList;

    FirebaseRecyclerAdapter<Tops, TopChoicesViewHolder> adapter;

    private android.support.v7.widget.Toolbar toolbar;

    private ProgressBar mProgressBar;

    @Override
    protected void onRestart() {
        super.onRestart();

        if (Common.isConnectedToInternet(getBaseContext())) {
            loadListTopChoices();
        }else{
            Snacky.builder()
                    .setActivity(TopChoices.this)
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
        setContentView(R.layout.activity_top_choices);

        //Setando Orientação de Retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.top_choices_toolbar);
        toolbar.setTitle("Top 10 do Point");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mProgressBar = findViewById(R.id.progress_bar_tops);

        mProgressBar.setVisibility(View.VISIBLE);

        //Firebase Instance
        database = FirebaseDatabase.getInstance();
        topChoicesList = database.getReference("Tops");

        recyclerView = findViewById(R.id.recycler_top_choices);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(getBaseContext())) {
            loadListTopChoices();
        }else{
            Snacky.builder()
                    .setActivity(TopChoices.this)
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        }

    }

    private void loadListTopChoices() {

        Query search = topChoicesList;
        FirebaseRecyclerOptions<Tops> topChoicesOptions = new FirebaseRecyclerOptions.Builder<Tops>()
                .setQuery(search, Tops.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Tops, TopChoicesViewHolder>(topChoicesOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TopChoicesViewHolder holder, int position, @NonNull Tops model) {

                holder.top_name.setText(model.getName());
                holder.top_description.setText(model.getDescription());
                holder.top_number.setText(model.getTop_number());

                onReloadList(model.getName());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        String select_key = adapter.getRef(position).getKey();
                        Intent intent = new Intent(TopChoices.this, TopChoiceSizes.class);
                        intent.putExtra("top_key", select_key);
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public TopChoicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.top_choices_item, parent, false);
                return new TopChoicesViewHolder(itemView);

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
