package Recycler;

import android.app.ProgressDialog;
import android.app.SharedElementCallback;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.qhs.wallpapershopping.AuthHelper;
import com.example.qhs.wallpapershopping.Fragments.Fragment_favorite;
import com.example.qhs.wallpapershopping.Fragments.Fragment_home;
import com.example.qhs.wallpapershopping.Fragments.Fragment_search;
import com.example.qhs.wallpapershopping.MainActivity;
import com.example.qhs.wallpapershopping.R;
import com.example.qhs.wallpapershopping.network.Admin;
import com.example.qhs.wallpapershopping.network.NetRequest;


import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


import Model.ListItem;
import Model.RecyclerHorizentalItem;
import Ui.SpannableGridLayoutManager;


import static com.android.volley.toolbox.Volley.newRequestQueue;


public class Fragment_recycler extends Fragment {

    public RecyclerView recyclerView;
    public RecyclerAdapter adapter;
    public List<ListItem> listItems;
    private List<JSONArray> imageList;
    private NetRequest request;
    private ProgressDialog mProgressDialog;
    //log in
    private AuthHelper mAuthHelper;
    private Admin admin;
    private Menu mOptionsMenu;
    private Button profileBtn;
    Animation frombottom;

    //com.example.qhs.deydigital.Json parametr
    private final static String URL_products = "https://mobifytech.ir/wc-api/v3/products?filter[limit] =-1";
    private final static String URL_category = "https://mobifytech.ir/wc-api/v3/products?filter[categories]=";
    private static String URL;
    private final static String URL_category_product = "http://mobifytech.ir/wp-json/wc/v3/products?category=";
    private static String category_id;
    private final static String per_page = "&per_page=";
    private final static String per_page_number = "100";
    private final static String offset = "&offset=";
    private final static String offset_number = "0";
    private RequestQueue queue;
    public View view;
    public JSONArray image_series_json;
    private int response_number = 0;
    private int iteration_number;
    public HurlStack hurlStack;

    private RecyclerView recyclerViewHorizental;
    private RecyclerView.Adapter adapterHorizental;
    private List<RecyclerHorizentalItem> HorizentalItems = new ArrayList<>();

    public ProgressBar pgsBar;
    // public Context context;
    public String URL_complete;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

//        //Splash Screen
//        if (Splashscreen.Splash==0){
//            Intent intent = new Intent(this,Splashscreen.class);
//            startActivity(intent);
//            super.onCreate(savedInstanceState);
//            finish();
//            return;
//        }
//        //End Splash


        Bundle ext = this.getArguments();
        String name_string = ext.getString("name");
        //Toolbar
        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
        title.setText(name_string);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity)getActivity()).onBackPressed();
            }
        });


        frombottom = AnimationUtils.loadAnimation(getContext(), R.anim.frombottom);


        // prepareTransitions();
       // postponeEnterTransition();

        request = new NetRequest(getContext());
        queue = newRequestQueue(getContext());
        admin = Admin.getInstance(getContext());

        mProgressDialog = new ProgressDialog(getContext());
        mAuthHelper = AuthHelper.getInstance(getContext());
        if (mAuthHelper.isLoggedIn()) {
            Log.d("USERNAME: ", "isloggedin");
            // setupView();
        }

        //Horizental RecyclerView
        //image for horizental com.example.qhs.deydigital.Recycler
//        int logos[] = {
//                R.drawable.logo1, R.drawable.logo12, R.drawable.logo3, R.drawable.logo6,
//                R.drawable.logo8,R.drawable.logo5};
        int[] vector = {
                R.drawable.livingroom,
                R.drawable.kids,
                R.drawable.tvroom,
                R.drawable.bedroom,
                R.drawable.threed,
                R.drawable.artistic};
        //text for horizental com.example.qhs.deydigital.Recycler
        String txt[]={
                "سالن پذیرایی",
                "اتاق کودک",
                "پشت Tv",
                "اتاق خواب",
                "سه بعدی",
                "هنری",
        };

        recyclerViewHorizental = (RecyclerView) view.findViewById(R.id.reciclerViewHorizental);

        // add a divider after each item for more clarity
        //recyclerViewHorizental.addItemDecoration(new DividerItemDecoration(
        //      RecyclerActivity.this, LinearLayoutManager.HORIZONTAL));

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewHorizental.setLayoutManager(horizontalLayoutManager);

        for (int i = 0; i < vector.length; i++) {

            RecyclerHorizentalItem HItem = new RecyclerHorizentalItem(vector[i], txt[i]);

            HorizentalItems.add(HItem);
            //  adapterHorizental.notifyDataSetChanged();

        }
        adapterHorizental = new RecyclerViewHorizontalListAdapter(HorizentalItems, getContext());
        recyclerViewHorizental.setAdapter(adapterHorizental);


        //Retrieve Bundle value
        Bundle extras = this.getArguments();
        if (extras == null) {
            category_id = null;
            Log.d("key:", category_id);
        } else {
            category_id = extras.getString("key");
        }

        imageList = new ArrayList<JSONArray>();
        image_series_json = new JSONArray();

        ///com.example.qhs.deydigital.Recycler
        recyclerView = (RecyclerView) view.findViewById(R.id.reciclerViewID);
        recyclerView.setHasFixedSize(true);
        //recyclerView.startAnimation(frombottom);
        recyclerView.startLayoutAnimation();
        //********************
        SpannableGridLayoutManager gridLayoutManager = new
                SpannableGridLayoutManager(new SpannableGridLayoutManager.GridSpanLookup() {
            @Override
            public SpannableGridLayoutManager.SpanInfo getSpanInfo(int position)
            {
                if (position == 0) {
                    return new SpannableGridLayoutManager.SpanInfo(2, 2);
                    //this will count of row and column you want to replace
                } else {
                    return new SpannableGridLayoutManager.SpanInfo(1, 1);
                }
            }
        }, 3, 1f); // 3 is the number of coloumn , how nay to display is 1f

        recyclerView.setLayoutManager(gridLayoutManager);

        //*******************
        // recyclerView.setLayoutManager( new GridLayoutManager(this,3));
        // SnapHelper snapHelper = new PagerSnapHelper();
        // snapHelper.attachToRecyclerView(recyclerView);
        listItems = new ArrayList<>();
        adapter = new RecyclerAdapter(getContext(), listItems);
        recyclerView.setAdapter(adapter);




        category_Response_edited();


        // newMyResponse(URL_products);
        pgsBar = (ProgressBar) view.findViewById(R.id.pBar);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                pgsBar.setVisibility(view.VISIBLE);
            }
        }, 1000);
        // pgsBar.setVisibility(view.GONE);
        return view;
    }

    //anim shared element trans
    /**
     * Prepares the shared element transition to the pager fragment, as well as the other transitions
     * that affect the flow.
     */


//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//
//        Bundle extras = this.getArguments();
//        String name_string = extras.getString("name");
//        //Toolbar
//        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
//        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
//        title.setText(name_string);
//        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((AppCompatActivity)getActivity()).onBackPressed();
//            }
//        });
//        super.onActivityCreated(savedInstanceState);
//    }

    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //return true; // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("localhost", session);
            }
        };
    }


    private void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void category_Response_edited(){

        imageList.clear();
        Bundle extras = this.getArguments();
        String key_string = extras.getString("key");

        request.JsonArrayNetRequest("GET", "wc/v3/products?category="+
                key_string, mCategoryCallback, admin.getAdminAuth());

    }

    private NetRequest.Callback<JSONArray> mCategoryCallback = new NetRequest.Callback<JSONArray>(){

        @Override
        public void onResponse(@NonNull JSONArray response) {
            try {

                //JSONArray products = response.getJSONArray();
                for (int i = 0; i < response.length(); i++) {

                    response_number = response.length();


                    image_series_json = response.getJSONObject(i).getJSONArray("images");
                    //viewDialog.hideDialog();

                    //Log.d("j**response number",String.valueOf(iteration_number) + "***"+String.valueOf(response_number) );
                    //get image urls and save in arraylist
                    imageList.add(image_series_json);
                    ListItem item;
                    Log.d("response*****", response.getJSONObject(i).getString("name"));
                    if (!response.getJSONObject(i).getString("price").equals("")){
                        item = new ListItem(
                                response.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("src"),
                                response.getJSONObject(i).getString("name"),
                                /*image_series_json*/
                                response.getJSONObject(i).getString("id"),
                                response.getJSONObject(i).getString("short_description"),
                                response.getJSONObject(i).getJSONArray("images"),
                                new ArrayList()
                                ,Integer.parseInt(response.getJSONObject(i).getString("price"))
                        );
                    }else {
                        item = new ListItem(
                                response.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("src"),
                                response.getJSONObject(i).getString("name"),
                                /*image_series_json*/
                                response.getJSONObject(i).getString("id"),
                                response.getJSONObject(i).getString("short_description"),
                                response.getJSONObject(i).getJSONArray("images"),
                                new ArrayList()
                                , 0
                        );
                    }


                    for (int u = 0; u < response.getJSONObject(i).getJSONArray("images").length(); u++) {
                        Log.d("imgsrc***", response.getJSONObject(i).getJSONArray("images").getJSONObject(u).getString("src"));
                    }


                    listItems.add(item);
                    adapter.notifyDataSetChanged();
                    pgsBar.setVisibility(view.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String error) {

        }
    };


}
