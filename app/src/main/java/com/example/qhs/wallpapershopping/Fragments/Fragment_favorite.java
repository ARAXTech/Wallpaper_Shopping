package com.example.qhs.wallpapershopping.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.qhs.wallpapershopping.AuthHelper;
import com.example.qhs.wallpapershopping.FavoriteAdapter;
import com.example.qhs.wallpapershopping.R;
import com.example.qhs.wallpapershopping.network.Admin;
import com.example.qhs.wallpapershopping.network.NetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Data.DatabaseHandler;
import Model.ListItem;

import static com.android.volley.toolbox.Volley.newRequestQueue;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class Fragment_favorite extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private List<ListItem> listItems;
    private AuthHelper mAuthHelper;
    private Admin admin;
    private DatabaseHandler db;
    private int[][] wishlistProductId;
    private String shareKey;
    private int num;
    private RequestQueue queue;
    private ProgressDialog mProgressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        //Toolbar
        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
        title.setText("علاقه مندی ها");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity)getActivity()).onBackPressed();
            }
        });

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity)getActivity()).onBackPressed();
            }
        });


        db = new DatabaseHandler(getContext());

        mAuthHelper = AuthHelper.getInstance(getContext());
        admin = Admin.getInstance(getContext());


        recyclerView = (RecyclerView) view.findViewById(R.id.FavoriteRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItems = new ArrayList<>();



        //db.deleteAll();
        if(db.getFavoriteItemCount()!=0){
            listItems = db.getAllFavoriteItem();}

        num = db.getFavoriteItemCount();

        ListItem[] myArray = listItems.toArray(new ListItem[listItems.size()]);

        wishlistProductId = new int[2][num];
        for (int i = 0; i < num; i++) {
            wishlistProductId[0][i] = Integer.parseInt(myArray[i].getId());
            wishlistProductId[1][i] = -1;
            Log.d(" contact: ",  listItems.get(i).getId());
        }


        adapter = new FavoriteAdapter(getContext(), listItems);
        recyclerView.setAdapter(adapter);


        // connection with site
        mProgressDialog = new ProgressDialog(getContext());
        Arrays.sort(wishlistProductId[0]);

        queue = newRequestQueue(getContext());

        //doGetFavorite();
        shareKey = mAuthHelper.getSharekey();
        getWishlistProduct(shareKey);

        return view;
    }

    private void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void getWishlistProduct(String shareKey) {
        NetRequest request = new NetRequest(getContext());
        request.JsonArrayNetRequest("GET", "wc/v3/wishlist/"+shareKey+"/get_products", mWishlistProductCallback, null);
    }

    private NetRequest.Callback<JSONArray> mWishlistProductCallback = new NetRequest.Callback<JSONArray>(){

        @Override
        public void onResponse(@NonNull JSONArray response) {
            for (int i=0; i< response.length(); i++){
                try {
                    int productId = response.getJSONObject(i).getInt("product_id");
                    //wishlistId = response.getJSONObject(i).getInt("item_id");

                    int idx = Arrays.binarySearch(wishlistProductId[0], productId);
                    if (idx < 0){
                        //get product from site
                        Log.d("NetworkRequest_WishID ", String.valueOf(productId));
                        getProduct(productId);
                    }
                    else{
                        wishlistProductId[1][idx] = 1;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < num; i++) {
                //Delete those that have been deleted from the site
                if (wishlistProductId[1][i] == -1) {
                    Log.d("index ", String.valueOf(wishlistProductId[0][i]));
                    db.deleteListItem(String.valueOf(wishlistProductId[0][i]));
                    adapter.notifyDataSetChanged();

                    Fragment fragment = new Fragment_favorite();
                    ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack("fragment_favorite").commit();

                }
            }


        }

        @Override
        public void onError(String error) {
            for (int i = 0; i < num; i++) {
                if (wishlistProductId[1][i] == -1) {
                    Log.d("index ", String.valueOf(wishlistProductId[0][i]));
                    db.deleteListItem(String.valueOf(wishlistProductId[0][i]));
                    adapter.notifyDataSetChanged();

                    Fragment fragment = new Fragment_favorite();
                    ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack("fragment_favorite").commit();


                    //addProduct(wishlistProductId[0][i]);
                }
            }
            Log.d("Server Error",error);

        }
    };

    public void getProduct(int id){

        NetRequest request = new NetRequest(getContext());
        request.JsonObjectNetRequest("GET", "wc/v3/products/" + id, mProductCallback, admin.getAdminAuth());

    }

    private NetRequest.Callback<JSONObject> mProductCallback = new NetRequest.Callback<JSONObject>() {
        @Override
        public void onResponse(@NonNull JSONObject response) {
            try {
                // if added through cart
                if(db.Exists(response.getString("id"))){
                    ListItem get_item=db.getListItem(Integer.parseInt(response.getString("id")));
                    get_item.setFavorite("true");
                    db.updateListItem(get_item);
                }
                else {
                    //fill img_src
                    JSONArray image_series_json = response.getJSONArray("images");
                    ArrayList<String> images_src = new ArrayList<>();
                    for (int j=0; j < image_series_json.length(); j++){
                        images_src.add(image_series_json.getJSONObject(j).getString("src"));
                    }

                    String imgLink = images_src.toString().substring(1,images_src.toString().length()-1);

                    ListItem item;
                    if (response.getJSONArray("images").length() != 0){
                        item = new ListItem(
                                response.getString("id"),
                                response.getString("name"),
                                response.getString("short_description"),
                                imgLink,
                                "true",
                                response.getJSONArray("images").length(),
                                Integer.parseInt(response.getString("price")),
                                Integer.parseInt(response.getString("stock_quantity")),
                                0,//
                                Integer.parseInt(mAuthHelper.getIdUser())
                        );
                    }
                    else {
                        item = new ListItem(
                                response.getString("id"),
                                response.getString("name"),
                                response.getString("short_description"),
                                imgLink,
                                "true",
                                0,
                                Integer.parseInt(response.getString("price")),
                                Integer.parseInt(response.getString("stock_quantity")),
                                0,//
                                Integer.parseInt(mAuthHelper.getIdUser())
                        );
                    }


                    item.setImg_src(images_src);


                    db.addListItem(item);
                    listItems.add(item);
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Log.d("JSONException_get", e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String error) {
        }
    };





}
