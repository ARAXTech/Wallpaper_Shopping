package com.example.qhs.wallpapershopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.qhs.wallpapershopping.AuthHelper;
import com.example.qhs.wallpapershopping.Fragments.Fragment_favorite;
import com.example.qhs.wallpapershopping.Fragments.Fragment_gallery;
import com.example.qhs.wallpapershopping.Fragments.Fragment_search;
import com.example.qhs.wallpapershopping.network.Admin;
import com.example.qhs.wallpapershopping.network.NetRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Data.DatabaseHandler;
import Model.ListItem;


import static com.android.volley.toolbox.Volley.newRequestQueue;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private Context context;
    private List<ListItem> listItems;
    private DatabaseHandler db;
    private RequestQueue queue;
    private AuthHelper mAuthHelper;
    private Admin admin;


    public  FavoriteAdapter(Context context, List listitem){
        this.context=context;
        this.listItems=listitem;
        db = new DatabaseHandler(context);
        queue = newRequestQueue(context);
        mAuthHelper = AuthHelper.getInstance(context);
        admin = Admin.getInstance(context);
    }


    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.favorite_item, parent, false);
        return new FavoriteAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final FavoriteAdapter.ViewHolder holder, final int position) {

        final ListItem item = listItems.get(position);
        List <String> image_link =new ArrayList<>(Arrays.asList(item.getImgLink().split("\\s*,\\s*")));
        final String temp = image_link.get(0);
        int id = Integer.parseInt(item.getId());
        JsonObjectRequest objectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, "http://mobifytech.ir/wp-json/wc/v3/products/"+item.getId(), null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //  temp = temp.replace("https", "http");
                        Picasso.with(context)
                                .load(temp).resize(200, 200)
                                .into(holder.img);

                        holder.favorite_title.setText(item.getName());
                        String str=item.getDescription().replaceAll("<p>","");
                        holder.favorite_description.setText(str.replaceAll("</p>",""));


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    Picasso.with(context)
                            .load(temp).resize(200, 200)
                            .into(holder.img);
                }else {
                    db.deleteListItem(item.getId());
                    listItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                }


            }
        }){
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + admin.getAdminAuth());
                return params;
            }

        };

        objectRequest.setRetryPolicy(new DefaultRetryPolicy(1000000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(objectRequest);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Button del;
        public ImageView img;
        public NetRequest request;
        public TextView favorite_title;
        public TextView favorite_description;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img = (ImageView) itemView.findViewById(R.id.imgF);
            del = (Button) itemView.findViewById(R.id.btnDelete);
            favorite_title=(TextView)itemView.findViewById(R.id.txtView_favorite_title);
            favorite_description=(TextView)itemView.findViewById(R.id.txtView_favorite_description);
            request = new NetRequest(context);

            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListItem item=listItems.get(getAdapterPosition());
                    db.deleteListItem(item.getId());
                    //notifyDataSetChanged();

                    //remove from site
                    request.JsonArrayNetRequest("GET", "wc/v3/wishlist/"+mAuthHelper.getSharekey()+"/get_products", mWishlistProductCallback, null);
                }
            });
        }
        private NetRequest.Callback<JSONArray> mWishlistProductCallback = new NetRequest.Callback<JSONArray>(){

            @Override
            public void onResponse(@NonNull JSONArray response) {
                ListItem item=listItems.get(getAdapterPosition());

                for (int i=0; i< response.length(); i++){
                    try {
                        int productId = response.getJSONObject(i).getInt("product_id");
                        if (productId == Integer.parseInt(item.getId())){

                            request.JsonStringNetRequest("POST", "wc/v3/wishlist/remove_product/" + response.getJSONObject(i).getInt("item_id"));
                            //context.startActivity(new Intent(context, FavoriteActivity.class));
                            Fragment fragment = new Fragment_favorite();
                            ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();

                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {
                Log.d("Server Error",error);

            }
        };


        @Override
        public void onClick(View view) {
            //  List<String> image_link = null;
            int Position=getAdapterPosition();
            ListItem item=listItems.get(Position);
            Bundle bundle = new Bundle();
            bundle.putString("id",item.getId());
            bundle.putString("name",item.getName());
            bundle.putString("description",item.getDescription());
            //convert string to array with , seperator
            List <String> image_link =new ArrayList<>(Arrays.asList(item.getImgLink().split("\\s*,\\s*")));

            bundle.putStringArrayList("imageJsonObj", (ArrayList <String>) image_link);
            Fragment fragment = new Fragment_gallery();
            fragment.setArguments(bundle);
            ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();


        }

    }
}
