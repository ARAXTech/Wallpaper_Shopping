package com.example.qhs.wallpapershopping;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qhs.wallpapershopping.Fragments.Fragment_gallery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.ListItem;
import Recycler.Fragment_recycler;

public class StaggeredRecyclerAdapter extends RecyclerView.Adapter<StaggeredRecyclerAdapter.ImageViewHolder> {
    Context context;
    int logos[];
    String txt[];
    String logo_txt[];
    Boolean b;

    LayoutInflater inflter;
    //constractor for drawable images
    public StaggeredRecyclerAdapter (Context applicationContext, int[] logos,String[] txt,Boolean b) {
        this.context = applicationContext;
        this.logos = logos;
        this.txt=txt;
        this.txt=txt;
        this.b=b;
        inflter = (LayoutInflater.from(applicationContext));
    }
    //constractor for url images
    public StaggeredRecyclerAdapter (Context applicationContext, String[] logo_txt,String[] txt,Boolean b) {
        this.context = applicationContext;
        this.logo_txt = logo_txt;
        this.txt=txt;
        this.b=b;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @NonNull
    @Override
    public StaggeredRecyclerAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View rootView = inflater.inflate(R.layout.activity_gridview, viewGroup, false);
        return new StaggeredRecyclerAdapter.ImageViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaggeredRecyclerAdapter.ImageViewHolder imageViewHolder, int i) {
        if(b==false) {

            imageViewHolder.icon.setImageResource(logos[i]); // set logo images
        }
        if (b==true){
            //set sublist images
            Glide.with(context)
                    .load(logo_txt[i])
                    .thumbnail(0.5f)
                    //.crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    ///.override(400,200)
                    .into(imageViewHolder.icon);
        }
        //Typeface face=Typeface.createFromAsset(context.getAssets(), "font/byekan.ttf");

        imageViewHolder.Txt.setText(txt[i].toString());
    }

    @Override
    public int getItemCount() {
        if(b==false)
        {return logos.length;}
        if(b==true)
        {return logo_txt.length;}
        return 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public ImageView icon;
        public TextView Txt;
        public ImageViewHolder(@NonNull View itemView)  {
            super(itemView);
            itemView.setOnClickListener(this);
          icon = (ImageView) itemView.findViewById(R.id.icon);
          Txt=(TextView)itemView.findViewById(R.id.txtView);


        }

        public void onClick(View view) {
            int position=getAdapterPosition();
            Bundle bundle = new Bundle();
            Fragment fragment;
            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction;
            switch (position){
                case 0:
                    //salon paziraee

                    bundle.putString("key", "18");
                    bundle.putString("count", "15");
                    bundle.putString("name","سالن پذیرایی");
//                        Intent intent = new Intent(getContext(), Fragment_recycler.class);
//                        intent.putExtra("key", "18"); // put image data in Intent
//                        //intent.putExtra("count", "275"); // put number of image data in Intent
//                        intent.putExtra("count", "15");
                    //animation
                    if(Build.VERSION.SDK_INT>20){
//                            ActivityOptions options =
//                                    ActivityOptions.makeSceneTransitionAnimation(getActivity());
//                            startActivity(intent,options.toBundle());


                        fragment = new Fragment_recycler();
                        fragment.setArguments(bundle);
                        ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                    }else {


                        fragment = new Fragment_recycler();
                        fragment.setArguments(bundle);
                        ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                    }
                    //End animation
                    //the below line commented because of animation
                    //startActivity(intent); // start Intent
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case 1:
                    //otagh koodak
                    bundle.putString("key", "111");// put image data in Intent
                    bundle.putString ("count", "5");
                    bundle.putString("name","اتاق کودک");

                    fragment = new Fragment_recycler();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                    break;

                case 2:
                    //posht e TV
                    bundle.putString("key", "20");// put image data in Intent
                    bundle.putString ("count", "5");
                    bundle.putString("name","پشت TV");


                    fragment = new Fragment_recycler();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                    break;
                case 3:
                    //otagh khab
                    bundle.putString("key", "112");// put image data in Intent
                    bundle.putString ("count", "5");
                    bundle.putString("name","اتاق خواب");


                    fragment = new Fragment_recycler();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                    break;

                case 4:
                    //3 boodi
                    bundle.putString("key", "113");// put image data in Intent
                    bundle.putString ("count", "5");
                    bundle.putString("name","سه بعدی");


                    fragment = new Fragment_recycler();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                case 5:
                    //honari
                    bundle.putString("key", "21");// put image data in Intent
                    bundle.putString ("count", "5");
                    bundle.putString("name","هنری");


                    fragment = new Fragment_recycler();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                    break;

            }
        }

    }
}
