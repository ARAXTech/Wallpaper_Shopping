package com.example.qhs.wallpapershopping.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.qhs.wallpapershopping.R;
import com.example.qhs.wallpapershopping.network.Admin;
import com.example.qhs.wallpapershopping.network.NetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.ListItem;
import Recycler.RecyclerAdapter;
import Recycler.RecyclerViewHorizontalListAdapter;
import Ui.SpannableGridLayoutManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.android.volley.toolbox.Volley.newRequestQueue;
import Model.RecyclerHorizentalItem;



public class Fragment_search extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private List<JSONArray> imageList;
    private Admin admin;
    private RequestQueue queue;
    //public  View view;
    public  JSONArray image_series_json;
    private ProgressBar pgsBar;
    private SpannableGridLayoutManager gridLayoutManager;
    private EditText editText;

    //horizental recycler
    private RecyclerView recyclerViewHorizental;
    private RecyclerView.Adapter adapterHorizental;
    private List<RecyclerHorizentalItem> HorizentalItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //Toolbar
        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
        title.setText("جستجو");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity)getActivity()).onBackPressed();
            }
        });

        admin = Admin.getInstance(getContext());

        pgsBar = (ProgressBar) view.findViewById(R.id.SearchpBar);

        recyclerView = (RecyclerView) view.findViewById(R.id.searchRecycler);


        //hide keyboard when activity start
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //**************************************************
       // editText=(EditText) view.findViewById(R.id.EtSearch);
        //  category_id=editText.getText().toString();




        imageList = new ArrayList<JSONArray>();
        image_series_json = new JSONArray();

        //********************
        gridLayoutManager = new
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

        //*******************
        recyclerView.setLayoutManager(gridLayoutManager);
        // SnapHelper snapHelper = new PagerSnapHelper();
        //   snapHelper.attachToRecyclerView(recyclerView);
        listItems=new ArrayList<>();

        adapter=new RecyclerAdapter( getContext(),listItems);
        recyclerView.setAdapter(adapter);

        //Horizental RecyclerView///////////////////////////////////////////////////////////////////////////////
        //image for horizental com.example.qhs.deydigital.Recycler
        int logos[] = {
                R.drawable.logo1, R.drawable.logo12, R.drawable.logo3, R.drawable.logo6,
                R.drawable.logo8,R.drawable.logo5};
        //text for horizental com.example.qhs.deydigital.Recycler
        String txt[]={
                "سالن پذیرایی",
                "اتاق کودک",
                "پشت Tv",
                "اتاق خواب",
                "سه بعدی",
                "هنری",
        };

        recyclerViewHorizental = (RecyclerView) view.findViewById(R.id.searchReciclerViewHorizental);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHorizental.setLayoutManager(horizontalLayoutManager);

        for (int i = 0; i < logos.length; i++) {

            RecyclerHorizentalItem HItem = new RecyclerHorizentalItem(logos[i], txt[i]);

            HorizentalItems.add(HItem);
            //  adapterHorizental.notifyDataSetChanged();

        }
        adapterHorizental = new RecyclerViewHorizontalListAdapter(HorizentalItems, getContext());
        recyclerViewHorizental.setAdapter(adapterHorizental);
        //End Horizental RecyclerView///////////////////////////////////////////////////////////////////////////////


        firstLoad();

        SearchView simpleSearchView = (SearchView) view.findViewById(R.id.search_view); // inititate a search view
//        simpleSearchView.onActionViewExpanded();
//        simpleSearchView.setIconified(true);

        CharSequence query = simpleSearchView.getQuery(); // get the query string currently in the text field

        Log.d("Query", String.valueOf(query));
        simpleSearchView.setOnQueryTextListener( this);

        simpleSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, boolean hasFocus) {
                if (hasFocus) {
                    simpleSearchView.setBackgroundColor(Color.WHITE);
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(view.findFocus(), 0);
                        }
                    }, 200);
                }
            }
        });

//        final Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "yekan/homa.ttf");
        //TextView txtView_title = (TextView) view.findViewById(R.id.txtTitle);
//        txtView_title.setTypeface(face);



        return view;
    }

//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        //Toolbar
//        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
//        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
//        title.setText("جستجو");
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


    public void search( String query){

        imageList = new ArrayList<JSONArray>();
        image_series_json = new JSONArray();


        recyclerView.setLayoutManager(gridLayoutManager);
        // SnapHelper snapHelper = new PagerSnapHelper();
        //   snapHelper.attachToRecyclerView(recyclerView);
        listItems=new ArrayList<>();

        adapter=new RecyclerAdapter( getContext(),listItems);
        recyclerView.setAdapter(adapter);
//        String txt_search = txt=editText.getText().toString();
        // newMyResponse(URL_products);

        pgsBar.setVisibility(VISIBLE);


        String search_key_encode = Uri.encode(query) ;
        String url = "wc/v3/products?search="+
                // url = "https://deydigital.ir/wp-json/wc/v3/products?filter[q]=" +
                search_key_encode ;//+
                //  "&per_page=100";
               // "&page=";


        Log.d("unicode**",url + "***"+search_key_encode);

        NetRequest request = new NetRequest(getContext());
        request.JsonArrayNetRequest("GET", url, mProductCallback, admin.getAdminAuth());

      //  for (int j = 0; j < 10 + 1; j++) {

            //final String error= "پاسخی از سایت دریافت نشد.";
          //  String new_url = url + String.valueOf(j+1);

//                   /* if(products.length() == 0){
//                        pgsBar.setVisibility(view.GONE);
//                        Toast.makeText(getApplicationContext(),error ,
//                                Toast.LENGTH_LONG).show();
//                    }
//                    else

            }
    public void firstLoad(){

        pgsBar.setVisibility(VISIBLE);

        NetRequest request = new NetRequest(getContext());
        request.JsonArrayNetRequest("GET", "wc/v3/products", mProductCallback, admin.getAdminAuth());

//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                String creds = String.format("%s:%s", "ck_f3a763d40a0444805b94290bca9390353118c29f",
//                        "cs_f0aeb4bdc3fe204e8591a12fa9270caabef2a1f9");
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
//                auth.replace("\n", "");
//                params.put("Authorization", auth);
//                return params;
//            }
//        };
    }
    private NetRequest.Callback<JSONArray> mProductCallback = new NetRequest.Callback<JSONArray>() {

        @Override
        public void onResponse(@NonNull JSONArray products) {
            try {

                   // JSONArray products = response.getJSONArray("products");
                    for (int i=0; i<products.length();i++){

                        image_series_json = products.getJSONObject(i).getJSONArray("images");
                        //viewDialog.hideDialog();

                        if (image_series_json.length() > 0) {
                            //get image urls and save in arraylist
                            imageList.add(image_series_json);

                            ListItem item;
                            if (!products.getJSONObject(i).getString("price").equals("")){
                                item=new ListItem(
                                        products.getJSONObject(i).getJSONArray("images").
                                                getJSONObject(0).getString("src"),
                                        products.getJSONObject(i).getString("name"),
                                        /*image_series_json*/
                                        products.getJSONObject(i).getString("id"),
                                        products.getJSONObject(i).getString("description"),
                                        products.getJSONObject(i).getJSONArray("images"),
                                        new ArrayList(),
                                        Integer.parseInt(products.getJSONObject(i).getString("price"))
                                );
                            }else {
                                item=new ListItem(
                                        products.getJSONObject(i).getJSONArray("images").
                                                getJSONObject(0).getString("src"),
                                        products.getJSONObject(i).getString("name"),
                                        /*image_series_json*/
                                        products.getJSONObject(i).getString("id"),
                                        products.getJSONObject(i).getString("description"),
                                        products.getJSONObject(i).getJSONArray("images"),
                                        new ArrayList(),
                                        0
                                );
                            }

                            listItems.add(item);
                            adapter.notifyDataSetChanged();
                            pgsBar.setVisibility(GONE);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

        @Override
        public void onError(String error) {

        }
    };


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
//                InputMethodManager inputManager = (InputMethodManager)
//                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);

                search(s);
        return false;
    }
}
