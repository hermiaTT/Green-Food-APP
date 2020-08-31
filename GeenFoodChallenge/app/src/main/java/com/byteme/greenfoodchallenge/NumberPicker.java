package com.byteme.greenfoodchallenge;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class NumberPicker extends LinearLayout {

    int selectedValue = 0;
    TextView currentValue;
    TextView foodName;

    public NumberPicker(Context context) {
        super(context);
        initializeViews(context);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    // Set the value of the edit box
    public void setValue(int value){
        selectedValue = value;
        currentValue.setText(String.valueOf(value));
    }

    // Return the value of the number picker
    public int getValue(){
        return selectedValue;
    }

    //set the name of the food
    public void setFoodName(String name){
        foodName.setText(name);
    }

    // Inflate the view in the layout
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.number_picker, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Button incrementButton = this.findViewById(R.id.increment);
        Button decrementButton = this.findViewById(R.id.decrement);
        currentValue = this.findViewById(R.id.serving);
        foodName = this.findViewById(R.id.food_item);

        incrementButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                selectedValue += 1;
                currentValue.setText(String.valueOf(selectedValue));
            }
        });

        decrementButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if(selectedValue > 0){
                    selectedValue -= 1;
                }
                currentValue.setText(String.valueOf(selectedValue));
            }
        });
    }
}
