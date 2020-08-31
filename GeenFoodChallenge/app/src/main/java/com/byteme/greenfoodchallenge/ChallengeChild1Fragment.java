package com.byteme.greenfoodchallenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import pledgeCode.CallBackValue;
import pledgeCode.FirebaseMethods;


public class ChallengeChild1Fragment extends Fragment {

    int average_pledged;

    public ChallengeChild1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_child1, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        final TextView totalUsers = view.findViewById(R.id.total_users);
        final TextView avgPledged = view.findViewById(R.id.avg_pledged);
        final TextView totalPledged = view.findViewById(R.id.total_pledged);
        final TextView targetPercent = view.findViewById(R.id.progress_text);
        final ProgressBar progressWheel = view.findViewById(R.id.progressWheel);
        final TextView totalPledged1 = view.findViewById(R.id.total_pledged_1);

        //update total co2
        //use array to save value
        FirebaseMethods.getTotalCo2(new CallBackValue() {
            @Override
            public void onCallback(int value) {

                totalPledged.setText(String.valueOf(value) );
                totalPledged1.setText(String.valueOf(value/1000));
            }
        });

        //update total pledges
        FirebaseMethods.getTotalPledges(new CallBackValue() {
            @Override
            public void onCallback(int value) {
                totalUsers.setText(String.valueOf(value));

                //get average
                int savedTotalCo2 = Integer.valueOf(totalPledged.getText().toString());
                if(getContext() != null){
                    String total_pledged = String.valueOf(savedTotalCo2) + getResources().getString(R.string.kg_unit);
                    totalPledged.setText(total_pledged);
                    int avgUserPledged = savedTotalCo2/ value;
                    String avg_pledged = String.valueOf(avgUserPledged) + getResources().getString(R.string.kg_unit);
                    avgPledged.setText(avg_pledged);
                }
                else{
                    totalPledged.setText("0");
                    avgPledged.setText("0");
                }

                //populate progress wheel
                int goal = 382000000;
                int percent = 100;
                double progressDecimal = (Double.valueOf(savedTotalCo2)/(Double.valueOf(goal)));
                int ourProgress =(int) (progressDecimal * percent);
                String progressString = String.valueOf(ourProgress) + "%";
                targetPercent.setText(progressString);
                Log.d("PROGRESS:",progressString);
                progressWheel.setSecondaryProgress(ourProgress);
            }
        });
    }
}
