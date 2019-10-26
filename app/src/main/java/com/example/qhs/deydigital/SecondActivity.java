package com.example.qhs.deydigital;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import About.AboutUs;
import Recycler.RecyclerActivity;
import Recycler.Search;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UIElement cls = new UIElement(SecondActivity.this,this);
        cls.FontMethod();
        //add back button in toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        //Navigation
        UIElement cls1 = new UIElement(SecondActivity.this,this);
        cls1.NavigationMethod();

        ImageView image = (ImageView) findViewById(R.id.icon); // init a ImageView
        Intent intent = getIntent(); // get Intent which we set from Previous Activity
        final GridView simpleList = (GridView) findViewById(R.id.secondActivityGridView);

        String[] myStrings = intent.getStringArrayExtra("name");//name for sublist
        String[] imgStrings = intent.getStringArrayExtra("image"); //image for sublist
        final String[] idStrings = intent.getStringArrayExtra("id");
        final String[] countStrings = intent.getStringArrayExtra("count");
        final GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), myStrings,imgStrings,true);
        simpleList.setAdapter(gridAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView <?> adapterView, View view, int position, long l) {
                TextView textView = (TextView) view.findViewById(R.id.textView);
                //  String text = simpleList.getItemAtPosition(position).toString();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), RecyclerActivity.class);
                //  intent.putExtra("child", selectedFromList);
//                Log.d("child",textView.getText().toString());
                // Or / And
                intent.putExtra("key", idStrings[position]);
                intent.putExtra("count", countStrings[position]);
                Log.d("keyChild",idStrings[position]);
                startActivity(intent);
                //add animation when clicked
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }


        });
    }

}
