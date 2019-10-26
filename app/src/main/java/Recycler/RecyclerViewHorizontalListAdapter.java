package Recycler;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.qhs.deydigital.MainActivity;
import com.example.qhs.deydigital.R;
import com.example.qhs.deydigital.SecondActivity;

import java.util.List;

import Model.RecyclerHorizentalItem;

/**
 * Created by Sadruddin on 12/24/2017.
 */
public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.ViewHolder> {
    private List <RecyclerHorizentalItem> horizontalList;
    Context context;

    public RecyclerViewHorizontalListAdapter(List <RecyclerHorizentalItem> horizontalList, Context context) {
        this.horizontalList = horizontalList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_horizental, parent, false);
        ViewHolder gvh = new ViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.imageView.setImageResource(horizontalList.get(position).getImage());
        holder.txtview.setText(horizontalList.get(position).getTitle());
        // holder.imageView.setOnClickListener(new View.OnClickListener() {
        //@Override
        //  public void onClick(View v) {
        //   String productName = horizontalList.get(position).getProd().toString();
        //  Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
        //}
        //});
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iconH);
            txtview = (TextView) view.findViewById(R.id.textViewH);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    switch (position){
                        case 0:
                            //salon paziraee
                            Intent intent = new Intent(context, RecyclerActivity.class);
                            intent.putExtra("key", "18"); // put image data in Intent
                            //intent.putExtra("count", "275"); // put number of image data in Intent
                            intent.putExtra("count", "15");
                            context.startActivity(intent); // start Intent
                          //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 1:
                            //otagh koodak
                            //salon paziraee
                            Intent intent1 = new Intent(context, RecyclerActivity.class);
                            intent1.putExtra("key", "111"); // put image data in Intent
                            //intent.putExtra("count", "275"); // put number of image data in Intent
                            intent1.putExtra("count", "5");
                            context.startActivity(intent1); // start Intent
                           // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;

                        case 2:
                            //posht e TV
                            Intent intent2 = new Intent(context, RecyclerActivity.class);
                            intent2.putExtra("key", "20"); // put image data in Intent
                            //intent2.putExtra("count", "99"); // put number of image data in Intent
                            intent2.putExtra("count", "5");
                            context.startActivity(intent2); // start Intent
                           // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 3:
                            //otagh khab
                            Intent intent3 = new Intent(context, RecyclerActivity.class);
                            intent3.putExtra("key", "112"); // put image data in Intent
                            //intent.putExtra("count", "275"); // put number of image data in Intent
                            intent3.putExtra("count", "5");
                            context.startActivity(intent3); // start Intent
                          //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;

                        case 4:
                            //3 boodi
                            Intent intent4 = new Intent(context, RecyclerActivity.class);
                            intent4.putExtra("key", "113"); // put image data in Intent
                            //intent.putExtra("count", "275"); // put number of image data in Intent
                            intent4.putExtra("count", "5");
                            context.startActivity(intent4); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 5:
                            //honari
                            Intent intent5 = new Intent(context, RecyclerActivity.class);
                            intent5.putExtra("key", "21"); // put image data in Intent
                            //intent.putExtra("count", "275"); // put number of image data in Intent
                            intent5.putExtra("count", "5");
                            context.startActivity(intent5); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;



                    }
                    // set an Intent to Another Activity
                    // Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    //intent.putExtra("image", logos[position]); // put image data in Intent
                    //startActivity(intent); // start Intent
                }
            });

        }}}


