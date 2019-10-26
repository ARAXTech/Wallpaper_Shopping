package com.example.qhs.deydigital;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class GridAdapter  extends BaseAdapter {
    Context context;
    int logos[];
    String txt[];
    String logo_txt[];
    Boolean b;

    LayoutInflater inflter;
    //constractor for drawable images
    public GridAdapter(Context applicationContext, int[] logos,String[] txt,Boolean b) {
        this.context = applicationContext;
        this.logos = logos;
        this.txt=txt;
        this.txt=txt;
        this.b=b;
        inflter = (LayoutInflater.from(applicationContext));
    }
    //constractor for url images
    public GridAdapter(Context applicationContext, String[] logo_txt,String[] txt,Boolean b) {
        this.context = applicationContext;
        this.logo_txt = logo_txt;
        this.txt=txt;
        this.b=b;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        if(b==false)
        {return logos.length;}
        if(b==true)
        {return logo_txt.length;}
        return 0;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_gridview, null); // inflate the layout
        ImageView icon = (ImageView) view.findViewById(R.id.icon); // get the reference of ImageView
        if(b==false) {

            icon.setImageResource(logos[i]); // set logo images
        }
        if (b==true){
            //set sublist images
            Glide.with(context)
                    .load(logo_txt[i])
                    .thumbnail(0.5f)
                    //.crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    ///.override(400,200)
                    .into(icon);
        }
        Typeface face=Typeface.createFromAsset(context.getAssets(),"fonts/homa.ttf");

        TextView Txt=(TextView)view.findViewById(R.id.txtView);
        Txt.setText(txt[i].toString());
        Txt.setTypeface(face);
        return view;
    }
}
