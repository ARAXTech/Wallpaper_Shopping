package Recycler;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.qhs.wallpapershopping.Fragments.Fragment_gallery;

import com.example.qhs.wallpapershopping.R;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import Model.ListItem;

import static android.support.test.InstrumentationRegistry.getContext;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    public static Context context;
    public static List<ListItem> listItems;


    public  RecyclerAdapter(  Context context, List listitem){
        this.context= context;
        this.listItems=listitem;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, int position) {

        final ListItem item = listItems.get(position);
        holder.id = item.getId();
        holder.name = item.getName();
        holder.description = item.getDescription();

        List <String> image_link = Arrays.asList(item.getImgLink().split("\\s*,\\s*"));

        final String temp = image_link.get(0);

        //String temp = item.getImgLink();
        Picasso.with(context)
                .load(temp)
                .fit().centerCrop()
                .into(holder.imgR);

       // ViewCompat.setTransitionName(holder.imgR, mPhotoObjects.get(position).photo);


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void addItem(ListItem item){

        listItems.add(item);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgR;
        public String id;
        public String description;
        public String name;



        public ViewHolder(View itemView) {
            super(itemView);
            imgR=itemView.findViewById(R.id.imgR);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    ListItem item = listItems.get(getAdapterPosition());

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("productItem", item);

                    Fragment fragment = new Fragment_gallery();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity)context).
                            getSupportFragmentManager()
                            .beginTransaction()
                            .addSharedElement(imgR, imgR.getTransitionName())
                            .replace(R.id.frame, fragment)
                            .addToBackStack("fragment_gallery")
                            .commit();
//                    ActivityOptionsCompat option  =
//                            ActivityOptionsCompat.makeSceneTransitionAnimation(Fragment_recycler.class,imgR,
//                                    ViewCompat.getTransitionName(imgR));



                }
            });

        }


    }

}