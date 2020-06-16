package com.devup.opointdoacai.opointdoacai;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.Map;
import de.mateware.snacky.Snacky;

public class SignUp extends AppCompatActivity {

    private TextView signInClick;
    private TextView txt_terms;
    private FloatingActionButton fabCadastrar;

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mNameField;
    private EditText mTelField;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private ProgressBar mRegisterProgressBar;

    private CheckBox checkBoxTerms;

    ConstraintLayout rootLayout;

    Dialog mDialog;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_sign_up);

        //Setando Orientação Padrão para a Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        rootLayout = findViewById(R.id.rootLayout);

        checkBoxTerms = findViewById(R.id.checkbox_terms);

        mRegisterProgressBar = findViewById(R.id.registerProgressBar);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        signInClick = findViewById(R.id.txt_sign_in);
        txt_terms = findViewById(R.id.txt_terms);
        signInClick.setPaintFlags(signInClick.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_terms.setPaintFlags(txt_terms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txt_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTermsAndCondtions();
            }
        });

        signInClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });

        mNameField = findViewById(R.id.edt_nome);
        mTelField = findViewById(R.id.edt_tel);
        mEmailField = findViewById(R.id.edt_email);
        mPasswordField = findViewById(R.id.edt_senha);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(mTelField, smf);
        mTelField.addTextChangedListener(mtw);

        fabCadastrar = findViewById(R.id.fab_cadastrar);

        fabCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {
                    mRegisterProgressBar.setVisibility(View.VISIBLE);

                    final String nome = mNameField.getText().toString();
                    final String telefone = mTelField.getText().toString();
                    String email = mEmailField.getText().toString();
                    String senha = mPasswordField.getText().toString();

                    if(!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(telefone) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)){

                        if (checkBoxTerms.isChecked()){
                            mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        String user_id = mAuth.getCurrentUser().getUid();
                                        String token_id = FirebaseInstanceId.getInstance().getToken();
                                        String bonus_value = "0.00";

                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put("name", nome);
                                        userMap.put("telefone", telefone);
                                        userMap.put("token_id", token_id);
                                        userMap.put("bonus_value", bonus_value);

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(nome)
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                        }
                                                    }
                                                });

                                        mFirestore.collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                                sendToMain();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snacky.builder()
                                                        .setActivity(SignUp.this)
                                                        .setText("Erro: " + e.getMessage())
                                                        .setDuration(Snacky.LENGTH_LONG)
                                                        .error()
                                                        .show();
                                                mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                            }
                                        });

                                    }else {
                                        Snacky.builder()
                                                .setActivity(SignUp.this)
                                                .setText("Erro: " + task.getException().getMessage())
                                                .setDuration(Snacky.LENGTH_LONG)
                                                .error()
                                                .show();
                                        mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                        return;
                                    }
                                }
                            });
                        }else{
                            Snacky.builder()
                                    .setActivity(SignUp.this)
                                    .setText("É necessário aceitar os Termos e Condições de Uso.\nLeia os Termos e Condições e clique na caixa de seleção para aceitar.")
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .warning()
                                    .show();
                            mRegisterProgressBar.setVisibility(View.INVISIBLE);
                            return;
                        }

                    }else{

                        Snacky.builder()
                                .setActivity(SignUp.this)
                                .setText("Por favor, preencha todos os campos")
                                .setDuration(Snacky.LENGTH_LONG)
                                .error()
                                .show();
                        mRegisterProgressBar.setVisibility(View.INVISIBLE);
                        return;
                    }
                }else{
                    Snacky.builder()
                            .setActivity(SignUp.this)
                            .setText("Sem conexão com a Internet")
                            .setDuration(Snacky.LENGTH_LONG)
                            .error()
                            .show();
                    return;
                }

            }
        });


    }

    private void showTermsAndCondtions() {

        mDialog = new Dialog(SignUp.this);
        mDialog.setContentView(R.layout.dialog_terms_and_conditions);

        btnClose = mDialog.findViewById(R.id.dialog_close_ok);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

    }

    private void sendToMain() {
        Intent mainIntent = new Intent(SignUp.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
