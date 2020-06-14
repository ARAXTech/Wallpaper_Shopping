package com.example.qhs.wallpapershopping;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView Txt;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
          icon = (ImageView) itemView.findViewById(R.id.icon);
          Txt=(TextView)itemView.findViewById(R.id.txtView);

        }
    }
}
