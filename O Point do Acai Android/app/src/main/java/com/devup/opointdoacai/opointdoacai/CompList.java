package com.devup.opointdoacai.opointdoacai;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Adapter.PagerViewAdapterComps;
import com.devup.opointdoacai.opointdoacai.Database.Database;
import com.devup.opointdoacai.opointdoacai.Database.DatabaseComps;
import com.devup.opointdoacai.opointdoacai.Model.Order;
import com.devup.opointdoacai.opointdoacai.Model.SelectedComps;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CompList extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;

    private TextView mMousseLabel;
    private TextView mFrutaLabel;
    private TextView mCompLabel;
    private TextView mCoberturaLabel;
    private TextView mLimitedEditionLabel;

    private ViewPager mViewPager;

    private PagerViewAdapterComps mPagerViewAdapter;

    FloatingActionButton fab;

    List<SelectedComps> selected_comps = new ArrayList<>();

    private float total = 0.00f;
    private float valorqnt = 0.00f;
    private String complementos = "";
    private String quantidade = "";
    private String valorTotal = "";

    Dialog mDialog;
    Button buttonConfirmar;
    Button buttonVoltar;
    TextView txtDialogValor;
    TextView txtDialogComp;
    TextView txtDialogQnt;

    private ProgressBar mProgressBar;

    private String someVariable = "l";

    public String getSomeVariable() {

        if (getIntent() != null){

            someVariable = getIntent().getStringExtra("size_choice");

        }

        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_list);

        //Setando Orientação de Retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.toolbar_comps);
        toolbar.setTitle("Selecione os Complementos");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatabaseComps(getBaseContext()).cleanSelectedComps();
                finish();

            }
        });

        mProgressBar = findViewById(R.id.progress_bar);

        mProgressBar.setVisibility(View.VISIBLE);

        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100);
        animation.setDuration(5000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { }

            @Override
            public void onAnimationEnd(Animator animator) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) { }

            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
        animation.start();


        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selected_comps = new DatabaseComps(getBaseContext()).getComps();

                float total = 0.0f;
                for (SelectedComps comps : selected_comps){
                    total+=Float.parseFloat(comps.getCompPrice());
                }

                String complementos = "";
                for (SelectedComps comps : selected_comps){
                    complementos +=comps.getCompName() + ", ";
                }

                if (complementos.isEmpty()){
                    complementos = "Nenhum complemento selecionado";
                }

                if (getIntent() != null){

                    valorqnt = Float.parseFloat(getIntent().getStringExtra("size_price"));

                    total = valorqnt + total;

                    quantidade = getIntent().getStringExtra("size_name");

                    Locale locale = new Locale("PT","BR");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    String vTotal = fmt.format(total);
                    valorTotal = vTotal;

                }

                ConfirmOrCancelDialog(valorTotal, complementos, quantidade);

            }
        });

        mMousseLabel = findViewById(R.id.mousse_pager);
        mFrutaLabel = findViewById(R.id.fruta_pager);
        mCompLabel = findViewById(R.id.comp_pager);
        mCoberturaLabel = findViewById(R.id.cobertura_pager);
        mLimitedEditionLabel = findViewById(R.id.limited_edition_pager);

        mViewPager = findViewById(R.id.view_pager_comps);
        mViewPager.setOffscreenPageLimit(5);

        mPagerViewAdapter = new PagerViewAdapterComps(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerViewAdapter);

        mMousseLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });

        mFrutaLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });

        mCompLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
            }
        });

        mCoberturaLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(3);
            }
        });

        mLimitedEditionLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(4);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {



            }

            @Override
            public void onPageSelected(int position) {

                changeTabs(position);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void changeTabs(int position) {

        if (position == 0){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mMousseLabel.setTextColor(getColor(R.color.colorPrimary));
            }
            mMousseLabel.setTextSize(18);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mFrutaLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mFrutaLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCompLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mCompLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCoberturaLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mCoberturaLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mLimitedEditionLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mLimitedEditionLabel.setTextSize(14);

        }

        if (position == 1){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mMousseLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mMousseLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mFrutaLabel.setTextColor(getColor(R.color.colorPrimary));
            }
            mFrutaLabel.setTextSize(18);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCompLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mCompLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCoberturaLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mCoberturaLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mLimitedEditionLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mLimitedEditionLabel.setTextSize(14);

        }

        if (position == 2){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mMousseLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mMousseLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mFrutaLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mFrutaLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCompLabel.setTextColor(getColor(R.color.colorPrimary));
            }
            mCompLabel.setTextSize(18);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCoberturaLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mCoberturaLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mLimitedEditionLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mLimitedEditionLabel.setTextSize(14);

        }

        if (position == 3){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mMousseLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mMousseLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mFrutaLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mFrutaLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCompLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mCompLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCoberturaLabel.setTextColor(getColor(R.color.colorPrimary));
            }
            mCoberturaLabel.setTextSize(18);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mLimitedEditionLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mLimitedEditionLabel.setTextSize(14);

        }

        if (position == 4){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mMousseLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mMousseLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mFrutaLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mFrutaLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCompLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mCompLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCoberturaLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mCoberturaLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mLimitedEditionLabel.setTextColor(getColor(R.color.colorPrimary));
            }
            mLimitedEditionLabel.setTextSize(18);

        }
    }

    public void ConfirmOrCancelDialog(final String valorTotal, final String complementos, final String quantidade) {

        mDialog = new Dialog(CompList.this);
        mDialog.setContentView(R.layout.dialog_confirm_cancel);
        mDialog.setTitle("Confirmar Pedido?");

        txtDialogQnt = mDialog.findViewById(R.id.txt_tipo_dialog);
        txtDialogQnt.setText(quantidade);

        txtDialogValor = mDialog.findViewById(R.id.txt_valor_dialog);
        txtDialogValor.setText(valorTotal);

        txtDialogComp = mDialog.findViewById(R.id.txt_comp_dialog);
        txtDialogComp.setText(complementos);

        buttonConfirmar = mDialog.findViewById(R.id.card_dialog_btn_confirm);
        buttonVoltar = mDialog.findViewById(R.id.card_dialog_btn_cancel);

        buttonConfirmar.setEnabled(true);
        buttonVoltar.setEnabled(true);

        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String select_key_comps = String.valueOf(System.currentTimeMillis());

                new Database(getBaseContext()).addToCart(new Order(
                        select_key_comps,
                        quantidade,
                        complementos,
                        valorTotal
                ));

                Intent intent = new Intent(CompList.this, Cart.class);
                startActivity(intent);
                finish();

            }
        });

        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.cancel();

            }
        });

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

    }

    @Override
    public void onBackPressed() {

        new DatabaseComps(getBaseContext()).cleanSelectedComps();
        finish();

    }
}
