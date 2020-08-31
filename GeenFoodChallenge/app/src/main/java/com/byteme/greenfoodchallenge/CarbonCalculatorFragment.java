package com.byteme.greenfoodchallenge;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CarbonCalculatorFragment extends Fragment {

    public CarbonCalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_carbon_calculator, container, false);

        // Make the text in the collapsing toolbar bold
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.DEFAULT_BOLD);

        CardView aboutView = view.findViewById(R.id.about_cardview);
        aboutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAbout = new Intent(getActivity(),AboutActivity.class);
                startActivity(goToAbout);
            }
        });

        CardView quizView = view.findViewById(R.id.quiz_cardview);
        quizView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToQuiz = new Intent(getActivity(),QuestionActivity.class);
                startActivity(goToQuiz);
            }
        });

        CardView historyView = view.findViewById(R.id.history_cardview);
        historyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHistory = new Intent(getActivity(),EditQuizActivity.class);
                startActivity(goToHistory);
            }
        });

        CardView dietView = view.findViewById(R.id.diet_cardview);
        dietView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToDiet = new Intent(getActivity(),SuggestedDietActivity.class);
                startActivity(goToDiet);
            }
        });
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }

}
