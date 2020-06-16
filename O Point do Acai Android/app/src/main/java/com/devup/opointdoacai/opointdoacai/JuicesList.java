package com.devup.opointdoacai.opointdoacai;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.devup.opointdoacai.opointdoacai.Adapter.PagerViewAdapter;


public class JuicesList extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;

    private TextView mMilkLabel;
    private TextView mWaterLabel;
    private TextView mDoubleLabel;

    private ViewPager mViewPager;

    private PagerViewAdapter mPagerViewAdapter;

    private ProgressBar mProgressBar;


    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juices_list);

        mProgressBar = findViewById(R.id.progress_bar_juices);

        mProgressBar.setVisibility(View.VISIBLE);

        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100);
        animation.setDuration(3000);
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

        //Setando Orientação de Retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mMilkLabel = findViewById(R.id.milk_pager_id);
        mWaterLabel = findViewById(R.id.water_pager_id);
        mDoubleLabel = findViewById(R.id.double_pager_id);

        mViewPager = findViewById(R.id.view_pager_id);
        mViewPager.setOffscreenPageLimit(2);

        mPagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerViewAdapter);

        mMilkLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mViewPager.setCurrentItem(0);

            }
        });

        mWaterLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mViewPager.setCurrentItem(1);

            }
        });

        mDoubleLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPager.setCurrentItem(2);

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {

                changeTabs(position);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //Toolbar - Instanciando
        toolbar = findViewById(R.id.toolbar_id_juices);
        toolbar.setTitle("Sucos");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

    private void changeTabs(int position) {

        if (position == 0){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mMilkLabel.setTextColor(getColor(R.color.colorPrimary));
            }
            mMilkLabel.setTextSize(18);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mWaterLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mWaterLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mDoubleLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mDoubleLabel.setTextSize(14);

        }

        if (position == 1){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mMilkLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mMilkLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mWaterLabel.setTextColor(getColor(R.color.colorPrimary));
            }
            mWaterLabel.setTextSize(18);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mDoubleLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mDoubleLabel.setTextSize(14);

        }

        if (position == 2){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mMilkLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mMilkLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mWaterLabel.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            mWaterLabel.setTextSize(14);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mDoubleLabel.setTextColor(getColor(R.color.colorPrimary));
            }
            mDoubleLabel.setTextSize(18);

        }

    }

    @Override
    public void onBackPressed() {

        finish();

    }
}
