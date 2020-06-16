package com.devup.opointdoacai.opointdoacai;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.devup.opointdoacai.opointdoacai.ViewHolder.OrderDetailAdapter;
import com.google.firebase.auth.FirebaseAuth;
import java.text.NumberFormat;
import java.util.Locale;

public class OrderDetail extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;

    TextView order_id, order_phone, order_adress, order_total, order_person_name, order_payment_method, order_payment_troco, order_exped_method, order_status;
    String order_id_value = "";
    RecyclerView lstFoods;
    RecyclerView.LayoutManager layoutManager;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.toolbar_order_detail);
        toolbar.setTitle("Detalhes do Pedido");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mAuth = FirebaseAuth.getInstance();

        order_id = findViewById(R.id.order_id_det);
        order_phone = findViewById(R.id.order_phone_det);
        order_adress = findViewById(R.id.order_address_det);
        order_total = findViewById(R.id.order_total_det);
        order_person_name = findViewById(R.id.order_person_name_det);
        order_payment_method = findViewById(R.id.order_payment_method_det);
        order_payment_troco = findViewById(R.id.order_payment_troco_det);
        order_exped_method = findViewById(R.id.order_expedition_det);
        order_status = findViewById(R.id.order_status_det);

        lstFoods = findViewById(R.id.lstFoods);
        lstFoods.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstFoods.setLayoutManager(layoutManager);

        if (getIntent() != null){

            order_id_value = getIntent().getStringExtra("OrderId");

            //Set Value
            order_id.setText("Nº Pedido: " + order_id_value);
            order_phone.setText("Celular: " + Common.currentRequest.getTelefone());
            order_total.setText("Valor Total: " + Common.currentRequest.getTotal());
            order_adress.setText("Endereço: " + Common.currentRequest.getEndereco());
            order_person_name.setText("Nome: " + Common.currentRequest.getNome());
            order_payment_method.setText("Método de Pagamento: " + Common.currentRequest.getPaymentMethod());

            //Canculando valor do troco a ser enviado
            String a = Common.currentRequest.getTroco();
            if (a.equals("")){
                a = "R$0,00";
                order_payment_troco.setText("Troco a ser enviado: " + a);
            }else{

                String b = a.replace("R","");
                String c = b.replace("$","");
                String d = c.replace(",", ".");
                float troco_get = Float.parseFloat(d);

                String one = Common.currentRequest.getTotal();
                String two = one.replace("R","");
                String three = two.replace("$","");
                String four = three.replace(",", ".");
                float total_get = Float.parseFloat(four);

                float troco;
                troco = troco_get - total_get;

                Locale locale = new Locale("PT","BR");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                order_payment_troco.setText("Troco a ser enviado: " + fmt.format(troco));

            }

            order_exped_method.setText("Delivery ou Retira?: " + Common.currentRequest.getShipping());
            order_status.setText("Status do Pedido: " + Common.convertCodeToStatus(Common.currentRequest.getStatus()));

            OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentRequest.getPedidos());
            adapter.notifyDataSetChanged();
            lstFoods.setAdapter(adapter);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}
