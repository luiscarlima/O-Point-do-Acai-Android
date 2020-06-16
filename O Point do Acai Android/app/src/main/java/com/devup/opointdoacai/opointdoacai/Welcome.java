package com.devup.opointdoacai.opointdoacai;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import de.mateware.snacky.Snacky;

public class Welcome extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button click;

    //Layouts
    RelativeLayout rl_one;
    RelativeLayout rl_two;

    //Animations
    Animation up_to_down;
    Animation down_to_up;

    @Override
    public void onStart() {
        super.onStart();

        if (Common.isConnectedToInternet(getBaseContext())) {

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                startActivity(new Intent(Welcome.this, MainActivity.class));
                finish();
            }

        }else{
            Snacky.builder()
                    .setActivity(Welcome.this)
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

        requestWindowFeature(1);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_welcome);

        //Setando Orientação Padrão para a Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mAuth = FirebaseAuth.getInstance();

        click = findViewById(R.id.click);
        rl_one = findViewById(R.id.l1);
        rl_two = findViewById(R.id.l2);

        up_to_down = AnimationUtils.loadAnimation(this, R.anim.up_to_down);
        down_to_up = AnimationUtils.loadAnimation(this, R.anim.down_to_up);

        rl_one.setAnimation(up_to_down);
        rl_two.setAnimation(down_to_up);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {
                    Intent intent = new Intent(Welcome.this, Login.class);
                    startActivity(intent);
                    finish();
                }else{
                    Snacky.builder()
                            .setActivity(Welcome.this)
                            .setText("Sem conexão com a Internet")
                            .setDuration(Snacky.LENGTH_LONG)
                            .error()
                            .show();
                    return;
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }

}
