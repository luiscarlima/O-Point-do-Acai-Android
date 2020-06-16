package com.devup.opointdoacai.opointdoacai;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.Map;
import de.mateware.snacky.Snacky;

public class Login extends AppCompatActivity {

    private TextView signUpClick;
    private TextView forgotPass;
    private FloatingActionButton fabEntrar;

    private EditText mEmailField;
    private EditText mPasswordField;

    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    ConstraintLayout rootl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);

        //Setando Orientação Padrão para a Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        signUpClick = findViewById(R.id.sign_up_text_view);
        forgotPass = findViewById(R.id.forgot_password);
        signUpClick.setPaintFlags(signUpClick.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgotPass.setPaintFlags(forgotPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        signUpClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ResetPassword.class);
                startActivity(intent);
            }
        });

        rootl = findViewById(R.id.rootL);


        mEmailField = findViewById(R.id.edt_e_mail);
        mPasswordField = findViewById(R.id.edt_password);

        mProgressBar = findViewById(R.id.progress_login_bar);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        fabEntrar = findViewById(R.id.fab_entrar);

        fabEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    String email = mEmailField.getText().toString();
                    String senha = mPasswordField.getText().toString();

                    if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)){

                        mProgressBar.setVisibility(View.VISIBLE);

                        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    String token_id = FirebaseInstanceId.getInstance().getToken();
                                    String current_id = mAuth.getCurrentUser().getUid();

                                    Map<String, Object> tokenMap = new HashMap<>();
                                    tokenMap.put("token_id", token_id);

                                    mFirestore.collection("Users").document(current_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            sendToMain();
                                            mProgressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });

                                }else{

                                    Snacky.builder()
                                            .setActivity(Login.this)
                                            .setText("Erro: " + task.getException().getMessage())
                                            .setDuration(Snacky.LENGTH_LONG)
                                            .error()
                                            .show();
                                    mProgressBar.setVisibility(View.INVISIBLE);

                                }
                            }
                        });

                    }else{

                        Snacky.builder()
                                .setActivity(Login.this)
                                .setText("Por favor, preencha todos os campos")
                                .setDuration(Snacky.LENGTH_LONG)
                                .error()
                                .show();
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                }else{
                    Snacky.builder()
                            .setActivity(Login.this)
                            .setText("Sem conexão com a Internet")
                            .setDuration(Snacky.LENGTH_LONG)
                            .error()
                            .show();
                    return;
                }
            }
        });

    }

    private void sendToMain() {
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
