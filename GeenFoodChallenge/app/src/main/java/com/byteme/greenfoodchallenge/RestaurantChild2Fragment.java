package com.byteme.greenfoodchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import pledgeCode.FirebaseMethods;
import pledgeCode.MyCallback;


public class RestaurantChild2Fragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<Restaurant> restaurantList;
    RestaurantAdapter myAdapter;

    //Buttons
    private FloatingActionButton restaurantRefresh;

    //filter
    public String municipalitySpinnerValue = "Any";
    public String proteinSpinnerValue = "Any";

    public RestaurantChild2Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant_child2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mRecyclerView = view.findViewById(R.id.recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        //initialize view and buttons
        restaurantList = new ArrayList<>();
        refreshAndDisplayRestaurants();

        restaurantRefresh = view.findViewById(R.id.restaurantRefresh);
        restaurantRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAndDisplayRestaurants();
            }
        });


        // Municipality Dropdown
        final Spinner municipalitySpinner = (Spinner) view.findViewById(R.id.municipality_spinner);
        municipalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                municipalitySpinnerValue = (String) parent.getItemAtPosition(position);
                if(myAdapter != null){
                    filterRestaurants();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.municipalityFilter_array, R.layout.municipality_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municipalitySpinner.setAdapter(adapter);

        // protein dropdown
        final Spinner proteinSpinner = (Spinner) view.findViewById(R.id.protein_spinner);
        proteinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                proteinSpinnerValue = (String) parent.getItemAtPosition(position);
                if(myAdapter != null){
                    filterRestaurants();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final ArrayAdapter<CharSequence> proteinAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.proteinFilter_array, R.layout.municipality_spinner);
        proteinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        proteinSpinner.setAdapter(proteinAdapter);
    }

    private void filterRestaurants(){
        ArrayList<Restaurant> filteredRestaurants = new ArrayList<>();
        for(int i = 0; i< restaurantList.size() ; i++) {
            Restaurant restaurant = restaurantList.get(i);
            String protein =  restaurant.getProtein();
            String municipality =  restaurant.getLocation();

            boolean sameMunicipality = municipality.equals(municipalitySpinnerValue);
            boolean sameProtein = protein.equals(proteinSpinnerValue);
            boolean anyProtein = proteinSpinnerValue.equals("Any");
            boolean anyMunicipality = municipalitySpinnerValue.equals("Any");
            if ( anyProtein && anyMunicipality ){
                filteredRestaurants.add(restaurant);
            }
            else if (anyProtein && sameMunicipality ){
                filteredRestaurants.add(restaurant);
            }
            else if (sameProtein && anyMunicipality){
                filteredRestaurants.add(restaurant);
            }
            else if(sameMunicipality && sameProtein){
                filteredRestaurants.add(restaurant);
            }
        }
        myAdapter.setFilteredList(filteredRestaurants);
        myAdapter.notifyDataSetChanged();
    }
    private void refreshAndDisplayRestaurants(){
        restaurantList.clear();
        FirebaseMethods.getAllRestaurant(new MyCallback<Restaurant>() {
            @Override
            public void onCallback(ArrayList<Restaurant> list) {
                restaurantList.addAll(list);
                myAdapter = new RestaurantAdapter(getActivity(), restaurantList);
                mRecyclerView.setAdapter(myAdapter);
                if(!restaurantList.isEmpty()){
                    filterRestaurants();
                }
            }
        });
    }
}
