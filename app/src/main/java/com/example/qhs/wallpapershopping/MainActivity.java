package com.example.qhs.wallpapershopping;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.qhs.wallpapershopping.Fragments.Dialog;
import com.example.qhs.wallpapershopping.Fragments.Fragment_Shopping;
import com.example.qhs.wallpapershopping.Fragments.Fragment_about;
import com.example.qhs.wallpapershopping.Fragments.Fragment_favorite;
import com.example.qhs.wallpapershopping.Fragments.Fragment_home;
import com.example.qhs.wallpapershopping.Fragments.Fragment_login;
import com.example.qhs.wallpapershopping.Fragments.Fragment_search;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import android.os.Bundle;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity<navigation> extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    //Decleration
    private AuthHelper mAuthHelper;
    private Menu mOptionsMenu;
    private ImageButton profileBtn;
    private TextView txtView_login;
    private TextView txtView_signUp;
    private Toolbar toolbar;
    private BottomNavigationView navigation;
    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        //Splash Screen
        if (Splashscreen.Splash == 0){
            Intent intent = new Intent(this,Splashscreen.class);
            startActivity(intent);
            super.onCreate(savedInstanceState);
            finish();
            return;
        }
//        //End Splash

//        Window window = getWindow();
//        WindowManager.LayoutParams winParams = window.getAttributes();
//        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        window.setAttributes(winParams);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);



        //Full screen ui
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                                  WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        //End Full screen ui
        //Hide navigation bar
        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions =  View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        //End Hide navigation bar
        super.onCreate(savedInstanceState);
        //animation
        //setAnimation();
        setContentView(R.layout.activity_main);

        fragment = new Fragment_home();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame, fragment).commit();


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //toolbar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
     //Toolbar
       // toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);





        //Profile
        mAuthHelper = AuthHelper.getInstance(this);

        profileBtn = (ImageButton) findViewById(R.id.ProfileBtn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new Fragment_login();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment).commit();
            }
        });

//        //updateOptionsMenu();
        if (mAuthHelper.isLoggedIn()) {
            Log.d("USERNAME: ", "isloggedin");
            profileBtn.setVisibility(View.GONE);
        }
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

    }

    @SuppressLint("Range")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.menu_shopping_cart:

                if (mAuthHelper.isLoggedIn()) {
                    fragment = new Fragment_Shopping();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame, fragment).commit();;
                } else {
                    Blur blur=new Blur();
                    Bitmap map = blur.takeScreenShot(this);
                    Bitmap fast = blur.fastblur(map, 10);
                    fragment = new Dialog();
                    ConstraintLayout constraintLayout=(ConstraintLayout)findViewById(R.id.constraintLayout);
                    BitmapDrawable ob = new BitmapDrawable(getResources(), fast);
                    constraintLayout.setBackground(ob);
                    getSupportFragmentManager().beginTransaction().addToBackStack("tag")
                            .replace(R.id.frame, fragment).commit();
                }
                break;
            case R.id.menu_search:

                fragment = new Fragment_search();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment).commit();
                break;

            case R.id.menu_home:
                fragment = new Fragment_home();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame, fragment).commit();
                break;

            case R.id.menu_favorite:
                if (mAuthHelper.isLoggedIn()) {
                    fragment = new Fragment_favorite();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame, fragment).commit();
                } else {
                    Blur blur=new Blur();
                    Bitmap map = blur.takeScreenShot(this);
                    Bitmap fast = blur.fastblur(map, 10);
                    fragment = new Dialog();
                    ConstraintLayout constraintLayout=(ConstraintLayout)findViewById(R.id.constraintLayout);
                    BitmapDrawable ob = new BitmapDrawable(getResources(), fast);
                    constraintLayout.setBackground(ob);
                    getSupportFragmentManager().beginTransaction().addToBackStack("tag")
                            .replace(R.id.frame, fragment).commit();
                }
                break;

            case R.id.menu_call:
                fragment = new Fragment_about();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment).commit();
                break;

        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_signout){
            mAuthHelper.clear();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            profileBtn.setVisibility(View.VISIBLE );
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