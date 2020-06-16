package com.devup.opointdoacai.opointdoacai;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.devup.opointdoacai.opointdoacai.Database.DatabaseComps;
import com.devup.opointdoacai.opointdoacai.Model.Comps;
import com.devup.opointdoacai.opointdoacai.Model.SelectedComps;
import com.devup.opointdoacai.opointdoacai.ViewHolder.CompsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import de.mateware.snacky.Snacky;


public class FragmentMousses extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference compList;

    private String flag_size;

    FirebaseRecyclerAdapter<Comps, CompsViewHolder> adapter;

    public FragmentMousses() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Common.isConnectedToInternet(getContext())) {
            loadListComps();
        }else{
            Snacky.builder()
                    .setActivity(getActivity())
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get flag para verificação
        flag_size = ((CompList) this.getActivity()).getSomeVariable();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mousses, container, false);

        //Firebase Instance
        database = FirebaseDatabase.getInstance();
        compList = database.getReference("Comps");

        recyclerView = view.findViewById(R.id.recycler_mousses_comp);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(getContext())) {
            loadListComps();
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

    private void loadListComps() {

        Query search = compList.orderByChild("type").equalTo("mousse");
        FirebaseRecyclerOptions<Comps> compOptions = new FirebaseRecyclerOptions.Builder<Comps>()
                .setQuery(search, Comps.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Comps, CompsViewHolder>(compOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final CompsViewHolder holder, final int position, @NonNull final Comps model) {

                holder.comp_name.setText(model.getName());

                //Verifica se o cliente selecionou o tamanho "1 Litro" e dobra o valor dos complementos
                if (flag_size.equals("1 Litro")){
                    float price = Float.parseFloat(model.getPrice());
                    float result = price * 2;
                    holder.comp_price.setText(String.valueOf(result));
                }else{
                    holder.comp_price.setText(model.getPrice());
                }

                holder.comp_select.setOnCheckedChangeListener(null);
                holder.comp_select.setChecked(model.isSelected());

                holder.comp_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        model.setSelected(b);

                        if (compoundButton.isChecked()){

                            if (flag_size.equals("1 Litro")){
                                float price = Float.parseFloat(model.getPrice());
                                float result = price * 2;
                                holder.comp_price.setText(String.valueOf(result));

                                new DatabaseComps(getContext()).addToSelectedComps(new SelectedComps(
                                        model.getName(),
                                        String.valueOf(result)
                                ));
                            }else{
                                new DatabaseComps(getContext()).addToSelectedComps(new SelectedComps(
                                        model.getName(),
                                        model.getPrice()
                                ));
                            }

                        }else if (!compoundButton.isChecked()){

                            String key = model.getName();

                            new DatabaseComps(getContext()).cleanOfSelectedComps(key);

                        }
                    }
                });
            }

            @NonNull
            @Override
            public CompsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comp_item, parent, false);
                return new CompsViewHolder(itemView);
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
        getActivity().finish();

    }



}
