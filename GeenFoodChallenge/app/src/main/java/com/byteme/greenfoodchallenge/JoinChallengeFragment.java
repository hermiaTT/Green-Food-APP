package com.byteme.greenfoodchallenge;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class JoinChallengeFragment extends Fragment {
    int joinedChallenge;

    public JoinChallengeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_join_challenge, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Button joinButton = view.findViewById(R.id.join_button);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinedChallenge = 1;
                SharedPreferences sp = getContext().getSharedPreferences("myprefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("firstTime", joinedChallenge);
                editor.commit();

                Intent intent = new Intent(getActivity(), ChallengeIntroActivity.class);
                startActivity(intent);
            }
        });

    }


}
