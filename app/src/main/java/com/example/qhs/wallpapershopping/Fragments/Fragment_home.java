package com.example.qhs.wallpapershopping.Fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qhs.wallpapershopping.AuthHelper;
import com.example.qhs.wallpapershopping.GridAdapter;
import com.example.qhs.wallpapershopping.MainActivity;
import com.example.qhs.wallpapershopping.R;
import com.example.qhs.wallpapershopping.StaggeredRecyclerAdapter;
import com.example.qhs.wallpapershopping.UIElement;
import com.example.qhs.wallpapershopping.network.Admin;

import Recycler.Fragment_recycler;

//import com.example.qhs.deydigital.com.example.qhs.wallpapershopping.R;


public class Fragment_home extends Fragment {
    //Decleration
   // GridView simpleGrid;
    //GridAdapter gridAdapter;
    private RecyclerView staggeredRv;
    private StaggeredRecyclerAdapter adapter;
    private StaggeredGridLayoutManager manager;
    private AuthHelper mAuthHelper;
    private Admin admin;
    private Menu mOptionsMenu;
    private ImageButton profileBtn;
    private TextView txtView_login;
    private TextView txtView_signUp;
    ImageView bgApp, clover;
    Animation bgAnim, cloverAnim;
    LinearLayout textSplash, textHome;
     Animation frombottom;
    private Toolbar toolbar;

    //CurvedBottomNavigationView curvedBottomNavigationView;

    //GridView images
    int logos[] = {
            R.drawable.logo1,
            R.drawable.logo12,
            R.drawable.logo3,
            R.drawable.logo6,
            R.drawable.logo8,
            R.drawable.logo5};
    int[] vector = {
            R.drawable.livingroom,
            R.drawable.kids,
            R.drawable.tvroom,
            R.drawable.bedroom,
            R.drawable.threed,
            R.drawable.artistic};

    //Gridview Text
    String[] txt ={
            "سالن پذیرایی",
            "اتاق کودک",
            "پشت Tv",
            "اتاق خواب",
            "سه بعدی",
            "هنری",
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        admin = Admin.getInstance(getContext());

//        //Splash Screen
//        if (Splashscreen.Splash==0){
//            Intent intent = new Intent(this,Splashscreen.class);
//            startActivity(intent);
//            super.onCreate(savedInstanceState);
//            finish();
//            return;
//        }
//        //End Splash

        //  super.onCreate(savedInstanceState);
        //animation
        //setAnimation();
        frombottom = AnimationUtils.loadAnimation(getContext(), R.anim.frombottom);
//
//        textSplash = view.findViewById(R.id.textsplash);
//        textHome = view.findViewById(R.id.texthome);
//
//        bgApp = view.findViewById(R.id.bgapp);
//        clover = view.findViewById(R.id.clover);

        // init GridView
  //      simpleGrid = (GridView) view.findViewById(R.id.simpleGridView);

//        bgAnim = AnimationUtils.loadAnimation(this, R.anim.bganim);
//        cloverAnim = AnimationUtils.loadAnimation(this, R.anim.clovernim);
        //bgApp.startAnimation(bgAnim);
//        bgApp.animate().translationY(-1900).setDuration(2000).setStartDelay(300);
//        clover.animate().alpha(0).setDuration(800).setStartDelay(600);
//        textSplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(300);
//        textHome.startAnimation(frombottom);

  //      simpleGrid.startAnimation(frombottom);

        //Toolbar

        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
        title.setText("خانه");

        toolbar.setNavigationIcon(null);


        //comment here

        UIElement cls = new UIElement(getContext(), getActivity());

        mAuthHelper = AuthHelper.getInstance(getContext());


        staggeredRv = (RecyclerView) view.findViewById(R.id.staggeredRv);
        //staggeredRv.setHasFixedSize(true);
        manager= new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredRv.setLayoutManager(manager);

        adapter = new StaggeredRecyclerAdapter(getContext(), logos,txt,false);
        staggeredRv.setAdapter(adapter);
//        txtView_login = view.findViewById(R.id.nameuser);
//        txtView_signUp= view.findViewById(R.id.walletuser);

        //updateOptionsMenu();
//        if (mAuthHelper.isLoggedIn()) {
//            Log.d("USERNAME: ", "isloggedin");
////            txtView_login.setText("Hello");
////            txtView_signUp.setText(mAuthHelper.getUsername());
//            // setupView();
//        }
//        txtView_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frame, new Fragment_login());
//                fragmentTransaction.commit();
//            }
//        });

        // Create an object of GridAdapter and set Adapter to GirdView
      //  GridAdapter gridAdapter = new GridAdapter(getContext(), vector,txt,false);
        //simpleGrid.setAdapter(gridAdapter);

        // implement setOnItemClickListener event on GridView

//        staggeredRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Bundle bundle = new Bundle();
//                Fragment fragment;
//                FragmentManager fragmentManager;
//                FragmentTransaction fragmentTransaction;
//                switch (position){
//                    case 0:
//                        //salon paziraee
//
//                        bundle.putString("key", "18");
//                        bundle.putString("count", "15");
//                        bundle.putString("name","سالن پذیرایی");
////                        Intent intent = new Intent(getContext(), Fragment_recycler.class);
////                        intent.putExtra("key", "18"); // put image data in Intent
////                        //intent.putExtra("count", "275"); // put number of image data in Intent
////                        intent.putExtra("count", "15");
//                        //animation
//                        if(Build.VERSION.SDK_INT>20){
////                            ActivityOptions options =
////                                    ActivityOptions.makeSceneTransitionAnimation(getActivity());
////                            startActivity(intent,options.toBundle());
//
//                            fragment = new Fragment_recycler();
//                            fragment.setArguments(bundle);
//                            fragmentManager = getFragmentManager();
//                            fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.frame, fragment);
//                           fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }else {
//
//                            //startActivity(intent);
//                            fragment = new Fragment_recycler();
//                            fragment.setArguments(bundle);
//                            fragmentManager = getFragmentManager();
//                            fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.frame, fragment);
//                             fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
//                        //End animation
//                        //the below line commented because of animation
//                        //startActivity(intent); // start Intent
//                        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        break;
//                    case 1:
//                        //otagh koodak
//                        bundle.putString("key", "111");// put image data in Intent
//                        bundle.putString ("count", "5");
//                        bundle.putString("name","اتاق کودک");
//                        //intent.putExtra("count", "275"); // put number of image data in Intent
//                        fragment = new Fragment_recycler();
//                        fragment.setArguments(bundle);
//                        fragmentManager = getFragmentManager();
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.frame, fragment);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
//                        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        break;
//
//                    case 2:
//                        //posht e TV
//                        bundle.putString("key", "20");// put image data in Intent
//                        bundle.putString ("count", "5");
//                        bundle.putString("name","پشت TV");
//                        //intent.putExtra("count", "275"); // put number of image data in Intent
//                        fragment = new Fragment_recycler();
//                        fragment.setArguments(bundle);
//                        fragmentManager = getFragmentManager();
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.frame, fragment);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
//                        break;
//                    case 3:
//                        //otagh khab
//                        bundle.putString("key", "112");// put image data in Intent
//                        bundle.putString ("count", "5");
//                        bundle.putString("name","اتاق خواب");
//                        //intent.putExtra("count", "275"); // put number of image data in Intent
//                        fragment = new Fragment_recycler();
//                        fragment.setArguments(bundle);
//                        fragmentManager = getFragmentManager();
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.frame, fragment);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
//                        break;
//
//                    case 4:
//                        //3 boodi
//                        bundle.putString("key", "113");// put image data in Intent
//                        bundle.putString ("count", "5");
//                        bundle.putString("name","سه بعدی");
//                        //intent.putExtra("count", "275"); // put number of image data in Intent
//                        fragment = new Fragment_recycler();
//                        fragment.setArguments(bundle);
//                        fragmentManager = getFragmentManager();
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.frame, fragment);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
//                        break;
//                    case 5:
//                        //honari
//                        bundle.putString("key", "21");// put image data in Intent
//                        bundle.putString ("count", "5");
//                        bundle.putString("name","هنری");
//                        //intent.putExtra("count", "275"); // put number of image data in Intent
//                        fragment = new Fragment_recycler();
//                        fragment.setArguments(bundle);
//                        fragmentManager = getFragmentManager();
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.frame, fragment);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
//                        break;
//
//                }
//            }
//        });

        return view;
    }

    public void setAnimation(){
        if(Build.VERSION.SDK_INT>20) {
            Explode explode = new Explode();
            explode.setDuration(1000);
            explode.setInterpolator(new DecelerateInterpolator());
            getActivity().getWindow().setExitTransition(explode);
            getActivity().getWindow().setEnterTransition(explode);
        }
    }

    private void setupView() {
        //Log.d("USERNAME - setupView: ", mAuthHelper.getUsername());
        setWelcomeText(mAuthHelper.getUsername());
    }
    private void setWelcomeText(String username) {
        // Log.d("USERNAME: ", username);
        //  mTextWelcome.setText(String.format(getString(R.string.text_welcome), username));
    }

}
