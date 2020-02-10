package com.example.qhs.wallpapershopping;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.example.qhs.wallpapershopping.Fragments.Fragment_Shopping;
import com.example.qhs.wallpapershopping.Fragments.Fragment_favorite;
import com.example.qhs.wallpapershopping.Fragments.Fragment_gallery;
import com.example.qhs.wallpapershopping.R;
import com.example.qhs.wallpapershopping.network.NetRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import Data.DatabaseHandler;
import Model.ListItem;

public class ShoppingAdapter  extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {
    private Context context;
    private NetRequest request;
    private List<ListItem> listItems;
    private boolean mFlag;
    private int deleteId;

    public  ShoppingAdapter(Context context, List listitem) {
        this.context = context;
        this.listItems = listitem;
        mFlag = false;
        request = new NetRequest(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.shopping_item, parent, false);
        return new ShoppingAdapter.ViewHolder(rootView);
    }
    private boolean onBind;
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final DatabaseHandler db1 = new DatabaseHandler(context);
//        for (ListItem item : db1.getAllShoppingItem())
//            Log.d("count of " + item.getId(), String.valueOf(item.getCount()));
        final ListItem item = listItems.get(position);
        final List <String> image_link = new ArrayList <>(Arrays.asList(item.getImgLink().split("\\s*,\\s*")));
        String temp = image_link.get(0);
        //  temp = temp.replace("https", "http");
        if (URLUtil.isValidUrl(temp)) {
            Picasso.with(context)
                    .load(temp).resize(200, 200)
                    .into(holder.img);

            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteId = Integer.parseInt(item.getId());
                    db1.deleteListItem(item.getId());
                    listItems.remove(position); // remove the item from list
                    notifyItemRemoved(position); // notify the adapter about the removed item
                    notifyItemRangeChanged(position, getItemCount());

                   // deleteFromServer();

                }
            });
            holder.counter.setSelection(item.getCount() - 1);

            holder.counter.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mFlag = true;
                    return false;
                }
            });

            holder.counter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                    // On selecting a spinner item
                    if (!onBind) {
                        item.setCount(Integer.parseInt((String) parent.getItemAtPosition(position)));
                        db1.updateListItem(item);
                        notifyItemRangeChanged(position, getItemCount());

                        final JSONObject postparams = new JSONObject();

//                        try {
//                            postparams.put("cart_item_key", );
//                            postparams.put("quantity", parent.getItemAtPosition(position));
//
//                        } catch (JSONException e) {
//                            Log.d("JSONException_add", e.getMessage());
//                            e.printStackTrace();
//                        }

                    //    request.JsonObjectNetRequest("POST", "cocart/v1/item", null, null);
//                        String item = parent.getItemAtPosition(position).toString();
//                        Log.d("SPINNER ", item);
                    }
                    if (mFlag) {
                        // Your selection handling code here
                        mFlag = false;
                        Fragment fragment = new Fragment_Shopping();
                        //AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        // Fragment myFragment = new TaskApprovalFragmentDetails();
                        ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();

                    }

                }

                @Override
                public void onNothingSelected(AdapterView <?> parent) {
                    // todo for nothing selected

                }
            });
            holder.price.setText(String.valueOf(item.getPrice()));

            holder.name.setText(" نام محصول:" + item.getName());

        }
        else
        {
            db1.deleteListItem(item.getId());
            listItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    private void deleteFromServer() {
        request.JsonObjectNetRequest("GET", "cocart/v1/get-cart", mShoppingProductCallback, null);


    }

    private NetRequest.Callback<JSONObject> mShoppingProductCallback = new NetRequest.Callback<JSONObject>(){

        @Override
        public void onResponse(@NonNull JSONObject response) {
            Iterator<String> keys = response.keys();

            while(keys.hasNext()) {
                String key = keys.next();
                try {
                    if (response.get(key) instanceof JSONObject) {
                        Log.d("SHOPPING ", key);

                        int productId = response.getJSONObject(key).getInt("product_id");
                        if (productId == deleteId){
                            //request.JsonStringNetRequest("DELETE", "cocart/v1/item", key);
                            break;
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
        public Spinner counter;
        public Button deleteBtn;
        public TextView price;
        public TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img=(ImageView) itemView.findViewById(R.id.imgF);
            counter=(Spinner)itemView.findViewById(R.id.counter);
            deleteBtn=(Button)itemView.findViewById(R.id.deleteBtn);
            price=(TextView)itemView.findViewById(R.id.price);
            name=(TextView)itemView.findViewById(R.id.name);
            String[] items = new String[]{"1", "2", "3"};
            ArrayAdapter<String> Spinneradapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
            Spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            counter.setAdapter(Spinneradapter);


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
}
