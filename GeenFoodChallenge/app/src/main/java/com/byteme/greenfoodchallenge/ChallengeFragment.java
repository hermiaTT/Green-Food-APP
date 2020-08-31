package com.byteme.greenfoodchallenge;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import pledgeCode.FirebaseMethods;
import pledgeCode.MyCallback;
import pledgeCode.Pledge;


public class ChallengeFragment extends Fragment  {

    int avatar_number;
    int icon_number;


    public ChallengeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_challenge, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ViewPager viewPager = getView().findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = getView().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ImageView menuIcon = view.findViewById(R.id.menu_icon);
        final RelativeLayout pledgeCard = view.findViewById(R.id.pledge_card);
        final TextView createPledgeCover = view.findViewById(R.id.create_pledge_cover);
        final TextView pledge = view.findViewById(R.id.pledge_value);
        final TextView myName = view.findViewById(R.id.pledge_name);
        final TextView myMuni = view.findViewById(R.id.pledge_municipality);

        // Get profile icon number from sharedpreferences
        SharedPreferences sp = this.getActivity().getSharedPreferences("myprefs", Activity.MODE_PRIVATE);
        avatar_number = sp.getInt("profileKey", 0);
        ImageView pledge_icon = view.findViewById(R.id.pledge_icon);
        final Drawable myDrawable = getResources().getDrawable(R.drawable.avatar_default);
        pledge_icon.setImageDrawable(myDrawable);

        // Set profile icon
        icon_number = setAvatar(avatar_number);
        pledge_icon.setImageDrawable(getResources().getDrawable(icon_number));

        // Load pledge card
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        String uid = FirebaseMethods.getFireBaseCurrentUser().getUid();
        FirebaseMethods.getMyPledge(new MyCallback<Pledge>() {
            @Override
            public void onCallback(ArrayList<Pledge> pledgeList) {
                Pledge myPledge = pledgeList.get(0);// get pledge
                Activity activity = getActivity();
                if (activity != null) {

                    if (myPledge != null) {
                        createPledgeCover.setVisibility(View.GONE);
                        pledgeCard.setVisibility(View.VISIBLE);

                        String name = myPledge.getName();
                        myName.setText(name);

                        String municipality = myPledge.getMunicipality();
                        myMuni.setText(municipality);

                        int co2Pledged = myPledge.getCo2Pledged();
                        String textCo2Pledged = String.valueOf(co2Pledged) + getString(R.string.kg_unit);
                        pledge.setText(textCo2Pledged);
                    }
                    else {
                        createPledgeCover.setVisibility(View.VISIBLE);
                        pledgeCard.setVisibility(View.GONE);
                    }
                }
            }
        }, uid);

        // Create pledge card
        createPledgeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToPledge = new Intent(getActivity(), PledgeActivity.class);
                startActivity(goToPledge);
            }
        });

        // Open Menu
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.inflate(R.menu.toolbar_challenge);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                Intent goToPledge = new Intent(getActivity(), PledgeActivity.class);
                                startActivity(goToPledge);
                                return true;
                            case R.id.action_share:
                                // Share challenge to social media
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Join the Green Food Challenge!");
                                String shareMessage = "Join me on this challenge and see if you can beat my pledge!";
                                intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                startActivity(Intent.createChooser(intent, "Share via"));
                                return true;
                        }
                        return false;
                    }
                });
            }});
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ChallengeChild1Fragment(), "Community");
        adapter.addFragment(new ChallengeChild2Fragment(), "Leaderboard");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    // Retrieve the drawable with a number
    public int setAvatar(int icon_number) {
        int[] icon = {
                R.drawable.avatar_default,
                R.drawable.avatar_dog,
                R.drawable.avatar_fox,
                R.drawable.avatar_lion,
                R.drawable.avatar_rabbit,
                R.drawable.avatar_koala,
                R.drawable.avatar_tiger
        };
        return icon[icon_number];
    }


}
