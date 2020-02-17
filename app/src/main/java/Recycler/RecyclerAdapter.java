package Recycler;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

import Model.ListItem;

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

        String temp = item.getImgLink();
        Picasso.with(context)
                .load(temp)
                .into(holder.imgR);

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
        //public TextView txtdescriptionR;
        public JSONArray image_obj ;
        public String id;
        public String description;
        public String name;
        ArrayList<String> imageList;



        public ViewHolder(View itemView) {
            super(itemView);
            imgR=itemView.findViewById(R.id.imgR);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    imageList = new ArrayList<>();
                    image_obj = new JSONArray();
                    ListItem item = listItems.get(getAdapterPosition());

                    if(item.get_img_src_size() == 0){

                        for(int i=0; i < item.getImage_json().length(); i++) {
                            try {

                                String temp = item.getImage_json().getJSONObject(i).getString("src");

                                temp = temp.replace("https", "http");
                                item.setImg_src(temp);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("imageJsonObj",item.img_src);
                    bundle.putString("name",item.getName());
                    bundle.putString("id",item.getId());
                    bundle.putString("description",item.getDescription());

                    Fragment fragment = new Fragment_gallery();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack("tag").commit();
                }
            });

        }

    }

}