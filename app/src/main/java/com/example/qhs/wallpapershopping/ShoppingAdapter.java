package com.example.qhs.wallpapershopping;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Data.DatabaseHandler;
import Gallery.gallery;
import Model.ListItem;

public class ShoppingAdapter  extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {
    private Context context;
    private List<ListItem> listItems;
    private boolean mFlag;

    public  ShoppingAdapter(ShoppingActivity context, List listitem) {
        this.context = context;
        this.listItems = listitem;
        mFlag = false;
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
        for (ListItem item : db1.getAllListItem())
            Log.d("count of " + item.getId(), String.valueOf(item.getCount()));
        final ListItem item = listItems.get(position);
        final List <String> image_link = new ArrayList <>(Arrays.asList(item.getImgLink().split("\\s*,\\s*")));
        String temp = image_link.get(0);
        //  temp = temp.replace("https", "http");
        if (URLUtil.isValidUrl(temp)) {
            Log.d("LINKKKK", temp);
            Picasso.with(context)
                    .load(temp).resize(200, 200)
                    .into(holder.img);
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db1.deleteListItem(item.getId());
                    listItems.remove(position); // remove the item from list
                    notifyItemRemoved(position); // notify the adapter about the removed item
                    notifyItemRangeChanged(position, getItemCount());
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
                    }
                    if (mFlag) {
                        // Your selection handling code here
                        mFlag = false;

                        context.startActivity(new Intent(context, ShoppingActivity.class));
                    }
                    Log.d("FLAG ONBIND: ", String.valueOf(mFlag));

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
        {       Log.d("LI", temp);
            db1.deleteListItem(item.getId());
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
}
