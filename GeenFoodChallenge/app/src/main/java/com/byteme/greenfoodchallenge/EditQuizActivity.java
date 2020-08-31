package com.byteme.greenfoodchallenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import CalculatorCode.Calculator;

public class EditQuizActivity extends AppCompatActivity {

    ListView listView ;
    ArrayList<String> keys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        final SharedPreferences mPrefs = getSharedPreferences("CalculatorData",MODE_PRIVATE);


        keys = new ArrayList<>();
        getAllSavedQuizzes(mPrefs);
        int offset = 1;
        final ArrayList<String> listNames = new ArrayList<>();
        for (int i = 0 ; i < keys.size(); i++){
            int number = i+offset;

            String timestamp = convertToDate(keys.get(i));
            listNames.add("Diet #" + number + ": " + timestamp );
        }
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listNames);


        // Assign adapter to ListView
        listView.setAdapter(adapter);
        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index

                Intent goToQuiz = new Intent(getApplicationContext(),QuestionActivity.class);
                goToQuiz.putExtra("calculatorId",keys.get(position));
                startActivity(goToQuiz);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.remove(keys.get(position)).apply();
                keys.remove(position);
                listNames.remove(position);

                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }
    //gets all saved quizzes in shared preferences
 private void getAllSavedQuizzes(SharedPreferences mPrefs) {

     Map<String, ?> allQuizzes= mPrefs.getAll();
     for (Map.Entry<String, ?> entry : allQuizzes.entrySet()) { // for each value in the map
         keys.add(entry.getKey());
     }
 }

    // Convert date to specified format
    public String convertToDate(String timeStamp){
        SimpleDateFormat fmt = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Date date = null;
        String resultDate = null;
        try {
            date = fmt.parse(timeStamp);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM dd yyyy");
            resultDate = fmtOut.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultDate;
    }
}
