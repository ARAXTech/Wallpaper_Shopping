package Recycler;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.qhs.wallpapershopping.R;

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

                    Bundle bundle = new Bundle();
                    Fragment fragment;

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    switch (position){
                        case 0:
                            //salon paziraee

                            bundle.putString("key", "18");
                            bundle.putString("count", "15");
//                        Intent intent = new Intent(getContext(), Fragment_recycler.class);
//                        intent.putExtra("key", "18"); // put image data in Intent
//                        //intent.putExtra("count", "275"); // put number of image data in Intent
//                        intent.putExtra("count", "15");
                            //animation
                            if(Build.VERSION.SDK_INT>20){
//                            ActivityOptions options =
//                                    ActivityOptions.makeSceneTransitionAnimation(getActivity());
//                            startActivity(intent,options.toBundle());

                                fragment = new Fragment_recycler();
                                fragment.setArguments(bundle);
                                // Fragment myFragment = new TaskApprovalFragmentDetails();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                            }else {

                                //startActivity(intent);
                                fragment = new Fragment_recycler();
                                fragment.setArguments(bundle);
                                // Fragment myFragment = new TaskApprovalFragmentDetails();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                            }
                            //End animation
                            //the below line commented because of animation
                            //startActivity(intent); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 1:
                            //otagh koodak
                            bundle.putString("key", "111");// put image data in Intent
                            bundle.putString ("count", "5");
                            //intent.putExtra("count", "275"); // put number of image data in Intent
                            fragment = new Fragment_recycler();
                            fragment.setArguments(bundle);
                            // Fragment myFragment = new TaskApprovalFragmentDetails();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;

                        case 2:
                            //posht e TV
                            bundle.putString("key", "20");// put image data in Intent
                            bundle.putString ("count", "5");
                            //intent.putExtra("count", "275"); // put number of image data in Intent
                            fragment = new Fragment_recycler();
                            fragment.setArguments(bundle);
                            // Fragment myFragment = new TaskApprovalFragmentDetails();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                            break;
                        case 3:
                            //otagh khab
                            bundle.putString("key", "112");// put image data in Intent
                            bundle.putString ("count", "5");
                            //intent.putExtra("count", "275"); // put number of image data in Intent
                            fragment = new Fragment_recycler();
                            fragment.setArguments(bundle);
                            // Fragment myFragment = new TaskApprovalFragmentDetails();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                            break;

                        case 4:
                            //3 boodi
                            bundle.putString("key", "113");// put image data in Intent
                            bundle.putString ("count", "5");
                            //intent.putExtra("count", "275"); // put number of image data in Intent
                            fragment = new Fragment_recycler();
                            fragment.setArguments(bundle);
                            // Fragment myFragment = new TaskApprovalFragmentDetails();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                            break;
                        case 5:
                            //honari
                            bundle.putString("key", "21");// put image data in Intent
                            bundle.putString ("count", "5");
                            //intent.putExtra("count", "275"); // put number of image data in Intent
                            fragment = new Fragment_recycler();
                            fragment.setArguments(bundle);
                            // Fragment myFragment = new TaskApprovalFragmentDetails();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                            break;




                    }
                }
            });

        }}}


