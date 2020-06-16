package com.devup.opointdoacai.opointdoacai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.devup.opointdoacai.opointdoacai.Database.Database;
import com.devup.opointdoacai.opointdoacai.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import de.mateware.snacky.Snacky;

public class JuiceDoubleFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference spinnerListOne;
    private DatabaseReference spinnerListTwo;

    private String spinner_one_selected;
    private String spinner_two_selected;
    private String price_comb_juices;

    private float valorIndividual = 0.00f;
    private String valor = "";
    private String quantidade = "";
    private String complementos = "";

    private Button btn_combinar;

    private TextView txt_price_view;

    public JuiceDoubleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Common.isConnectedToInternet(getContext())) {
            populateSpinners(txt_price_view);
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
        View view = inflater.inflate(R.layout.fragment_juice_double, container, false);

        //Firebase Instance
        database = FirebaseDatabase.getInstance();
        spinnerListOne = database.getReference("JuicesSpinner");
        spinnerListTwo = database.getReference("JuicesSpinner");

        btn_combinar = view.findViewById(R.id.btn_combinar);

        txt_price_view = view.findViewById(R.id.txt_valor_juices);

        btn_combinar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinner_one_selected.equals("") || spinner_two_selected.equals("")){
                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText("Selecione os dois sabores para combinar!")
                            .setDuration(Snacky.LENGTH_LONG)
                            .warning()
                            .show();
                    return;
                }else{

                    valorIndividual = Float.parseFloat(price_comb_juices);
                    quantidade = "Suco de " + spinner_one_selected + " com " + spinner_two_selected;
                    complementos = "Suco de dois sabores";

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

            }
        });

        if (Common.isConnectedToInternet(getContext())) {
            populateSpinners(txt_price_view);
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

    private void populateSpinners(final TextView txt_price_view) {

        spinnerListOne.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> spinner_one_list = new ArrayList<>();

                for (DataSnapshot spinner_one_snapshot: dataSnapshot.getChildren()){

                    price_comb_juices = spinner_one_snapshot.child("price").getValue(String.class);

                    String spinner_one_name = spinner_one_snapshot.child("name").getValue(String.class);
                    spinner_one_list.add(spinner_one_name);

                }

                Spinner spinner_one = getView().findViewById(R.id.spinner_sabor_um);
                ArrayAdapter<String> spinner_one_adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, spinner_one_list);
                spinner_one_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_one.setAdapter(spinner_one_adapter);

                spinner_one.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 0){
                            spinner_one_selected = "";
                            txt_price_view.setText("Preço R$ " + price_comb_juices);
                        }else{
                            spinner_one_selected = parent.getItemAtPosition(position).toString();
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinnerListTwo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> spinner_two_list = new ArrayList<>();

                for (DataSnapshot spinner_two_snapshot: dataSnapshot.getChildren()){

                    String spinner_two_name = spinner_two_snapshot.child("name").getValue(String.class);
                    spinner_two_list.add(spinner_two_name);

                }

                Spinner spinner_two = getView().findViewById(R.id.spinner_sabor_dois);
                ArrayAdapter<String> spinner_two_adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, spinner_two_list);
                spinner_two_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_two.setAdapter(spinner_two_adapter);

                spinner_two.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 0){
                            spinner_two_selected = "";
                        }else{
                            spinner_two_selected = parent.getItemAtPosition(position).toString();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
