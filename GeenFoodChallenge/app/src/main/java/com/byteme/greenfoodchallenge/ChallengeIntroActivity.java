package com.byteme.greenfoodchallenge;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChallengeIntroActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots = new TextView[3];
    TextView next;
    TextView skip;
    int lastPageIndex = mDots.length - 1;
    RelativeLayout introLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_intro);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        introLayout = findViewById(R.id.intro_layout);
        skip = findViewById(R.id.previous);
        skip.setText(getResources().getString(R.string.skip));
        skip.setEnabled(true);
        next = findViewById(R.id.next);
        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.dotsLayout);

        ChallengeSliderAdapter sliderAdapter = new ChallengeSliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListner);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPageIndex = mSlideViewPager.getCurrentItem();
                if(currentPageIndex == lastPageIndex){
                    Intent goToPledge = new Intent(getApplicationContext(),PledgeActivity.class);
                    startActivity(goToPledge);
                }
                else {
                    mSlideViewPager.setCurrentItem(mSlideViewPager.getCurrentItem() + 1, true);
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToPledge = new Intent(getApplicationContext(),PledgeActivity.class);
                startActivity(goToPledge);
            }
        });
    }

    public  void addDotsIndicator(int position){
        mDotLayout.removeAllViews();

        for(int i =0 ; i<mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.white));

            mDotLayout.addView(mDots[i]);
        }
        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.dotscolor));
        }
    }

    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);

            if(i==0){
                introLayout.setBackgroundResource(R.color.about_bg1);
            }
            else if(i==1){
                introLayout.setBackgroundResource(R.color.about_bg2);
            }
            else if(i==2){
                introLayout.setBackgroundResource(R.color.about_bg3);
            }


            if(i == lastPageIndex){
                next.setText(getResources().getString(R.string.got_it));
            }
            else {
                next.setText(getResources().getString(R.string.next_cap));
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    // If the user presses the Android back button before completing the intro slides, take them back to the Home page
    @Override
    public void onBackPressed() {
        Intent goBack = new Intent(getApplicationContext(), CarbonCalculatorActivity.class);
        goBack.putExtra("fragmentToLoad", 0); // Home value is 0
        startActivity(goBack);
    }
}
