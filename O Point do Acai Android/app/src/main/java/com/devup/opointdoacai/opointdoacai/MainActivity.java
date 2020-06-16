package com.devup.opointdoacai.opointdoacai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.devup.opointdoacai.opointdoacai.Database.DatabaseComps;
import com.devup.opointdoacai.opointdoacai.Model.Token;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import de.mateware.snacky.Snacky;

public class MainActivity extends AppCompatActivity {

    //Variavel de Views
    private android.support.v7.widget.Toolbar toolbar;

    //Authentication
    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //Firestore Backend
    private FirebaseFirestore mFirestore;

    //Componetes Buttons Cards
    private Button btnAcai;
    private Button btnAcaiTop;
    private Button btnSucos;
    private Button btnVitaminas;
    private Button btnSaladaDeFrutas;
    private Button btnBeer;
    private Button btnPots;
    private Button btnSnacks;

    //Componetes Cards
    private CardView cardViewAcai;
    private CardView cardViewAcaiTop;
    private CardView cardViewSucos;
    private CardView cardViewVitaminas;
    private CardView cardViewSaladadDeFrutas;
    private CardView cardViewBeer;
    private CardView cardViewPots;
    private CardView cardViewSnacks;

    //Componentes Buttons Drawer
    private RelativeLayout rlHome;
    private RelativeLayout rlPedidos;
    private RelativeLayout rlCarrinho;
    private RelativeLayout rlSair;
    private RelativeLayout rlFidelidade;
    private RelativeLayout rlAbout;


    //Variáveis
    private String name;
    private String phone;
    private FlowingDrawer mDrawer;

    //SharedPreferences
    SharedPreferences sPreferences = null;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        if (Common.isConnectedToInternet(getBaseContext())) {

            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                    if (currentUser != null) {

                        updateToken(FirebaseInstanceId.getInstance().getToken());

                        final String user_id = mAuth.getCurrentUser().getUid();
                        String token_id = FirebaseInstanceId.getInstance().getToken();

                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("token_id", token_id);

                        mFirestore.collection("Users").document(user_id).update(userMap);


                    } else {
                        Intent intent = new Intent(MainActivity.this, Welcome.class);
                        startActivity(intent);
                        finish();
                    }

                }
            };

        }else{
            Snacky.builder()
                    .setActivity(MainActivity.this)
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
            return;
        }
    }

    private void updateToken(String token) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token, false);
        tokens.child(mAuth.getCurrentUser().getDisplayName()).setValue(data);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences Tap Target View
        sPreferences = getSharedPreferences("firstRun", MODE_PRIVATE);

        //Instanciando Componentes da Nav Drawer
        rlHome = findViewById(R.id.home_acess);
        rlSair = findViewById(R.id.out_acess);
        rlPedidos = findViewById(R.id.order_acess);
        rlCarrinho = findViewById(R.id.cart_acess);
        rlFidelidade = findViewById(R.id.fide_acess);
        rlAbout = findViewById(R.id.about_acess);

        rlAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawer.closeMenu();
                Intent intent = new Intent(MainActivity.this, DevUp.class);
                startActivity(intent);

            }
        });

        rlFidelidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawer.closeMenu();
                Intent intent = new Intent(MainActivity.this, FidelityCard.class);
                startActivity(intent);

            }
        });

        rlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawer.closeMenu();

            }
        });

        rlCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawer.closeMenu();
                Intent intent = new Intent(MainActivity.this, Cart.class);
                startActivity(intent);

            }
        });

        rlPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawer.closeMenu();
                Intent intent = new Intent(MainActivity.this, OrderStatus.class);
                startActivity(intent);

            }
        });


        rlSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                mDrawer.closeMenu();
                finish();
            }
        });


        //Toolbar - Instanciando
        toolbar = findViewById(R.id.toolbar_id_main);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawer.toggleMenu();

            }
        });

        mDrawer = findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {          }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
            }
        });


        mAuth = FirebaseAuth.getInstance();

        btnAcai = findViewById(R.id.btn_acai);
        btnAcaiTop = findViewById(R.id.btn_acai_top);
        btnSucos = findViewById(R.id.btn_sucos);
        btnVitaminas = findViewById(R.id.btn_vitaminas);
        btnSaladaDeFrutas = findViewById(R.id.btn_saladadefrutas);
        btnBeer = findViewById(R.id.btn_bebidas);
        btnPots = findViewById(R.id.btn_poteacai);
        btnSnacks = findViewById(R.id.btn_salgados);

        btnAcai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatabaseComps(getBaseContext()).cleanSelectedComps();

                Intent intent = new Intent(MainActivity.this, TypesList.class);
                startActivity(intent);

            }
        });

        btnAcaiTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TopChoices.class);
                startActivity(intent);

            }
        });

        btnVitaminas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, VitaminsList.class);
                startActivity(intent);

            }
        });

        btnSucos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, JuicesList.class);
                startActivity(intent);

            }
        });

        btnSaladaDeFrutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TypeFruitSalads.class);
                startActivity(intent);
            }
        });

        btnBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, BeersList.class);
                startActivity(intent);

            }
        });

        btnPots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, PotsList.class);
                startActivity(intent);

            }
        });

        btnSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SnacksList.class);
                startActivity(intent);

            }
        });

        //Instanciando os Cardviews
        cardViewAcai = findViewById(R.id.card_view);
        cardViewAcaiTop = findViewById(R.id.card_view2);
        cardViewSucos = findViewById(R.id.card_view3);
        cardViewVitaminas = findViewById(R.id.card_view4);
        cardViewSaladadDeFrutas = findViewById(R.id.card_view6);
        cardViewBeer = findViewById(R.id.card_view7);
        cardViewPots = findViewById(R.id.card_view8);
        cardViewSnacks = findViewById(R.id.card_view9);

        cardViewAcai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatabaseComps(getBaseContext()).cleanSelectedComps();

                Intent intent = new Intent(MainActivity.this, TypesList.class);
                startActivity(intent);

            }
        });

        cardViewAcaiTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TopChoices.class);
                startActivity(intent);

            }
        });

        cardViewVitaminas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, VitaminsList.class);
                startActivity(intent);

            }
        });

        cardViewSucos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, JuicesList.class);
                startActivity(intent);

            }
        });

        cardViewSaladadDeFrutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TypeFruitSalads.class);
                startActivity(intent);
            }
        });

        cardViewBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, BeersList.class);
                startActivity(intent);

            }
        });

        cardViewPots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, PotsList.class);
                startActivity(intent);

            }
        });

        cardViewSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SnacksList.class);
                startActivity(intent);

            }
        });

    }

    private void callTapTargetView() {

        TapTargetView.showFor(MainActivity.this,
                TapTarget.forView(findViewById(R.id.main_layout_target), "Seja Bem Vindo ao Aplicativo do Point do Açaí!\n\n\n\nMonte seu Açaí de forma rápida!", "É só escolher o tamanho do copo e adicionar as combinações desejadas.")
                        .outerCircleColor(R.color.colorPrimary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.colorTextWithe)
                        .titleTextSize(20)
                        .titleTextColor(R.color.colorTextWithe)
                        .descriptionTextSize(16)
                        .descriptionTextColor(R.color.colorTextWithe)
                        .textColor(R.color.colorTextWithe)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.colorPrimaryDark)
                        .drawShadow(true)
                        .cancelable(true)
                        .tintTarget(false)
                        .transparentTarget(false)
                        .targetRadius(110),
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                    }

                    @Override
                    public void onTargetCancel(TapTargetView view) {
                        super.onTargetCancel(view);
                    }

                });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null){

            mAuth.removeAuthStateListener(mAuthStateListener);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAuth.addAuthStateListener(mAuthStateListener);

        if (sPreferences.getBoolean("firstRun", true)) {
            sPreferences.edit().putBoolean("firstRun", false).apply();
            callTapTargetView();
        }

    }

    @Override
    public void onBackPressed() {
    }

}
