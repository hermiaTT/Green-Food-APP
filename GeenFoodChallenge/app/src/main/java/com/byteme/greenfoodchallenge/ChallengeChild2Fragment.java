package com.byteme.greenfoodchallenge;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import java.util.ArrayList;
import pledgeCode.FirebaseMethods;
import pledgeCode.MyCallback;
import pledgeCode.Pledge;


public class ChallengeChild2Fragment extends Fragment {

    private ArrayList<Pledge> pledgeLeaderBoard = new ArrayList<>();
    private ArrayList<Pledge> pledgeLeaderBoardFiltered = new ArrayList<>();
    private TableLayout tableLayout;
    private LayoutInflater inflater;
    private Context context;
    private String municipalitySpinnerValue = "Any"; // before these can be set by the system they are used , so I manually set to prevent crash
    private FloatingActionButton pledgeRefresh;

    public ChallengeChild2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_child2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        tableLayout = view.findViewById(R.id.leader_board);
        inflater = LayoutInflater.from(getContext());
        context = this.getContext();

        // Municipality Dropdown
        final Spinner municipalitySpinner = (Spinner) view.findViewById(R.id.municipality_spinner);
        municipalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                municipalitySpinnerValue = (String) parent.getItemAtPosition(position);
                filterPledges();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.municipalityFilter_array, R.layout.municipality_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municipalitySpinner.setAdapter(adapter);

        pledgeRefresh = view.findViewById(R.id.pledgeRefresh);
        pledgeRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAndDisplayPledges();
            }
        });
        //display
        refreshAndDisplayPledges();


    }
    private void filterPledges(){
        pledgeLeaderBoardFiltered.clear();

        int allPledgeSize = pledgeLeaderBoard.size();
        if(!municipalitySpinnerValue.equals("Any")){
            for(int i = 0; i <  allPledgeSize  ; i++){
                Pledge pledge = pledgeLeaderBoard.get(i);
                String municipality = pledge.getMunicipality();
                if(municipality.equals(municipalitySpinnerValue)){
                    pledgeLeaderBoardFiltered.add(pledge);
                }
            }
        }
        else{
            pledgeLeaderBoardFiltered.addAll(pledgeLeaderBoard);
        }


        tableLayout.removeAllViews();
        int filteredPledgeSize = pledgeLeaderBoardFiltered.size();
        for (int i = 0; i < filteredPledgeSize; i++) {
            View tableRow = inflater.inflate(R.layout.leaderboard, null, false);
            //get name and populate
            TextView pledgeName = tableRow.findViewById(R.id.leader_board_pledgeName);
            pledgeName.setText(pledgeLeaderBoardFiltered.get(i).getName());
            //get Municipality and populate
            TextView pledgeMunicipality = tableRow.findViewById(R.id.leader_board_municipality);
            pledgeMunicipality.setText(pledgeLeaderBoardFiltered.get(i).getMunicipality());
            //get co2 and populate
            TextView pledgeCo2 = tableRow.findViewById(R.id.leader_board_Co2);
            pledgeCo2.setText(String.valueOf(pledgeLeaderBoardFiltered.get(i).getCo2Pledged()));
            tableLayout.addView(tableRow);
        }

    }
    private void refreshAndDisplayPledges() {
        pledgeLeaderBoard.clear();
        tableLayout.removeAllViews();
        // get latest pledges
        FirebaseMethods.getSortedCo2Pledges(new MyCallback<Pledge>() {
            @Override
            public void onCallback(ArrayList<Pledge> pledgeList) {
                //testing if it crashes when it get pledge list
                int size = pledgeList.size();
                pledgeLeaderBoard.addAll(pledgeList);
                filterPledges();
            }
        });
    }
}


