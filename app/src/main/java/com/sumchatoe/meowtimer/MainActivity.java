package com.sumchatoe.meowtimer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    int quantity = 1;
    private NumberPicker numberPicker1, numberPicker2;
    int min, sec;
    long roundMills;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numberPicker1 = findViewById(R.id.np1);
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(59);
        numberPicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker1, int i, int i1) {
                min = numberPicker1.getValue() * 60000;
            }
        });
        numberPicker1.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        numberPicker2 = findViewById(R.id.np2);
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(59);
        numberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker2, int i, int i1) {
                sec = numberPicker2.getValue() * 1000;
            }
        });
        numberPicker2.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
    }

    //displays the given quantity value on the screen.
    private void display(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    //This method is called when the increment button is clicked.
    public void increment(View view) {
        quantity += 1;
        display(quantity);
    }

    //This method is called when the decrement button is clicked.
    public void decrement(View view) {
        if (quantity > 1) quantity--;
        display(quantity);
    }

    //This method will be called when ImageButton is clicked.
    public void onClickStart (View v){
        //try to catch data from numberpicker
        roundMills = min + sec;
        // Call second activity
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        Bundle b = new Bundle();
        //Storing data into bundle
        b.putLong("roundmills", roundMills);
        b.putInt("quantity", quantity);
        //send data to Main2Activity
        intent.putExtra("b", b);
        startActivity(intent);
    }
}
