package com.devup.opointdoacai.opointdoacai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.devup.opointdoacai.opointdoacai.Database.Database;
import com.devup.opointdoacai.opointdoacai.Interface.ItemClickListener;
import com.devup.opointdoacai.opointdoacai.Model.Juices;
import com.devup.opointdoacai.opointdoacai.Model.Order;
import com.devup.opointdoacai.opointdoacai.ViewHolder.JuicesViewHolder;
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

public class JuiceWaterFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference juicesList;

    FirebaseRecyclerAdapter<Juices, JuicesViewHolder> adapter;

    private float valorIndividual = 0.00f;
    private String valor = "";
    private String quantidade = "";
    private String complementos = "";

    public JuiceWaterFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Common.isConnectedToInternet(getContext())) {
            loadListJuices();
        }else{
            Snacky.builder()
                    .setActivity(getActivity())
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
            return;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_juice_water, container, false);

        //Firebase Instance
        database = FirebaseDatabase.getInstance();
        juicesList = database.getReference("Juices");

        recyclerView = view.findViewById(R.id.recycler_juice_water);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(getContext())) {
            loadListJuices();
        }else{
            Snacky.builder()
                    .setActivity(getActivity())
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
        }

        return view;

    }

    private void loadListJuices() {

        Query search = juicesList.orderByChild("base").equalTo("Agua");
        FirebaseRecyclerOptions<Juices> juicesOptions = new FirebaseRecyclerOptions.Builder<Juices>()
                .setQuery(search, Juices.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Juices, JuicesViewHolder>(juicesOptions) {
            @Override
            protected void onBindViewHolder(@NonNull JuicesViewHolder holder, int position, @NonNull Juices model) {

                holder.juice_name.setText(model.getName());
                holder.juice_description.setText(model.getDescription());
                holder.juice_price.setText(model.getPrice());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        final String select_key = adapter.getRef(position).getKey();

                        juicesList.child(select_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Juices juices = dataSnapshot.getValue(Juices.class);

                                valorIndividual = Float.parseFloat(juices.getPrice());
                                quantidade = juices.getName();
                                complementos = juices.getDescription();

                                Locale locale = new Locale("PT","BR");
                                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                                valor = fmt.format(valorIndividual);

                                String key_id = String.valueOf(System.currentTimeMillis());

                                new Database(getContext()).addToCart(new Order(
                                        key_id,
                                        quantidade,
                                        complementos,
                                        valor
                                ));

                                Intent intent = new Intent(getContext(), Cart.class);
                                startActivity(intent);
                                getActivity().finish();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });

            }

            @NonNull
            @Override
            public JuicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.juices_item, parent, false);
                return new JuicesViewHolder(itemView);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStop() {
        super.onStop();

        if (Common.isConnectedToInternet(getContext())) {
            adapter.stopListening();
        }else{
            return;
        }

    }
}
