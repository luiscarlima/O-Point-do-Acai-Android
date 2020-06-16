package com.devup.opointdoacai.opointdoacai;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import de.mateware.snacky.Snacky;

public class ResetPassword extends AppCompatActivity {

    private TextView returnToLogin;
    private FloatingActionButton fabReset;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private EditText mEmailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_reset_password);

        //Setando Orientação Padrão para a Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();

        returnToLogin = findViewById(R.id.txt_return_to_login);
        returnToLogin.setPaintFlags(returnToLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassword.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        fabReset = findViewById(R.id.fab_reset);
        fabReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        mEmailField = findViewById(R.id.edt_email_reset);
        mProgressBar = findViewById(R.id.resetProgressBar);

    }

    private void resetPassword() {

        final String email = mEmailField.getText().toString();

        if(!TextUtils.isEmpty(email)){

            mProgressBar.setVisibility(View.VISIBLE);

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        mEmailField.setText("");
                        Snacky.builder()
                                .setActivity(ResetPassword.this)
                                .setText("Um e-mail foi enviado para " + email + "\nAcesse este email para alterar sua senha.")
                                .setDuration(Snacky.LENGTH_LONG)
                                .success()
                                .show();
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }else{
                        Snacky.builder()
                                .setActivity(ResetPassword.this)
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
                    .setActivity(ResetPassword.this)
                    .setText("Digite o e-mail para recuperar a senha.")
                    .setDuration(Snacky.LENGTH_LONG)
                    .warning()
                    .show();
            mProgressBar.setVisibility(View.INVISIBLE);
        }

    }
}
