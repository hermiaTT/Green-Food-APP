package com.byteme.greenfoodchallenge;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;


public class CarbonCalculatorActivity extends AppCompatActivity {
    private static final String BACK_STACK_ROOT_TAG = "HOME";
    private Fragment home_Fragment;
    private Fragment challenge_Fragment;
    private Fragment restaurant_Fragment;
    private Fragment profile_Fragment;
    private BottomNavigationView navigation;
    private int joinedChallenge;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbon_calculator);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Get the saved value to determine if user has joined the challenge or not
        SharedPreferences sp = getSharedPreferences("myprefs", Activity.MODE_PRIVATE);
        joinedChallenge = sp.getInt("firstTime", 0);

        Bundle extras = getIntent().getExtras();
        // Check if the extras actually contain anything
        if(extras ==  null){
            home_Fragment = new CarbonCalculatorFragment();

            // load initial fragment
            loadHomeFragment(home_Fragment);
        }
        else {
            int intentFragment = getIntent().getExtras().getInt("fragmentToLoad");
            if(intentFragment==0){
                home_Fragment = new CarbonCalculatorFragment();
                navigation.setSelectedItemId(R.id.navigation_home);
                loadHomeFragment(home_Fragment);
            }
            else if(intentFragment==1){
                challenge_Fragment = new ChallengeFragment();
                navigation.setSelectedItemId(R.id.navigation_challenge);
                loadFragment(challenge_Fragment);
            }
            else if(intentFragment==2){
                restaurant_Fragment = new RestaurantFragment();
                navigation.setSelectedItemId(R.id.navigation_restaurant);
                loadFragment(restaurant_Fragment);
            }
            else if(intentFragment==3){
                profile_Fragment = new ProfileFragment();
                navigation.setSelectedItemId(R.id.navigation_account);
                loadFragment(profile_Fragment);
            }

        }


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    home_Fragment = new CarbonCalculatorFragment();
                    loadFragment(home_Fragment);
                    return true;
                case R.id.navigation_challenge:
                    if(joinedChallenge == 0){
                        home_Fragment = new CarbonCalculatorFragment();
                        // Using back button after going from pledge activity to fragment it will crash because the home page is created without the tag: BACK_STACK_ROOT_TAG , so we create a new one here.
                        loadFragment(home_Fragment);
                        fragment = new JoinChallengeFragment();
                        loadFragment(fragment);
                    }
                    else {
                        home_Fragment = new CarbonCalculatorFragment();
                        loadFragment(home_Fragment);
                        fragment = new ChallengeFragment();
                        loadFragment(fragment);
                    }
                    return true;
                case R.id.navigation_restaurant:
                    home_Fragment = new CarbonCalculatorFragment();
                    loadFragment(home_Fragment);
                    fragment = new RestaurantFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_account:
                    home_Fragment = new CarbonCalculatorFragment();
                    loadFragment(home_Fragment);
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    private void loadHomeFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment)
                .addToBackStack(BACK_STACK_ROOT_TAG);
        transaction.commit();
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        //
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Log.d("TAGNAME",String.valueOf(fragmentManager.getBackStackEntryCount()));

        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // load initial fragment
        loadHomeFragment(home_Fragment);
        navigation.setSelectedItemId(R.id.navigation_home);
    }
}
