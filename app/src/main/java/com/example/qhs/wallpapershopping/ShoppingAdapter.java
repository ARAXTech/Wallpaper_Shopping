package com.example.qhs.wallpapershopping;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.ListPreloader;
import com.example.qhs.wallpapershopping.Fragments.Fragment_Shopping;
import com.example.qhs.wallpapershopping.Fragments.Fragment_favorite;
import com.example.qhs.wallpapershopping.Fragments.Fragment_gallery;
import com.example.qhs.wallpapershopping.R;
import com.example.qhs.wallpapershopping.network.Admin;
import com.example.qhs.wallpapershopping.network.NetRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import Data.DatabaseHandler;
import Model.ListItem;

public class ShoppingAdapter  extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {
    private Context context;
    private NetRequest request;
    private List<ListItem> listItems;
    private boolean mFlag;
    private String[][] id;
    private Admin admin;
    private AuthHelper mAuthHelper;
    private DatabaseHandler db;
    private Integer[][] shoppingProductId;
    private int quantity;
    int num;
    ItemCallback Listener;

    public ShoppingAdapter(Context context, List listitem,ItemCallback Listener) {
        this.context = context;
        this.listItems = listitem;
        this.Listener=Listener;
        mFlag = false;
        request = new NetRequest(context);
        admin = Admin.getInstance(context);
        mAuthHelper = AuthHelper.getInstance(context);
        id = new String[2][10];
        db = new DatabaseHandler(context);
        num = db.getShoppingItemCount();

        shoppingProductId = new Integer[num][3];
        for (int i = 0; i < num; i++) {
            shoppingProductId[i][0] = Integer.valueOf(listItems.get(i).getId());
            shoppingProductId[i][1] = -1;
            shoppingProductId[i][2] = listItems.get(i).getCount();
        }


        // sort the array on item id(first column)
        Arrays.sort(shoppingProductId, new Comparator<Integer[]>() {
            @Override
            //arguments to this method represent the arrays to be sorted
            public int compare(Integer[] o1, Integer[] o2) {
                //get the item ids which are at index 0 of the array
                Integer itemIdOne = o1[0];
                Integer itemIdTwo = o2[0];
                // sort on item id
                return itemIdOne.compareTo(itemIdTwo);
            }
        });

        //getFromServer();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.shopping_item, parent, false);

        return new ShoppingAdapter.ViewHolder(rootView);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final ListItem item = listItems.get(position);
        final List <String> image_link = new ArrayList <>(Arrays.asList(item.getImgLink().split("\\s*,\\s*")));
        String temp = image_link.get(0);
        //  temp = temp.replace("https", "http");
        if (URLUtil.isValidUrl(temp)) {
            Picasso.with(context)
                    .load(temp).resize(200, 200)
                    .into(holder.img);

            holder.counter.setText(String.valueOf(item.getCount_shop()));
             Log.d("shopcount1",String.valueOf(item.getCount_shop()));


            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    holder.counter.setText(String.valueOf(item.getCount_shop()-1));
                    item.setCount_shop(item.getCount_shop()-1);
                    Log.d("shopcount2",String.valueOf(item.getCount_shop()));

                    if (item.getCount_shop()<1) {
                        String deleteId = item.getId();
                        db.deleteListItem(deleteId);
                        listItems.remove(position); // remove the item from list
                        notifyItemRemoved(position); // notify the adapter about the removed item
                        notifyItemRangeChanged(position, getItemCount());
                        //deleteFromServer(deleteId);
                    }
                    db.updateListItem(item);
                    //updateQuantityOnServer(item.getId(), item.getCount_shop());
                    Listener.TotalPrice();


                }
            });
            holder.counterTxt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    holder.counter.setText(String.valueOf(item.getCount_shop()+1));
                    item.setCount_shop(item.getCount_shop()+1);
                    Log.d("shopcount3",String.valueOf(item.getCount_shop()));
                    db.updateListItem(item);
                    //updateQuantityOnServer(item.getId(), item.getCount_shop());
                    Listener.TotalPrice();

               }
            });

            holder.price.setText(String.valueOf(item.getPrice()));

            holder.name.setText(" نام محصول:" + item.getName());

        }
        else
        {
            db.deleteListItem(item.getId());
            listItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());

        }
    }

    public void getProduct(int id){
        NetRequest request = new NetRequest(context);
        request.JsonObjectNetRequest("GET", "wc/v3/products/" + id, mProductCallback, admin.getAdminAuth());

    }

    private NetRequest.Callback<JSONObject> mProductCallback = new NetRequest.Callback<JSONObject>() {
        @Override
        public void onResponse(@NonNull JSONObject response) {
            try {
                ListItem item = new ListItem(
                        response.getString("id"),
                        response.getString("name"),
                        response.getString("short_description"),
                        response.getJSONArray("images").getJSONObject(0).getString("src"),
                        "false",
                        response.getJSONArray("images").length(),
                        Integer.parseInt(response.getString("price")),
                        1,
                        quantity
                );

                db.addListItem(item);
                listItems.add(item);
                notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d("JSONException_get", e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String error) {
        }
    };


    private void updateQuantityOnServer(String updateId, int newQuantity) {
        ArrayList<String> idList = new ArrayList<>(Arrays.asList(id[0]));
        int idx = idList.indexOf(updateId);
        Log.d("index of update id", String.valueOf(idx));
        String cartKey="";
        if (idx>=0 & idx<id.length) {
            cartKey = id[1][idx];
        }
        Log.d("Cart key of update id", cartKey);
        request.JsonObjectNetRequest("POST", "cocart/v1/item?cart_item_key=" + cartKey + "&quantity=" + newQuantity,
                null, null);
        request.JsonStringNetRequest("POST", "/wp-json/cocart/v1/calculate");

    }

    private void deleteFromServer(String deleteId) {
        ArrayList<String> idList = new ArrayList<>(Arrays.asList(id[0]));
        int idx = idList.indexOf(deleteId);
        Log.d("index of deleted id", String.valueOf(idx));
        String cartKey="";
        if (idx>=0 & idx<id.length) {
            cartKey = id[1][idx];
        }
        Log.d("Cart key of deleted id", cartKey);
        request.JsonStringNetRequest("DELETE", "cocart/v1/item?cart_item_key="+ cartKey);

        request.JsonStringNetRequest("POST", "/wp-json/cocart/v1/calculate");
    }

    private void getFromServer() {
        request.JsonObjectNetRequest("GET", "cocart/v1/get-cart/" + mAuthHelper.getIdUser(), mShoppingProductCallback, admin.getAdminAuth());
    }


    private NetRequest.Callback<JSONObject> mShoppingProductCallback = new NetRequest.Callback<JSONObject>(){

        @Override
        public void onResponse(@NonNull JSONObject response) {
            Iterator<String> keys = response.keys();

            //copy first column to a new array for binarySearch
            Integer[] arrayIdx = new Integer[num];
            for (int i=0; i<num; i++){
                arrayIdx[i] = shoppingProductId[i][0];
            }

            int j=0;
            while(keys.hasNext()) {
                String key = keys.next();
                try {
                    if (response.get(key) instanceof JSONObject) {

                        int productId = response.getJSONObject(key).getInt("product_id");
                        quantity = response.getJSONObject(key).getInt("quantity");


                        id[0][j] = String.valueOf(productId);
                        id[1][j] = key;


                        int idx = Arrays.binarySearch(arrayIdx, productId);
                        Log.d("productId,idx ", productId+" "+idx);
                        if (idx < 0){
                            //get product from site
                            Log.d("getProduct_WishID ", String.valueOf(productId));
                            getProduct(productId);
                        }
                        else{
                            shoppingProductId[idx][1] = 1;
                            // TODO: check quantity is updated
                            Log.d("QuantityDB_QServer ", shoppingProductId[idx][2]+" "+quantity);
                            if (quantity != shoppingProductId[idx][2]){
                                ListItem item = new ListItem(
                                        listItems.get(idx).getId(),
                                        listItems.get(idx).getName(),
                                        listItems.get(idx).getDescription(),
                                        listItems.get(idx).getImgLink(),
                                        listItems.get(idx).getFavorite(),
                                        listItems.get(idx).getNum_link(),
                                        listItems.get(idx).getPrice(),
                                        listItems.get(idx).getCount(),
                                        quantity
                                );

                                db.updateListItem(item);
                                Log.d("after update ", String.valueOf(db.getListItem(productId).getCount_shop()));
                                //listItems.get(idx).setCount_shop(quantity);
                                //   adapter.notifyDataSetChanged();
                                listItems.set(idx, item);
                                notifyItemChanged(idx);
                                Log.d("adapter updated ", String.valueOf(listItems.get(idx).getCount_shop()));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                j = j + 1;
            }

            // delete item from app. this item was deleted from site before
            for (int i = 0; i < num; i++) {
                if (shoppingProductId[i][1] == -1) {
//                    Log.d("addProduct_index ", String.valueOf(shoppingProductId[i][0]));
//                    addProduct(shoppingProductId[i][0], shoppingProductId[i][2]);

                    db.deleteListItem(String.valueOf(shoppingProductId[i][0]));

                    listItems.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, getItemCount());
                    notifyDataSetChanged();

                }
            }

        }

        @Override
        public void onError(String error) {
            Log.d("Server Error ", error);

        }
    };



    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public TextView counter;
        public Button deleteBtn;
        public TextView price;
        public TextView name;
        public Button counterTxt;
     //   public TextView total;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img=(ImageView) itemView.findViewById(R.id.imgF);
            counter=(TextView)itemView.findViewById(R.id.counter);
            deleteBtn=(Button)itemView.findViewById(R.id.deleteBtn);
            counterTxt=(Button)itemView.findViewById(R.id.counterTxt);
            price=(TextView)itemView.findViewById(R.id.price);
            name=(TextView)itemView.findViewById(R.id.name);
            //total=(TextView)itemView.findViewById(R.id.totalPrice);

        }
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
    public interface ItemCallback{
        void TotalPrice();
    }
}