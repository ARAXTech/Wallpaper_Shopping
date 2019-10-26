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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import About.AboutUs;
import Recycler.RecyclerActivity;
import Recycler.Search;

public class MainActivity extends AppCompatActivity {

    GridView simpleGrid;
    GridAdapter gridAdapter;
    private AuthHelper mAuthHelper;
    private Menu mOptionsMenu;
    private Button profileBtn;
    //GridView images
    int logos[] = {
            R.drawable.logo1, R.drawable.logo12, R.drawable.logo3, R.drawable.logo6,
            R.drawable.logo8,R.drawable.logo5};
   //Gridview Text
    String txt[]={
            "سالن پذیرایی",
            "اتاق کودک",
            "پشت Tv",
            "اتاق خواب",
            "سه بعدی",
            "هنری",
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Splash Screen
        if (Splashscreen.Splash==0){
            Intent intent = new Intent(this,Splashscreen.class);
            startActivity(intent);
            super.onCreate(savedInstanceState);
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         UIElement cls = new UIElement(MainActivity.this,this);
         cls.FontMethod();
        //add back button in toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        //Profile
        profileBtn=(Button) findViewById(R.id.ProfileBtn);
        mAuthHelper = AuthHelper.getInstance(this);

        profileBtn = (Button) findViewById(R.id.ProfileBtn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        });

        //updateOptionsMenu();
        if (mAuthHelper.isLoggedIn()) {
            Log.d("USERNAME: ", "isloggedin");
            profileBtn.setVisibility(View.GONE);
            // setupView();
        } else {

            //  finish();
        }

        //Navigation
         UIElement cls1 = new UIElement(MainActivity.this,this);
         cls1.NavigationMethod();


        // init GridView
        simpleGrid = (GridView) findViewById(R.id.simpleGridView);
        // Create an object of GridAdapter and set Adapter to GirdView
        GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), logos,txt,false);
        simpleGrid.setAdapter(gridAdapter);

        // implement setOnItemClickListener event on GridView

        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //salon paziraee
                        Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
                        intent.putExtra("key", "18"); // put image data in Intent
                        //intent.putExtra("count", "275"); // put number of image data in Intent
                        intent.putExtra("count", "15");
                        startActivity(intent); // start Intent
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case 1:
                        //otagh koodak
                        //salon paziraee
                        Intent intent1 = new Intent(MainActivity.this, RecyclerActivity.class);
                        intent1.putExtra("key", "111"); // put image data in Intent
                        //intent.putExtra("count", "275"); // put number of image data in Intent
                        intent1.putExtra("count", "5");
                        startActivity(intent1); // start Intent
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;

                    case 2:
                        //posht e TV
                        Intent intent2 = new Intent(MainActivity.this, RecyclerActivity.class);
                        intent2.putExtra("key", "20"); // put image data in Intent
                        //intent2.putExtra("count", "99"); // put number of image data in Intent
                        intent2.putExtra("count", "5");
                        startActivity(intent2); // start Intent
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case 3:
                        //otagh khab
                        Intent intent3 = new Intent(MainActivity.this, RecyclerActivity.class);
                        intent3.putExtra("key", "112"); // put image data in Intent
                        //intent.putExtra("count", "275"); // put number of image data in Intent
                        intent3.putExtra("count", "5");
                        startActivity(intent3); // start Intent
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;

                    case 4:
                        //3 boodi
                        Intent intent4 = new Intent(MainActivity.this, RecyclerActivity.class);
                        intent4.putExtra("key", "113"); // put image data in Intent
                        //intent.putExtra("count", "275"); // put number of image data in Intent
                        intent4.putExtra("count", "5");
                        startActivity(intent4); // start Intent
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case 5:
                        //honari
                        Intent intent5 = new Intent(MainActivity.this, RecyclerActivity.class);
                        intent5.putExtra("key", "21"); // put image data in Intent
                        //intent.putExtra("count", "275"); // put number of image data in Intent
                        intent5.putExtra("count", "5");
                        startActivity(intent5); // start Intent
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;

                }
            }
        });

    }

    private void setupView() {
        //Log.d("USERNAME - setupView: ", mAuthHelper.getUsername());
        setWelcomeText(mAuthHelper.getUsername());
    }
    private void setWelcomeText(String username) {
        // Log.d("USERNAME: ", username);
        //  mTextWelcome.setText(String.format(getString(R.string.text_welcome), username));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_signout){
            mAuthHelper.clear();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            // profileBtn.setVisibility(View.VISIBLE );
            //finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.signout_menu, menu);
        mOptionsMenu = menu;
        return super.onCreateOptionsMenu(mOptionsMenu);
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.action_signout);
        //register.setVisible(false);
        if(mAuthHelper.isLoggedIn())
        {
            register.setVisible(true);
        }
        else
        {
            register.setVisible(false);
        }
        //invalidateOptionsMenu();
        return true;
    }
    private void updateOptionsMenu() {
        if (mOptionsMenu != null) {
            onPrepareOptionsMenu(mOptionsMenu);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        updateOptionsMenu();
        super.onConfigurationChanged(newConfig);
    }


}
