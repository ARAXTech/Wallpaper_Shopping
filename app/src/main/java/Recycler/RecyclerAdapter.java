
package Recycler;

import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;

import com.bumptech.glide.Glide;
        import com.bumptech.glide.load.engine.DiskCacheStrategy;
import Ui.DimensionUtils;
import com.example.qhs.deydigital.R;


import org.json.JSONArray;
        import org.json.JSONException;

import java.util.ArrayList;
        import java.util.List;

import Gallery.gallery;
import Model.ListItem;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private Context context;
    private List<ListItem> listItems;


    public  RecyclerAdapter(  Context context, List listitem){
        this.context= context;
        this.listItems=listitem;
        //this.imageList=imageList;
        //this.image_obj = image_obj;
        // Log.d("cunstructor**", image_obj.toString());
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(view);



    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, int position) {

        final ListItem item = listItems.get(position);

        //image_obj = image_obj;
        holder.id = item.getId();
        holder.name = item.getName();
        holder.description = item.getDescription();

        //holder.txtdescriptionR.setText(item.getDescription());
        //  holder.imgR.setImageURI(Uri.parse(item.getImgLink()));

        String temp = item.getImgLink();

        temp = temp.replace("https", "http");

        //Glide.get(context).clearMemory();
        Log.d("glide",item.getImgLink().toString());
        Glide.with(context)
                .load(temp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.skipMemoryCache(true)
                //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .thumbnail(0.5f)
                //.crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                ///.override(400,200)
                .into(holder.imgR);



       //   Picasso.with(context)
        //    .load(temp).placeholder(R.drawable.ic_launcher_background)
          //.resize(700,500)

        //  .into(holder.imgR);

        /*Picasso.with(context)
                .load(item.getImgLink())
                .networkPolicy(NetworkPolicy.OFFLINE)
               .into(holder.imgR, new Callback() {
                    @Override
                   public void onSuccess() {
                        //..image loaded from cache
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(item.getImgLink())
                                .error(R.drawable.ic_launcher_background)
                                .into(holder.imgR, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        //... image loaded from online
                                    }


@Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });*/
        /*Glide
                .with( context )
                .load( item.getImgLink() )
               // .skipMemoryCache( true )
                .into( holder.imgR );*/

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void addItem(ListItem item){



        listItems.add(item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgR;
        //public TextView txtdescriptionR;
        public JSONArray image_obj ;
        public String id;
        public String description;
        public String name;
        ArrayList<String> imageList;



        public ViewHolder(View itemView) {
            super(itemView);

            GridLayoutManager.LayoutParams layoutParams = new
                    GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            float margin = DimensionUtils.convertDpToPixel(0, context);
            layoutParams.setMargins((int) margin, (int) margin, (int) margin,
                    (int) margin);

            itemView.setLayoutParams(layoutParams);


            imgR=itemView.findViewById(R.id.imgR);

            //txtdescriptionR=(TextView) itemView.findViewById(R.id.txtdescriptionR);




            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    imageList = new ArrayList<>();
                    image_obj = new JSONArray();
                    ListItem item = listItems.get(getAdapterPosition());

                   if(item.get_img_src_size() == 0){

                       for(int i=0; i < item.getImage_json().length(); i++) {
                           try {
                           /* image_obj.put(item.getImage_json().get(i));
                            imageList.add(image_obj.getJSONObject(i).getString("src"));
                            Log.d("adapter***",image_obj.getJSONObject(i).getString("src"));*/

                               String temp = item.getImage_json().getJSONObject(i).getString("src");

                               temp = temp.replace("https", "http");

                               item.setImg_src(temp);


                           } catch (JSONException e) {
                               e.printStackTrace();
                           }
                       }
                   }


                    Log.d("out of for","*******");

                    Intent intent =new Intent(context, gallery.class);
                    /*intent.putStringArrayListExtra("image_list",(ArrayList<String>) imageList);
                    for(int i=0; i < imageList.size();i++){
                        Log.d("ADAPTER",imageList.get(i));
                    }*/

                    Log.d("tedad dar json obj***",String.valueOf(item.getImage_json().length()));
                    //.putExtra("image_list",imageList);

                   /* for(int i = 0; i < image_obj.length(); i++){
                        try {
                            imageList.add(image_obj.getJSONObject(i).getString("src"));
                            Log.d("src**",image_obj.getJSONObject(i).getString("src"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    */
                    // String jsonArray = image_obj.toString();
                    //Log.d("JSON*",jsonArray);
                    intent.putStringArrayListExtra("imageJsonObj",item.img_src);
                    intent.putExtra("name",item.getName());
                    intent.putExtra("id",item.getId());
                    intent.putExtra("description",item.getDescription());
                    context.startActivity(intent);
                    Activity activity = (Activity) context;
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

        }

    }

}