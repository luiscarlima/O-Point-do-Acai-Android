package com.devup.opointdoacai.opointdoacai;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Database.Database;
import com.devup.opointdoacai.opointdoacai.Model.Order;
import com.devup.opointdoacai.opointdoacai.Model.Tops;
import com.devup.opointdoacai.opointdoacai.ViewHolder.TopChoicesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.NumberFormat;
import java.util.Locale;

public class TopChoiceSizes extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference topSizes;

    FirebaseRecyclerAdapter<Tops, TopChoicesViewHolder> adapter;

    private Toolbar toolbar;

    private String top_key = "";

    private TextView txt_price_one;
    private TextView txt_price_two;
    private TextView txt_price_three;

    private TextView txt_number_opc;
    private TextView txt_name_opc;
    private TextView txt_description_opc;

    private CardView opc1, opc2, opc3;

    private String complementos = "";
    private String quantidade = "";
    private String top = "";
    private float valor300 = 0.00f;
    private float valor500 = 0.00f;
    private float valor700 = 0.00f;
    private String valor = "";

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_choice_sizes);

        //Setando Orientação de Retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.toolbar_id_top_choice_sizes);
        toolbar.setTitle("Escolha o Tamanho do Copo");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        //Instanciando TextViews
        txt_price_one = findViewById(R.id.top_txt_preco_300);
        txt_price_two = findViewById(R.id.top_txt_preco_500);
        txt_price_three = findViewById(R.id.top_txt_preco_700);

        txt_number_opc = findViewById(R.id.txt_selected_top_number_choice);
        txt_name_opc = findViewById(R.id.txt_top_name_selected_choice);
        txt_description_opc = findViewById(R.id.txt_top_complementos_selected_choice);

        //Instanciando CardViews
        opc1 = findViewById(R.id.top_300);
        opc2 = findViewById(R.id.top_500);
        opc3 = findViewById(R.id.top_700);

        //Firebase Instance
        database = FirebaseDatabase.getInstance();
        topSizes = database.getReference("Tops");

        if (getIntent() != null){
            top_key = getIntent().getStringExtra("top_key");
        }if (!top_key.isEmpty()){

            getSelectTopChoice(top_key);

            loadSizesList(top_key);

            topSizes.child(top_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Tops selectValue = dataSnapshot.getValue(Tops.class);

                    valor300 = Float.parseFloat(selectValue.getPrice_one());
                    valor500 = Float.parseFloat(selectValue.getPrice_two());
                    valor700 = Float.parseFloat(selectValue.getPrice_three());
                    quantidade = selectValue.getName();
                    complementos = selectValue.getDescription();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        opc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Locale locale = new Locale("PT","BR");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                valor = fmt.format(valor300);

                String key_id = String.valueOf(System.currentTimeMillis());

                new Database(getBaseContext()).addToCart(new Order(
                        key_id,
                        quantidade,
                        complementos,
                        valor
                ));

                Intent intent = new Intent(TopChoiceSizes.this, Cart.class);
                startActivity(intent);
                finish();

            }
        });

        opc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Locale locale = new Locale("PT","BR");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                valor = fmt.format(valor500);

                String key_id = String.valueOf(System.currentTimeMillis());

                new Database(getBaseContext()).addToCart(new Order(
                        key_id,
                        quantidade,
                        complementos,
                        valor
                ));

                Intent intent = new Intent(TopChoiceSizes.this, Cart.class);
                startActivity(intent);
                finish();

            }
        });

        opc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Locale locale = new Locale("PT","BR");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                valor = fmt.format(valor700);

                String key_id = String.valueOf(System.currentTimeMillis());

                new Database(getBaseContext()).addToCart(new Order(
                        key_id,
                        quantidade,
                        complementos,
                        valor
                ));

                Intent intent = new Intent(TopChoiceSizes.this, Cart.class);
                startActivity(intent);
                finish();

            }
        });

    }


    private void getSelectTopChoice(String top_key) {

        topSizes.child(top_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Tops choice = dataSnapshot.getValue(Tops.class);

                txt_number_opc.setText(choice.getTop_number());
                txt_name_opc.setText(choice.getName());
                txt_description_opc.setText(choice.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadSizesList(String key) {

        topSizes.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Tops tops = dataSnapshot.getValue(Tops.class);

                txt_price_one.setText("R$ " + tops.getPrice_one());
                txt_price_two.setText("R$ " + tops.getPrice_two());
                txt_price_three.setText("R$ " + tops.getPrice_three());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}
