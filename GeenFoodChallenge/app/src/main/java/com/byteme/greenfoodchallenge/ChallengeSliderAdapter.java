package com.byteme.greenfoodchallenge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChallengeSliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public ChallengeSliderAdapter(Context context){
        this.context =context;
    }

    //arrays
    public int[] slide_images = {
            R.drawable.intro_welcome,
            R.drawable.intro_greenest,
            R.drawable.intro_pledge
    };

    public String[] slide_headings = {
            "Welcome to the Challenge!",
            "Greenest City 2020",
            "Make a Pledge"

    };

    public String[] slider_descs = {
            "Together, we can build a greener, healthier city!",
            "Our goal is to reduce a total of 328,000 tonnes of CO2e from food by 2020.",
            "This is the amount of CO2e you are pledging to reduce from your diet."
    };


    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription =(TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slider_descs[position]);


        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);

    }
}
