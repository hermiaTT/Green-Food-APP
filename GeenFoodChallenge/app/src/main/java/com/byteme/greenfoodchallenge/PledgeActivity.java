package com.byteme.greenfoodchallenge;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import pledgeCode.FirebaseMethods;
import pledgeCode.MyCallback;
import pledgeCode.Pledge;

public class PledgeActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, AdapterView.OnItemSelectedListener {
    Button submit;
    Button cancel;
    EditText pledgeNameTextBox;
    String municipalitySpinnerValue;
    EditText co2PledgedTextBox;
    ImageView avatar_img;
    Drawable avatar_drawable;
    int avatar_number = 0;
    int icon_number;
    boolean hasPledge = false;
    TextView pledgeHelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pledge_);
        pledgeNameTextBox = findViewById(R.id.pledgeName);
        co2PledgedTextBox = findViewById(R.id.co2Pledge);
        avatar_img = findViewById(R.id.avatar);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.inflateMenu(R.menu.toolbar_pledge);
        toolbar.setOnMenuItemClickListener(this);

        FloatingActionButton editIcon = findViewById(R.id.fab);
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar();
            }
        });

        // Pop-up box to aid the user in choosing a pledge amount
        pledgeHelp = findViewById(R.id.pledge_help);
        pledgeHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPledgeHelp();
            }
        });


        // Municipality Dropdown
        final Spinner municipalitySpinner = (Spinner) findViewById(R.id.municipality_spinner);
        municipalitySpinner.setOnItemSelectedListener(this);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.municipality_array, R.layout.municipality_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municipalitySpinner.setAdapter(adapter);

        String uid = FirebaseMethods.getFireBaseCurrentUser().getUid();
        FirebaseMethods.getMyPledge(new MyCallback<Pledge>() {
            @Override
            public void onCallback(ArrayList<Pledge> pledgeList) {
                Pledge myPledge = pledgeList.get(0);// get pledge

                    if (myPledge != null) {
                        hasPledge = true;
                        String name = myPledge.getName();
                        pledgeNameTextBox.setText(name);

                        String municipality = myPledge.getMunicipality();
                        // If municipality has value, then set this value to the spinner
                        if (municipality != null) {
                            int spinnerPosition = adapter.getPosition(municipality);
                            municipalitySpinner.setSelection(spinnerPosition);
                        }

                        int co2Pledged = myPledge.getCo2Pledged();
                        String textCo2Pledged = String.valueOf(co2Pledged); // getString(R.string.kg_unit);
                        co2PledgedTextBox.setText(textCo2Pledged);
                    }
                    else {
                        hasPledge = false;
                    }
            }
        }, uid);


        submit = findViewById(R.id.submitPledge);
        cancel = findViewById(R.id.cancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String name = FirebaseMethods.getFireBaseCurrentUser().getDisplayName();
                String name = pledgeNameTextBox.getText().toString();
                //String municipality = municipalityTextBox.getText().toString();
                String municipality = municipalitySpinnerValue;
                String co2Pledged = co2PledgedTextBox.getText().toString();

                int co2PledgeInteger;

                if(!co2Pledged.equals("")){
                    co2PledgeInteger = Integer.valueOf(co2Pledged);
                }
                else{
                    co2PledgeInteger = 0;
                }

                long unixTime = System.currentTimeMillis();

                if(!name.equals("") && !co2Pledged.equals("")){
                    Pledge pledge = new Pledge(name,municipality,co2PledgeInteger,unixTime);
                    FirebaseMethods.savePledge(pledge);
                }

                // After submitting, go back to the Challenge page
                Intent goBack = new Intent(getApplicationContext(), CarbonCalculatorActivity.class);
                goBack.putExtra("fragmentToLoad", 1); // ChallengeFragment value is 1
                startActivity(goBack);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the Challenge page without saving
                Intent goBack = new Intent(getApplicationContext(), CarbonCalculatorActivity.class);
                goBack.putExtra("fragmentToLoad", 1); // ChallengeFragment value is 1
                startActivity(goBack);
            }
        });


        // Get avatar number from shared preferences
        SharedPreferences sp = getSharedPreferences("myprefs", Activity.MODE_PRIVATE);
        avatar_number = sp.getInt("profileKey", 0);
        if(avatar_number != 0){
            icon_number = setAvatar(avatar_number);
            avatar_img.setImageDrawable(getResources().getDrawable(icon_number));
        }
        else {
            icon_number = setAvatar(0);
            avatar_img.setImageDrawable(getResources().getDrawable(icon_number));
        }

/*        avatar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAvatar();
            }
        });*/
    }


    // Method to change profile avatar
    public void changeAvatar() {

        final AlertDialog avatarSelector = new AlertDialog.Builder(this).create();

        LayoutInflater avatars = LayoutInflater.from(this);
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


    // Use shared preferences to store the number that corresponds to profile icon
    public void saveProfileIcon(int number) {
        SharedPreferences sp = getSharedPreferences("myprefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("profileKey", number);
        editor.commit();
    }

    // Delete button is clicked. If a pledge doesn't exist, let the user know.
    // Otherwise, delete the pledge and take the user back to the Challenge page.
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_delete:
                if(!hasPledge){
                    Toast.makeText(this, "You do not have a pledge to delete", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Delete Pledge");
                    alert.setMessage("Are you sure you want to delete your pledge? Doing so will remove your record from the leaderboard.");
                    alert.setIconAttribute(android.R.attr.alertDialogIcon);

                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String uid = FirebaseMethods.getFireBaseCurrentUser().getUid();
                            FirebaseMethods.getMyPledge(new MyCallback<Pledge>() {
                                @Override
                                public void onCallback(ArrayList<Pledge> pledgeList) {
                                    Pledge p = pledgeList.get(0);
                                    FirebaseMethods.deleteMyPledge(p);
                                    Intent goBack = new Intent(getApplicationContext(), CarbonCalculatorActivity.class);
                                    goBack.putExtra("fragmentToLoad", 1); // ChallengeFragment value is 1
                                    startActivity(goBack);
                                }
                            }, uid);

                        }});
                    alert.setNegativeButton(android.R.string.no, null).show();
                }
                return true;
        }
        return false;
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

    // Municipality spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        municipalitySpinnerValue = (String) parent.getItemAtPosition(position);

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }


    public void getPledgeHelp(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Pledge Amount");
        alert.setMessage("We recommend pledging around 10% of the CO2e from your current diet. Ambitious challengers may go for 20%. Go to the CO2e Calculator to find out your current diet's CO2e.");
        alert.setNegativeButton(android.R.string.ok, null).show();
    }
}
