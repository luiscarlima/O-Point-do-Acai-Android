package com.devup.opointdoacai.opointdoacai;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.devup.opointdoacai.opointdoacai.Model.Config;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class FidelityCard extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference requests;
    private DatabaseReference config;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private android.support.v7.widget.Toolbar toolbar;

    private int count = 0;

    private ImageView img_01;
    private ImageView img_02;
    private ImageView img_03;
    private ImageView img_04;
    private ImageView img_05;
    private ImageView img_06;
    private ImageView img_07;
    private ImageView img_08;
    private ImageView img_09;
    private ImageView img_10;

    private TextView txt_fide_info;
    private TextView txt_fide_gift1;
    private TextView txt_fide_gift2;

    private LottieAnimationView btn_lottie_resgatar;

    @Override
    protected void onStart() {
        super.onStart();

        if (Common.isConnectedToInternet(getBaseContext())) {
            calculateFidelity();
        }else{
            Snacky.builder()
                    .setActivity(FidelityCard.this)
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
        setContentView(R.layout.activity_fidelity_card);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        //Setando Orientação de Retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.fidelity_toolbar);
        toolbar.setTitle("Cartão Fidelidade");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        //Firebase Instance
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Pedidos");
        config = database.getReference("Config");

        img_01 = findViewById(R.id.icon_state_1);
        img_02 = findViewById(R.id.icon_state_2);
        img_03 = findViewById(R.id.icon_state_3);
        img_04 = findViewById(R.id.icon_state_4);
        img_05 = findViewById(R.id.icon_state_5);
        img_06 = findViewById(R.id.icon_state_6);
        img_07 = findViewById(R.id.icon_state_7);
        img_08 = findViewById(R.id.icon_state_8);
        img_09 = findViewById(R.id.icon_state_9);
        img_10 = findViewById(R.id.icon_state_10);
        txt_fide_info = findViewById(R.id.txt_fide_info);
        txt_fide_gift1 = findViewById(R.id.txt_click_on_gift_bonus1);
        txt_fide_gift2 = findViewById(R.id.txt_click_on_gift_bonus2);

        config.child("01").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String bonus_value = "00.00";
                Config configs = dataSnapshot.getValue(Config.class);
                bonus_value = configs.getValor();

                txt_fide_info.setText("Complete o Cartão Fidelidade para receber um Bônus de R$ " + bonus_value + " para comprar o que você quiser no App do Point!");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_lottie_resgatar = findViewById(R.id.animation_view_gift_btn);
        btn_lottie_resgatar.setEnabled(false);


        if (Common.isConnectedToInternet(getBaseContext())) {
            calculateFidelity();
        }else{
            Snacky.builder()
                    .setActivity(FidelityCard.this)
                    .setText("Sem conexão com a Internet")
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();
            return;
        }

    }

    private void calculateFidelity() {

        requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data_snapshot: dataSnapshot.getChildren()){

                    String user_phone = data_snapshot.child("id").getValue(String.class);
                    String status = data_snapshot.child("status").getValue(String.class);
                    String is_getting = data_snapshot.child("isGetting").getValue(String.class);

                    if (status.equals("3") && is_getting.equals("true") && user_phone.equals(mAuth.getCurrentUser().getUid().toString())){
                        count = count + 1;
                    }

                }

                //Completa o Card
                completeCard(count);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_lottie_resgatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gravarValorBonus();

            }
        });

    }

    private void gravarValorBonus() {

        final String user_id = mAuth.getCurrentUser().getUid();

        config.child("01").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String bonus_value = "00.00";

                Config configs = dataSnapshot.getValue(Config.class);

                bonus_value = configs.getValor();

                Map<String, Object> bonus_map_user = new HashMap<>();
                bonus_map_user.put("bonus_value", bonus_value);

                mFirestore.collection("Users").document(user_id).update(bonus_map_user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        setFalseRequests();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ERROR", "Error writing document", e);
                    }
                });

                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setFalseRequests() {

        requests.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data_snapshot: dataSnapshot.getChildren()){

                    String user_phone = data_snapshot.child("telefone").getValue(String.class);
                    String status = data_snapshot.child("status").getValue(String.class);
                    String is_getting = data_snapshot.child("isGetting").getValue(String.class);

                    if (status.equals("3") && is_getting.equals("true") && user_phone.equals(mAuth.getCurrentUser().getPhoneNumber().toString())){

                        requests.child(data_snapshot.getKey()).child("isGetting").setValue("false");

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void completeCard(int counted) {

        if (counted == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_01.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
        }else if (counted == 2) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_01.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_02.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }

        }else if (counted == 3) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_01.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_02.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_03.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }

        }else if (counted == 4) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_01.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_02.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_03.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_04.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }

        }else if (counted == 5) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_01.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_02.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_03.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_04.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_05.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }

        }else if (counted == 6) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_01.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_02.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_03.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_04.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_05.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_06.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }

        }else if (counted == 7) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_01.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_02.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_03.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_04.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_05.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_06.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_07.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }

        }else if (counted == 8) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_01.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_02.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_03.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_04.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_05.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_06.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_07.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_08.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }

        }else if (counted == 9) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_01.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_02.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_03.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_04.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_05.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_06.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_07.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_08.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_09.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }

        }else if (counted >= 10) {

            btn_lottie_resgatar.setVisibility(View.VISIBLE);
            btn_lottie_resgatar.setEnabled(true);
            txt_fide_gift1.setVisibility(View.VISIBLE);
            txt_fide_gift2.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_01.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_02.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_03.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_04.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_05.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_06.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_07.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_08.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_09.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_10.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
            }

        }

    }
}
