package com.devup.opointdoacai.opointdoacai;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Adapter.CartAdapter;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.devup.opointdoacai.opointdoacai.Database.Database;
import com.devup.opointdoacai.opointdoacai.Helper.RecyclerItemTouchHelper;
import com.devup.opointdoacai.opointdoacai.Interface.RecyclerItemTouchHelperListener;
import com.devup.opointdoacai.opointdoacai.Listener.ZipCodeListener;
import com.devup.opointdoacai.opointdoacai.Model.Address;
import com.devup.opointdoacai.opointdoacai.Model.Config;
import com.devup.opointdoacai.opointdoacai.Model.MyResponse;
import com.devup.opointdoacai.opointdoacai.Model.Notification;
import com.devup.opointdoacai.opointdoacai.Model.Order;
import com.devup.opointdoacai.opointdoacai.Model.Request;
import com.devup.opointdoacai.opointdoacai.Model.Sender;
import com.devup.opointdoacai.opointdoacai.Model.Token;
import com.devup.opointdoacai.opointdoacai.Remote.APIService;
import com.devup.opointdoacai.opointdoacai.ViewHolder.CartViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shuhart.stepview.StepView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import de.mateware.snacky.Snacky;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;
    private DatabaseReference config;
    private FirebaseFirestore mFirestore;

    TextView txtTotal;
    Button btnConfirmarPedido;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    Dialog adressDialog;
    Button adressDialogBtn;

    EditText adressRua;
    EditText adressNum;
    EditText adressBairro;
    EditText etZipCode;
    Spinner spinnerCidade;

    String cidadeSelected;

    float total = 0;
    float frete = 0;

    private FirebaseAuth mAuth;

    private Util util;

    RelativeLayout rootLayout;

    private android.support.v7.widget.Toolbar toolbar;

    private FloatingActionButton floatingActionButtonAdd;

    APIService mService;

    //Stepview
    private int currentStep = 0;
    RelativeLayout layout, layout01, layout02, layout03;
    StepView stepViewCart;

    private static String uniqueIdentifier = null;
    private static final String UNIQUE_ID = "UNIQUE_ID";
    private static final long ONE_HOUR_MILLI = 60*60*1000;

    private Button btnDelivery;
    private Button btnRetira;
    private Button btnProximo;
    private Button btnProxObs;
    private Button btnFinalizar;

    private String deliveryOrRetira = "";
    private String troco = "";
    private String observacao;

    EditText edtObservacao;
    EditText edtTroco;
    EditText edtZipCode;
    EditText edtRua;
    EditText edtNum;
    EditText edtBairro;
    Spinner spinnerCity;
    TextView txtTroco;
    TextView txtFinalTotal;
    TextView txtFrete;

    Dialog stepViewDialog;

    RadioButton rdiCashPayment;
    RadioButton rdiCardPayment;
    RadioButton rdiCashCardPayment;

    AlertDialog order_dialog;

    String endereco = "";

    float bonus_value = 0.00f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Iniciando Services
        mService = Common.getFCMService();

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.toolbar_id_cart);
        toolbar.setTitle("Carrinho");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        floatingActionButtonAdd = findViewById(R.id.fab_cart_add);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Cart.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Pedidos");
        config = database.getReference("Config");
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        //Init
        recyclerView = findViewById(R.id.list_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Swipe to Delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

        txtTotal = findViewById(R.id.total);
        btnConfirmarPedido = findViewById(R.id.btn_confirmar_pedido);

        rootLayout = findViewById(R.id.rootLayout);

        //Calculando valor total - eventual bonus fidelidade
        final String user_id = mAuth.getCurrentUser().getUid();
        mFirestore.collection("Users").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                float bonus_value = Float.parseFloat(documentSnapshot.getString("bonus_value"));

                loadListPedidos(total, bonus_value);

            }
        });


        btnConfirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    if (cart.size() > 0) {

                        Calendar now = Calendar.getInstance();
                        int sem = now.get(Calendar.DAY_OF_WEEK);
                        int hour = now.get(Calendar.HOUR_OF_DAY);
                        int minute = now.get(Calendar.MINUTE);

                        if (sem == 2){
                            Snacky.builder()
                                    .setActivity(Cart.this)
                                    .setText("Não abrimos às Segunda-feiras.\nObrigado pela compreensão.")
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .warning()
                                    .show();
                        }else{

                            if ((hour < 14) && (hour > 0)){

                                int hora = 13 - hour;
                                int minutos = 60 - minute;

                                if (hora == 0){
                                    Snacky.builder()
                                            .setActivity(Cart.this)
                                            .setText("Abriremos dentro de " + minutos + " minuto(s)")
                                            .setDuration(Snacky.LENGTH_LONG)
                                            .info()
                                            .show();
                                }else{
                                    Snacky.builder()
                                            .setActivity(Cart.this)
                                            .setText("Abriremos dentro de " + hora + " hora(s) e " + minutos + " minuto(s)")
                                            .setDuration(Snacky.LENGTH_LONG)
                                            .info()
                                            .show();
                                }


                            }else if ((hour >= 21) && (minute >= 30)){
                                Snacky.builder()
                                        .setActivity(Cart.this)
                                        .setText("Poxa, pedimos desculpas. Mas já encerramos por hoje")
                                        .setDuration(Snacky.LENGTH_LONG)
                                        .warning()
                                        .show();
                            }else{

                                callDialogStepView();

                            }

                        }
                    } else {
                        Snacky.builder()
                                .setActivity(Cart.this)
                                .setText("Seu carrinho de compras está vazio.\nAdicione items para prosseguir em seu pedido.")
                                .setDuration(Snacky.LENGTH_LONG)
                                .warning()
                                .show();
                    }
                }else{
                    Snacky.builder()
                            .setActivity(Cart.this)
                            .setText("Sem conexão com a Internet")
                            .setDuration(Snacky.LENGTH_LONG)
                            .error()
                            .show();
                return;
                }

            }

        });



        //Setando Orientação Padrão para a Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
    }

    private void callDialogStepView() {

        stepViewDialog = new Dialog(Cart.this);
        stepViewDialog.setContentView(R.layout.dialog_step_view);
        stepViewDialog.setTitle("");

        edtObservacao = stepViewDialog.findViewById(R.id.edt_observ_stepview);
        edtTroco = stepViewDialog.findViewById(R.id.edt_troco);
        edtZipCode = stepViewDialog.findViewById(R.id.edt_cep_stepview);
        edtRua = stepViewDialog.findViewById(R.id.edt_rua_stepview);
        edtNum = stepViewDialog.findViewById(R.id.edt_num_stepview);
        edtBairro = stepViewDialog.findViewById(R.id.edt_bairro_stepview);
        spinnerCity = stepViewDialog.findViewById(R.id.spinner_cidade_stepview);
        rdiCashPayment = stepViewDialog.findViewById(R.id.rdiCOD_stepview3);
        rdiCardPayment = stepViewDialog.findViewById(R.id.rdiCard_stepview3);
        rdiCashCardPayment = stepViewDialog.findViewById(R.id.rdiCardCOD_stepview3);
        btnDelivery = stepViewDialog.findViewById(R.id.btn_delivery);
        btnRetira = stepViewDialog.findViewById(R.id.btn_retira);
        btnProximo = stepViewDialog.findViewById(R.id.btn_prox_stepview2);
        btnProxObs = stepViewDialog.findViewById(R.id.btn_prox_stepview);
        btnFinalizar = stepViewDialog.findViewById(R.id.btn_finalizar_stepview3);
        layout = stepViewDialog.findViewById(R.id.layout_step);
        layout01 = stepViewDialog.findViewById(R.id.layout_step_1);
        layout02 = stepViewDialog.findViewById(R.id.layout_step_2);
        layout03 = stepViewDialog.findViewById(R.id.layout_step_3);
        txtTroco = stepViewDialog.findViewById(R.id.txt_dialog_stepview3_troco);
        txtFinalTotal = stepViewDialog.findViewById(R.id.txt_dialog_total_stepview3);
        txtFrete = stepViewDialog.findViewById(R.id.txt_dialog_frete);

        config.child("02").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Config configs = dataSnapshot.getValue(Config.class);
                txtFrete.setText("Valor do Frete R$ " + configs.getValor());
                frete = Float.parseFloat(configs.getValor());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txtFinalTotal.setText("Total " + txtTotal.getText().toString());

        stepViewCart = stepViewDialog.findViewById(R.id.step_view_cart);
        stepViewCart.setStepsNumber(4);

        stepViewCart.go(0, true);
        layout.setVisibility(View.VISIBLE);

        btnProxObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                observacao = edtObservacao.getText().toString();


                if (currentStep < stepViewCart.getStepCount() - 1) {
                    currentStep++;
                    stepViewCart.go(currentStep, true);
                } else {
                    stepViewCart.done(true);
                }

                layout.setVisibility(View.GONE);
                layout01.setVisibility(View.VISIBLE);

            }
        });

        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                frete = frete;

                //Calculando/
                String ff = "";
                String ss = "";
                String tt = "";
                String fff = "";
                float auxs = 0;
                ff = txtTotal.getText().toString();
                ss = ff.replace("R", "");
                tt = ss.replace("$", "");
                fff = tt.replace(",", ".");
                auxs = Float.parseFloat(fff);

                float totalizando = auxs + frete;

                String final_totals = String.valueOf(totalizando);

                txtFinalTotal.setText("Total R$ " + final_totals);

                deliveryOrRetira = "Entregar no Endereço";

                if (currentStep < stepViewCart.getStepCount() - 1) {
                    currentStep++;
                    stepViewCart.go(currentStep, true);
                } else {
                    stepViewCart.done(true);
                }

                layout01.setVisibility(View.GONE);
                layout02.setVisibility(View.VISIBLE);

            }
        });

        btnRetira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                frete = 0.00f;

                deliveryOrRetira = "Retira no Balcão";

                if (currentStep < stepViewCart.getStepCount() - 1) {
                    currentStep++;
                    currentStep++;
                    stepViewCart.go(currentStep, true);
                } else {
                    stepViewCart.done(true);
                }

                layout01.setVisibility(View.GONE);
                layout02.setVisibility(View.GONE);
                layout03.setVisibility(View.VISIBLE);

                edtTroco.setVisibility(View.INVISIBLE);
                txtTroco.setVisibility(View.INVISIBLE);

            }
        });

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cep = edtZipCode.getText().toString();
                String rua = edtRua.getText().toString();
                String num = edtNum.getText().toString();
                String bairro = edtBairro.getText().toString();
                String cidade = String.valueOf(spinnerCity.getSelectedItem());
                endereco = rua + ", " + num + ", " + bairro + ", " + cep + ", " + cidade + " - SP";

                if (TextUtils.isEmpty(edtRua.getText()) || TextUtils.isEmpty(edtNum.getText()) || TextUtils.isEmpty(edtBairro.getText())) {
                    Snacky.builder()
                            .setActivity(Cart.this)
                            .setText("Por favor, preencha com todos os dados")
                            .setDuration(Snacky.LENGTH_LONG)
                            .error()
                            .show();
                } else {

                    if (currentStep < stepViewCart.getStepCount() - 1) {
                        currentStep++;
                        stepViewCart.go(currentStep, true);
                    } else {
                        stepViewCart.done(true);
                    }
                    layout02.setVisibility(View.GONE);
                    layout01.setVisibility(View.GONE);
                    layout03.setVisibility(View.VISIBLE);

                }

            }
        });

        rdiCardPayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()) {
                    txtTroco.setVisibility(View.INVISIBLE);
                    edtTroco.setVisibility(View.INVISIBLE);
                }

            }
        });

        rdiCashCardPayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()) {
                    txtTroco.setVisibility(View.VISIBLE);
                    edtTroco.setVisibility(View.VISIBLE);
                }

            }
        });

        rdiCashPayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()) {
                    txtTroco.setVisibility(View.VISIBLE);
                    edtTroco.setVisibility(View.VISIBLE);
                }

            }
        });

        edtTroco.addTextChangedListener(new MascaraMonetaria(edtTroco));

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Calculando troco
                String one = "";
                String two = "";
                String three = "";
                String four = "";
                one = edtTroco.getText().toString();
                two = one.replace("R","");
                three = two.replace("$","");
                four = three.replace(",", ".");
                troco = four;

                //Calculando valor total - eventual bonus fidelidade
                final String user_id = mAuth.getCurrentUser().getUid();
                mFirestore.collection("Users").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        float bonus_value = Float.parseFloat(documentSnapshot.getString("bonus_value"));
                        String telefone = documentSnapshot.getString("telefone");

                        cart = new Database(getApplicationContext()).getCarts();
                        adapter = new CartAdapter(cart, getApplicationContext());
                        recyclerView.setAdapter(adapter);

                        //Calculando Preco Total
                        String firstString = "";
                        String secondString = "";
                        String thirdString = "";
                        String fourtyString = "";
                        float aux = 0;
                        for (Order order : cart) {
                            firstString = order.getPreco();
                            secondString = firstString.replace("R", "");
                            thirdString = secondString.replace("$", "");
                            fourtyString = thirdString.replace(",", ".");
                            aux = Float.parseFloat(fourtyString);
                            total += aux;
                        }

                        float m_final_total = total;
                        String result_bonus = "0.00";

                        if (m_final_total < bonus_value) {

                            result_bonus = String.valueOf(Math.abs(m_final_total - bonus_value) + 0.00f);

                            gravarNewBonus(result_bonus);

                        } else {

                            result_bonus = String.valueOf(0.00f);

                            gravarNewBonus(result_bonus);

                        }


                        //Firebase User
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        //Checando método de pagamento

                        if (!rdiCardPayment.isChecked() && !rdiCashPayment.isChecked() && !rdiCashCardPayment.isChecked()) {
                            Snacky.builder()
                                    .setActivity(Cart.this)
                                    .setText("Selecione uma opção de pagamento")
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .error()
                                    .show();
                        } else if (rdiCardPayment.isChecked()) {

                            //Calculando/
                            String f = "";
                            String s = "";
                            String t = "";
                            String ff = "";
                            float auxs = 0;
                            f = txtTotal.getText().toString();
                            s = f.replace("R", "");
                            t = s.replace("$", "");
                            ff = t.replace(",", ".");
                            auxs = Float.parseFloat(ff);

                            float totalizando = auxs + frete;

                            String uid = mAuth.getCurrentUser().getUid();

                            String final_total = String.valueOf(totalizando);

                            Request request = new Request(
                                    uid,
                                    telefone,
                                    firebaseUser.getEmail(),
                                    firebaseUser.getDisplayName(),
                                    endereco,
                                    final_total,
                                    "Cartão",
                                    "0",
                                    "0",
                                    deliveryOrRetira,
                                    cart,
                                    "false",
                                    observacao
                            );

                            String order_number = String.valueOf(System.currentTimeMillis());

                            //Enviando ao Firebase
                            requests.child(order_number)
                                    .setValue(request);

                            //Esvaziando o carrinho
                            new Database(getBaseContext()).cleanCart();

                            sendNotificationOrder(order_number);

                            if (currentStep < stepViewCart.getStepCount() - 1) {
                                currentStep++;
                                stepViewCart.go(currentStep, true);
                            } else {
                                stepViewCart.done(true);
                            }

                            //Enviando para a Atividade Principal
                            LayoutInflater inflater = getLayoutInflater();
                            View alertLayout = inflater.inflate(R.layout.processing_order_animation, null);
                            AlertDialog.Builder show = new AlertDialog.Builder(Cart.this);
                            show.setView(alertLayout);
                            show.setCancelable(false);
                            order_dialog = show.create();
                            order_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            order_dialog.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    order_dialog.dismiss();
                                    startActivity(new Intent(Cart.this, OrderStatus.class));
                                    stepViewDialog.dismiss();
                                    finish();
                                }
                            }, 2500);

                        } else if (rdiCashPayment.isChecked()) {

                            //Calculando/
                            String f = "";
                            String s = "";
                            String t = "";
                            String ff = "";
                            float auxs = 0;
                            f = txtTotal.getText().toString();
                            s = f.replace("R", "");
                            t = s.replace("$", "");
                            ff = t.replace(",", ".");
                            auxs = Float.parseFloat(ff);

                            float totalizando = auxs + frete;

                            String final_total = String.valueOf(totalizando);

                            String uid = mAuth.getCurrentUser().getUid();

                            Request request = new Request(
                                    uid,
                                    telefone,
                                    firebaseUser.getEmail(),
                                    firebaseUser.getDisplayName(),
                                    endereco,
                                    final_total,
                                    "Dinheiro",
                                    troco,
                                    "0",
                                    deliveryOrRetira,
                                    cart,
                                    "false",
                                    observacao
                            );

                            String order_number = String.valueOf(System.currentTimeMillis());

                            //Enviando ao Firebase
                            requests.child(String.valueOf(System.currentTimeMillis()))
                                    .setValue(request);

                            //Esvaziando o carrinho
                            new Database(getBaseContext()).cleanCart();

                            sendNotificationOrder(order_number);

                            if (currentStep < stepViewCart.getStepCount() - 1) {
                                currentStep++;
                                stepViewCart.go(currentStep, true);
                            } else {
                                stepViewCart.done(true);
                            }

                            //Enviando para a Atividade Principal
                            LayoutInflater inflater = getLayoutInflater();
                            View alertLayout = inflater.inflate(R.layout.processing_order_animation, null);
                            AlertDialog.Builder show = new AlertDialog.Builder(Cart.this);
                            show.setView(alertLayout);
                            show.setCancelable(false);
                            order_dialog = show.create();
                            order_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            order_dialog.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    order_dialog.dismiss();
                                    startActivity(new Intent(Cart.this, OrderStatus.class));
                                    stepViewDialog.dismiss();
                                    finish();
                                }
                            }, 2500);

                        } else if (rdiCashCardPayment.isChecked()) {

                            //Calculando/
                            String f = "";
                            String s = "";
                            String t = "";
                            String ff = "";
                            float auxs = 0;
                            f = txtTotal.getText().toString();
                            s = f.replace("R", "");
                            t = s.replace("$", "");
                            ff = t.replace(",", ".");
                            auxs = Float.parseFloat(ff);

                            float totalizando = auxs + frete;

                            String final_total = String.valueOf(totalizando);

                            String uid = mAuth.getCurrentUser().getUid();

                            Request request = new Request(
                                    uid,
                                    telefone,
                                    firebaseUser.getEmail(),
                                    firebaseUser.getDisplayName(),
                                    endereco,
                                    final_total,
                                    "Dinheiro e Cartão",
                                    troco,
                                    "0",
                                    deliveryOrRetira,
                                    cart,
                                    "false",
                                    observacao
                            );

                            String order_number = String.valueOf(System.currentTimeMillis());

                            //Enviando ao Firebase
                            requests.child(String.valueOf(order_number))
                                    .setValue(request);

                            //Esvaziando o carrinho
                            new Database(getBaseContext()).cleanCart();

                            sendNotificationOrder(order_number);

                            if (currentStep < stepViewCart.getStepCount() - 1) {
                                currentStep++;
                                stepViewCart.go(currentStep, true);
                            } else {
                                stepViewCart.done(true);
                            }

                            //Enviando para a Atividade Principal
                            LayoutInflater inflater = getLayoutInflater();
                            View alertLayout = inflater.inflate(R.layout.processing_order_animation, null);
                            AlertDialog.Builder show = new AlertDialog.Builder(Cart.this);
                            show.setView(alertLayout);
                            show.setCancelable(false);
                            order_dialog = show.create();
                            order_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            order_dialog.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    order_dialog.dismiss();
                                    startActivity(new Intent(Cart.this, OrderStatus.class));
                                    stepViewDialog.dismiss();
                                    finish();
                                }
                            }, 2500);

                        }

                    }
                });

            }

        });//btnFinalizar

        edtZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                edtRua.setEnabled(false);
                edtBairro.setEnabled(false);
                spinnerCity.setEnabled(false);

                if (s.length() == 0){

                    edtRua.setEnabled(true);
                    edtBairro.setEnabled(true);
                    spinnerCity.setEnabled(true);

                    edtRua.setText("");
                    edtBairro.setText("");
                    spinnerCity.setSelection(0);

                }

                if (s.length() == 8){

                    edtNum.requestFocus();

                }
            }
        });

        edtNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (false == hasFocus){

                    ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(edtNum.getWindowToken(), 0);

                }

            }
        });

        edtZipCode.addTextChangedListener(new ZipCodeListener(this));

        util = new Util(this,
                R.id.edt_rua_stepview,
                R.id.edt_bairro_stepview);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cidades_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapter);

        spinnerCity.setOnItemSelectedListener(this);

        stepViewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        stepViewDialog.show();

        currentStep = 0;

        //....
    }

    private void gravarNewBonus(String result_bonus) {

        //Gravando o novo valor do Bonus do Usuário
        final String user_id = mAuth.getCurrentUser().getUid();

        Map<String, Object> bonus_map_user = new HashMap<>();
        bonus_map_user.put("bonus_value", result_bonus);

        mFirestore.collection("Users").document(user_id).update(bonus_map_user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SUCCESS", "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("ERROR", "Error writing document", e);
            }
        });

    }

    private void sendNotificationOrder(final String order_number) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = tokens.orderByChild("isServerToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapShot:dataSnapshot.getChildren()){

                    Token serverToken = postSnapShot.getValue(Token.class);

                    //Criando um raw payload para enviar a notificação

                    Notification notification = new Notification("O Point do Açaí", "Você tem um novo pedido " + order_number);
                    Sender content = new Sender(serverToken.getToken(), notification);

                    mService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    //Somente executará com o resultado da aplicação cliente igual a 200
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {

                                        } else {
                                            Snacky.builder()
                                                    .setActivity(Cart.this)
                                                    .setText("Algo deu errado, por favor entre em contato direto conosco.")
                                                    .setDuration(Snacky.LENGTH_LONG)
                                                    .error()
                                                    .show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERROR",t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String getZipCode(){
        return edtZipCode.getText().toString();
    }

    public String getUriRequest(){
        return "https://viacep.com.br/ws/"+getZipCode()+"/json/";
    }


    public void setAddressFields( Address address){
        setField( R.id.edt_rua_stepview, address.getLogradouro() );
        setField( R.id.edt_bairro_stepview, address.getBairro() );
        setSpinner( R.id.spinner_cidade_stepview, R.array.cidades_array, address.getLocalidade() );
    }

    private void setField( int fieldId, String data ){
        ((EditText) stepViewDialog.findViewById( fieldId )).setText( data );
    }

    private void setSpinner( int fieldId, int arrayId, String cidade ){
        Spinner spinner = stepViewDialog.findViewById( fieldId );
        String[] cidades = getResources().getStringArray(arrayId);

        for( int i = 0; i < cidades.length; i++ ){
            if( cidades[i].equals(cidade) ){
                spinner.setSelection( i );
                break;
            }else if( cidades[i].equals(cidade) ){
                spinner.setSelection( i );
                break;
            }
        }
    }

    private float loadListPedidos(float total, float bonus_value) {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);

        //Calculando Preco Total
        String firstString = "";
        String secondString = "";
        String thirdString = "";
        String fourtyString = "";
        float aux = 0;
        for (Order order:cart){
            firstString = order.getPreco();
            secondString = firstString.replace("R","");
            thirdString = secondString.replace("$","");
            fourtyString = thirdString.replace(",", ".");
            aux = Float.parseFloat(fourtyString);
            total+=aux;
        }


        Locale locale = new Locale("PT","BR");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        if (total < bonus_value){

            txtTotal.setText(fmt.format(0));

            return total;

        }else{

            txtTotal.setText(fmt.format(total - bonus_value));

            return total;

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.DELETE)){
            deleteCart(item.getOrder());
        }

        return true;
    }

    private void deleteCart(int position) {

        cart.remove(position);

        new Database(this).cleanCart();

        for (Order item:cart){
            new Database(this).addToCart(item);
        }

        //Calculando valor total - eventual bonus fidelidade
        final String user_id = mAuth.getCurrentUser().getUid();
        mFirestore.collection("Users").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                float bonus_value = Float.parseFloat(documentSnapshot.getString("bonus_value"));

                loadListPedidos(total, bonus_value);
            }
        });

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        cidadeSelected = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof CartViewHolder) {

            String name = cart.get(viewHolder.getAdapterPosition()).getQuantidade();

            final Order deleteItem = cart.get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(deleteIndex);
            new Database(getBaseContext()).removeFromCart(deleteItem.getProductId());

            //Calculando valor total - eventual bonus fidelidade
            final String user_id = mAuth.getCurrentUser().getUid();
            mFirestore.collection("Users").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    float bonus_value = Float.parseFloat(documentSnapshot.getString("bonus_value"));

                    loadListPedidos(total, bonus_value);
                }
            });

            //Make a Snackbar
            Snackbar snackbar = Snackbar.make(rootLayout, name + " removido do carrinho", Snackbar.LENGTH_LONG);
            snackbar.setAction("RESTAURAR", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    adapter.restoreItem(deleteItem, deleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);

                    //Calculando valor total - eventual bonus fidelidade
                    final String user_id = mAuth.getCurrentUser().getUid();
                    mFirestore.collection("Users").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            float bonus_value = Float.parseFloat(documentSnapshot.getString("bonus_value"));

                            loadListPedidos(total, bonus_value);
                        }
                    });

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private class MascaraMonetaria implements TextWatcher{

        final EditText campo;

        public MascaraMonetaria(EditText campo) {
            super();
            this.campo = campo;
        }

        private boolean isUpdating = false;
        // Pega a formatacao do sistema, se for brasil R$ se EUA US$
        private NumberFormat nf = NumberFormat.getCurrencyInstance();

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int after) {
            // Evita que o método seja executado varias vezes.
            // Se tirar ele entre em loop
            if (isUpdating) {
                isUpdating = false;
                return;
            }

            isUpdating = true;
            String str = s.toString();
            // Verifica se já existe a máscara no texto.
            boolean hasMask = ((str.indexOf("R$") > -1 || str.indexOf("$") > -1) &&
                    (str.indexOf(".") > -1 || str.indexOf(",") > -1));
            // Verificamos se existe máscara
            if (hasMask) {
                // Retiramos a máscara.
                str = str.replaceAll("[R$]", "").replaceAll("[,]", "")
                        .replaceAll("[.]", "");
            }

            try {
                // Transformamos o número que está escrito no EditText em
                // monetário.
                str = nf.format(Double.parseDouble(str) / 100);
                campo.setText(str);
                campo.setSelection(campo.getText().length());
            } catch (NumberFormatException e) {
                s = "";
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // Não utilizado
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Não utilizado
        }
    }
}
