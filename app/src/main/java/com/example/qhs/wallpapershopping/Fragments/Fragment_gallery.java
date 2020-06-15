package com.example.qhs.wallpapershopping.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.example.qhs.wallpapershopping.AuthHelper;
import com.example.qhs.wallpapershopping.Blur;
import com.example.qhs.wallpapershopping.MainActivity;
import com.example.qhs.wallpapershopping.R;
import com.example.qhs.wallpapershopping.network.CustomJsonRequest;
import com.example.qhs.wallpapershopping.network.NetRequest;
import com.viewpagerindicator.CirclePageIndicator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import Data.DatabaseHandler;
import Gallery.SlidingImage_Adapter;
import Model.ImageModel;
import Model.ListItem;
import static com.android.volley.toolbox.Volley.newRequestQueue;


public class Fragment_gallery extends Fragment {

    private ViewPager mPager;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    private Timer swipeTimer;
    private AuthHelper mAuthHelper;
    private Menu mOptionsMenu;
    private NetRequest request;
    private RequestQueue queue;
    private DatabaseHandler db;
    private String id;
    private Fragment fragment;
    private float density;
    private CirclePageIndicator indicator;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        //Toolbar
        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
        title.setText("گالری");

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity)getActivity()).onBackPressed();
            }
        });

        //constraint layout animation

        ConstraintLayout cc1;
        cc1 = view.findViewById(R.id.constraintLayout_gallery);


        final ConstraintSet constraintSet = new ConstraintSet();
        final Handler handler = new Handler();

        constraintSet.clone(getContext(), R.layout.fragment_gallery_animation);
        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateInterpolator(1.0f));
        transition.setDuration(1200);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms



                TransitionManager.beginDelayedTransition(cc1, transition);
                constraintSet.applyTo(cc1);
            }
        }, 300);
//end constraint layout animation

        //DataBase Define
        db = new DatabaseHandler(getContext());

        //log in
        mAuthHelper = AuthHelper.getInstance(getContext());
        request = new NetRequest(getContext());
        queue = newRequestQueue(getContext());


        imageModelArrayList = new ArrayList<>();
        try {
            imageModelArrayList = populateList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Initialize views
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(getContext(),imageModelArrayList));

        indicator = (CirclePageIndicator)
                view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        //Get values from recyclerAdapter by intent to show description of products
        Bundle bundle = this.getArguments();
        id = bundle.getString("id");
        final String name = bundle.getString("name");
        final String description = bundle.getString("description");
        final int price = bundle.getInt("price");
        final int currentPosition = bundle.getInt("position");

        //Textview of details
        TextView txt_name = (TextView) view.findViewById(R.id.txt1);
        txt_name.setText( name );
        //txt_name.setTypeface(face);

        TextView txt_description = (TextView) view.findViewById(R.id.txt2);
        txt_description.setText(
                "\n" + Html.fromHtml(description) +
                 "\n" + Html.fromHtml(String.valueOf(price))+ " تومان");
        txt_description.setTextColor(Color.parseColor("#000000"));
        //txt_description.setPaintFlags(txt_description.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        //txt_description.setMovementMethod(new ScrollingMovementMethod());
        //txt_description.setTypeface(face);

        TextView txt_id=(TextView)view.findViewById(R.id.txt_productCode);
        String product_code=" کد محصول: " ;
        txt_id.setText(product_code + id);
        txt_id.setTextColor(Color.parseColor("#FF0000"));


        init();
        final ArrayList<String> image_list = bundle.getStringArrayList("imageJsonObj");
        String s="";
        for (int i=1;i<image_list.size(); i=i+1)
        {
            s=s+image_list.get(i)+",";
        }
        //favorite button
        final ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.toggleButton);
        if (db.Exists(id)==true) {
            toggleButton.setChecked(true);
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.favorite_yes));
        } else {
            toggleButton.setChecked(false);
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.favorite_no));
        }

        //favorite button
        final String finalS = s;
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ListItem item = new ListItem(id,name,description, finalS,"true",image_list.size(),price,0,1,Integer.parseInt(mAuthHelper.getIdUser()));
                if (isChecked ){
                    db.addListItem(item);
                    addProduct(Integer.parseInt(id));
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.favorite_yes)); }
                else if (!isChecked ){
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.favorite_no));
                    db.deleteListItem(id);

                    //remove from site
                    request.JsonArrayNetRequest("GET", "wc/v3/wishlist/"+mAuthHelper.getSharekey()+"/get_products",
                            mWishlistProductCallback, null);
                }
            }
        });

        //end favorite button

        ///Shopping Cart
        final Button shoppingBtn = (Button) view.findViewById(R.id.ShoppingBtn);
        shoppingBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                if (mAuthHelper.isLoggedIn()) {
                    ListItem item1 = new ListItem(id, name, description, finalS,"false", image_list.size(), price,1,1,Integer.parseInt(mAuthHelper.getIdUser()));

                    db.addListItem(item1);
                    //addProductToCart(Integer.parseInt(id), 1);
                    shoppingBtn.setEnabled(true);
                    shoppingBtn.setClickable(false);
                    shoppingBtn.setBackgroundColor(R.color.SecondaryLight);
                } else {
                    Blur blur=new Blur();
                    Bitmap map = blur.takeScreenShot(getActivity());
                    Bitmap fast = blur.fastblur(map, 10);
                    fragment = new Dialog();
                    View layout = getActivity().findViewById(R.id.constraintLayout);
                    BitmapDrawable ob = new BitmapDrawable(getResources(), fast);
                    layout.setBackground(ob);
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                            .replace(R.id.frame, fragment).commit();

                }

            }
        });


///SHARE
        ImageView img_share=(ImageView) view.findViewById(R.id.Share);
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                String ShareBody="your body here";
                String ShareSub="your subject here";
                sendIntent.putExtra(Intent.EXTRA_SUBJECT,ShareSub);
                sendIntent.putExtra(Intent.EXTRA_TEXT,ShareBody);
                startActivity(Intent.createChooser(sendIntent,"share using"));
            }
        });

//        //anim shared element trans
//        // Set the current position and add a listener that will update the selection coordinator when
//        // paging the images.
//
//        prepareSharedElementTransition();
//
//        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
//        if (savedInstanceState == null) {
//            postponeEnterTransition();
//        }
//        //end anim shared element trans

        return view;
    }


//    //anim shared element trans
//    /**
//     * Prepares the shared element transition from and back to the grid fragment.
//     */
//    private void prepareSharedElementTransition() {
//        Transition transition =
//                TransitionInflater.from(getContext())
//                        .inflateTransition(R.transition.image_shared_element_transition);
//        setSharedElementEnterTransition(transition);
//
//        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
//       setEnterSharedElementCallback(new SharedElementCallback() {
//           @Override
//           public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
//               // Locate the image view at the primary fragment (the ImageFragment that is currently
//               // visible). To locate the fragment, call instantiateItem with the selection position.
//               // At this stage, the method will simply return the fragment at the position and will
//               // not create a new one.
//               Fragment currentFragment = (Fragment) mPager.getAdapter()
//                       .instantiateItem(mPager, currentPage);
//               View view = currentFragment.getView();
//               if (view == null) {
//                   return;
//               }
//
//               // Map the first shared element name to the child ImageView.
//               sharedElements.put(names.get(0), view.findViewById(R.id.image));
//           }
//       });
//    }
//    //end anim


//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        //Toolbar
//        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
//        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
//        title.setText("گالری");
//
//        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((AppCompatActivity)getActivity()).onBackPressed();
//            }
//        });
//
//        super.onActivityCreated(savedInstanceState);
//    }


    private NetRequest.Callback<JSONArray> mWishlistProductCallback = new NetRequest.Callback<JSONArray>(){

        @Override
        public void onResponse(@NonNull JSONArray response) {
            //ListItem item=listItems.get(getAdapterPosition());
            for (int i=0; i< response.length(); i++){
                try {
                    int productId = response.getJSONObject(i).getInt("product_id");
                    if (productId == Integer.parseInt(id)){
                        db.deleteListItem(id);
                        //notifyDataSetChanged();
                        request.JsonStringNetRequest("POST", "wc/v3/wishlist/remove_product/" + response.getJSONObject(i).getInt("item_id"));
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

    public void addProduct(int productId){

        JSONObject postparams = new JSONObject();
        //Array[] meta = new Array[0];
        try {
            postparams.put("product_id", productId);
            postparams.put("variation_id", 0);
            //postparams.put("meta", meta);
        } catch (JSONException e) {
            Log.d("JSONException_add", e.getMessage());
            e.printStackTrace();
        }
        String url = "http://mobifytech.ir/wp-json/wc/v3/wishlist/" + mAuthHelper.getSharekey() + "/add_product";
        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(Request.Method.POST, url, postparams, getContext(), new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("response_add ", String.valueOf(response.length()));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response == null)
                    Log.d("error_add ", error.getMessage());

            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(1000000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }


    public void addProductToCart(final int productId, final int quantity){

        Log.d("ProductID Quantity ", String.valueOf(productId)+"   "+quantity);

        NetRequest request = new NetRequest(getContext());
        request.JsonObjectNetRequest("POST", "cocart/v1/add-item?product_id=" + productId + "&quantity=" + quantity , mAddProductCallback, null);

    }

    private NetRequest.Callback<JSONObject> mAddProductCallback = new NetRequest.Callback<JSONObject>() {
        @Override
        public void onResponse(@NonNull JSONObject response) {
            try {
                Log.d("Successful, key: ", response.getString("key"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String error) {
        }
    };

    private ArrayList<ImageModel> populateList() throws JSONException {



        ArrayList<String> image_list = this.getArguments().getStringArrayList("imageJsonObj");

        // JsonParser parser = new JsonParser();
        //JSONObject scamDataJsonObject = parser.parse(scamDatas).getAsJsonObject();


        ArrayList<ImageModel> list = new ArrayList<>();
        //add image to list
        for(int i = 0; i < image_list.size(); i++){

            ImageModel imageModel = new ImageModel();
            imageModel.setImage(image_list.get(i));
            list.add(imageModel);
        }

        return list;
    }

    private void init() {
        //add image to ViewPager
        mPager.setAdapter(new SlidingImage_Adapter(getContext(),imageModelArrayList));


        final float density = getResources().getDisplayMetrics().density;


        NUM_PAGES =imageModelArrayList.size();

        //View view = findViewById(android.R.id.content);
        Boolean toggle;
        // Auto start of viewpager
        //if(view.getId() == R.id.)
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }


    @Override
    public void onDestroy() {

        swipeTimer.cancel();
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_signout){
            mAuthHelper.clear();
            Fragment fragment2 = new Fragment_home();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment2);
            fragmentTransaction.addToBackStack("fragment_home");
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.signout_menu, menu);
        mOptionsMenu = menu;
        super.onCreateOptionsMenu(mOptionsMenu, inflater);
    }

    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.action_signout);
        //register.setVisible(false);
        if(mAuthHelper.isLoggedIn())
        {
            register.setVisible(true);
        }
        else
        {
            register.setVisible(false);
        }
        //invalidateOptionsMenu();
    }
    private void updateOptionsMenu() {
        if (mOptionsMenu != null) {
            onPrepareOptionsMenu(mOptionsMenu);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        updateOptionsMenu();
        super.onConfigurationChanged(newConfig);
    }


}
