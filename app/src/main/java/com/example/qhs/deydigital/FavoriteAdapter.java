package com.example.qhs.deydigital;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.StrictMode;
import android.renderscript.ScriptGroup;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ResponseCache;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import Data.DatabaseHandler;
import Model.ImageModel;
import Model.ListItem;
import Recycler.RecyclerActivity;
import Recycler.RecyclerAdapter;

import Gallery.gallery;
import okhttp3.Response;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private Context context;
    private List<ListItem> listItems;

    public  FavoriteAdapter(FavoriteActivity context, List listitem){
        this.context=context;
        this.listItems=listitem;
    }


    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.favorite_item, parent, false);
        return new FavoriteAdapter.ViewHolder(rootView);
     //   View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
       // return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavoriteAdapter.ViewHolder holder, final int position) {
        final DatabaseHandler db = new DatabaseHandler(context);
         ListItem item = listItems.get(position);
        List <String> image_link =new ArrayList<>(Arrays.asList(item.getImgLink().split("\\s*,\\s*")));
         String temp = image_link.get(0);


        if(URLUtil.isValidUrl(temp)) {
            //  temp = temp.replace("https", "http");
            Log.d("linkk",temp);
            Picasso.with(context)
                    .load(temp).resize(200, 200)
                    .into(holder.img);

        }
        else
        {
            db.deleteListItem(item.getId());
            listItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img=(ImageView) itemView.findViewById(R.id.imgF);



        }
        @Override
        public void onClick(View view) {
          //  List<String> image_link = null;
           int Position=getAdapterPosition();
           ListItem item=listItems.get(Position);
            Intent intent =new Intent(context, gallery.class);
//            // putExtra(key,value);
            intent.putExtra("id",item.getId());
            intent.putExtra("name",item.getName());
            intent.putExtra("description",item.getDescription());
          //convert string to array with , seperator
            List <String> image_link =new ArrayList<>(Arrays.asList(item.getImgLink().split("\\s*,\\s*")));

            intent.putStringArrayListExtra("imageJsonObj", (ArrayList <String>) image_link);
         //   intent.putExtra("num_link",item.getNum_link());
//            intent.putExtra("position",Position);
           context.startActivity(intent);

        }

    }


    public boolean URLIsReachable(String urlString)
    {StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
            .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            int responseCode = urlConnection.getResponseCode();
            urlConnection.disconnect();
            return responseCode != 200;
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            return false;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
