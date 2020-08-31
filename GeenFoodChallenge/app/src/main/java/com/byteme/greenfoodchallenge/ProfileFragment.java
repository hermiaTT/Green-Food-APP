package com.byteme.greenfoodchallenge;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import pledgeCode.FirebaseMethods;
import pledgeCode.MyCallback;
import pledgeCode.Pledge;


public class ProfileFragment extends Fragment {

    ImageView avatar_img;
    Drawable avatar_drawable;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    int avatar_number=2;
    int icon_number;
    int joinedChallenge;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Get profile icon number from sharedpreferences
        SharedPreferences sp = this.getActivity().getSharedPreferences("myprefs", Activity.MODE_PRIVATE);
        avatar_number = sp.getInt("profileKey", 0);

        // Get the saved value to determine if user has joined the challenge or not
        joinedChallenge = sp.getInt("firstTime", 0);

        FloatingActionButton editIcon = view.findViewById(R.id.fab);
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar();
            }
        });

        Button signOutButton = getView().findViewById(R.id.sign_out_button);
        Button quitChallengeButton = getView().findViewById(R.id.quit_challenge_button);
        if(joinedChallenge==0){
            quitChallengeButton.setVisibility(View.GONE);
        }

        // Set profile icon
        avatar_img = getView().findViewById(R.id.avatar);
        icon_number = setAvatar(avatar_number);
        avatar_img.setImageDrawable(getResources().getDrawable(icon_number));


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Sign Out");
                alert.setMessage("Are you sure you want to sign out?");
                alert.setIconAttribute(android.R.attr.alertDialogIcon);

                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mAuth.signOut();
                        Intent goToAuth = new Intent(getActivity(), AuthenticateActivity.class);
                        startActivity(goToAuth);
                    }});
                alert.setNegativeButton(android.R.string.no, null).show();
            }
        });

        quitChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Quit Challenge");
                alert.setMessage("Are you sure you want to quit the challenge? Your pledge will be deleted and you will no longer see the community stats. You can rejoin at any time.");
                alert.setIconAttribute(android.R.attr.alertDialogIcon);

                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Delete user's pledge if they quit the challenge
                        String uid = FirebaseMethods.getFireBaseCurrentUser().getUid();
                        FirebaseMethods.getMyPledge(new MyCallback<Pledge>() {
                            @Override
                            public void onCallback(ArrayList<Pledge> pledgeList) {
                                Pledge p = pledgeList.get(0);
                                FirebaseMethods.deleteMyPledge(p);
                            }
                        }, uid);

                        // Reset joinedChallenge flag to 0 and save this value
                        joinedChallenge = 0;
                        SharedPreferences sp = getContext().getSharedPreferences("myprefs", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("firstTime", joinedChallenge);
                        editor.commit();

                        // Go back to home page
                        Intent goToHome = new Intent(getActivity(),CarbonCalculatorActivity.class);
                        startActivity(goToHome);
                    }});
                alert.setNegativeButton(android.R.string.no, null).show();
            }
        });

        TextView name = getView().findViewById(R.id.name);
        name.setText(mAuth.getCurrentUser().getDisplayName());
        TextView email = getView().findViewById(R.id.email);
        email.setText(mAuth.getCurrentUser().getEmail());
    }

    // Use shared preferences to store the number that corresponds to profile icon
    public void saveProfileIcon(int number) {
        SharedPreferences sp = getActivity().getSharedPreferences("myprefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("profileKey", number);
        editor.commit();
    }

    // Method to change profile avatar
    public void changeAvatar() {

        final AlertDialog avatarSelector = new AlertDialog.Builder(getContext()).create();

        LayoutInflater avatars = LayoutInflater.from(getContext());
        final View avatarView = avatars.inflate(R.layout.avatar_alertdialog, null);
        avatarSelector.setView(avatarView);
        avatarSelector.setTitle("Select a Profile Icon");

        final ImageView avatar1 =  avatarView.findViewById(R.id.avatar1);
        final Drawable avartar_dog = getResources().getDrawable(R.drawable.avatar_dog);
        avatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatar_drawable = avartar_dog;
                saveProfileIcon(1);
                avatar_img.setImageDrawable(avatar_drawable);
                avatarSelector.dismiss();
            }
        });

        final ImageView avatar2 =  avatarView.findViewById(R.id.avatar2);
        final Drawable avartar_fox = getResources().getDrawable(R.drawable.avatar_fox);
        avatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatar_drawable = avartar_fox;
                saveProfileIcon(2);
                avatar_img.setImageDrawable(avatar_drawable);
                avatarSelector.dismiss();
            }
        });

        final ImageView avatar3 =  avatarView.findViewById(R.id.avatar3);
        final Drawable avartar_lion = getResources().getDrawable(R.drawable.avatar_lion);
        avatar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatar_drawable = avartar_lion;
                saveProfileIcon(3);
                avatar_img.setImageDrawable(avatar_drawable);
                avatarSelector.dismiss();
            }
        });

        final ImageView avatar4 =  avatarView.findViewById(R.id.avatar4);
        final Drawable avartar_rabbit = getResources().getDrawable(R.drawable.avatar_rabbit);
        avatar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatar_drawable = avartar_rabbit;
                saveProfileIcon(4);
                avatar_img.setImageDrawable(avatar_drawable);
                avatarSelector.dismiss();
            }
        });

        final ImageView avatar5 =  avatarView.findViewById(R.id.avatar5);
        final Drawable avartar_koala = getResources().getDrawable(R.drawable.avatar_koala);
        avatar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatar_drawable = avartar_koala;
                saveProfileIcon(5);
                avatar_img.setImageDrawable(avatar_drawable);
                avatarSelector.dismiss();
            }
        });

        final ImageView avatar6 =  avatarView.findViewById(R.id.avatar6);
        final Drawable avartar_tiger = getResources().getDrawable(R.drawable.avatar_tiger);
        avatar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatar_drawable = avartar_tiger;
                saveProfileIcon(6);
                avatar_img.setImageDrawable(avatar_drawable);
                avatarSelector.dismiss();
            }
        });

        final TextView cancel =  avatarView.findViewById(R.id.cancel_text);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarSelector.dismiss();
            }
        });
        avatarSelector.show();
    }


    // Retrieve the drawable with a number
    public int setAvatar(int icon_number){
        int[] icon= {
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
