package com.example.qhs.wallpapershopping.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;

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
import android.widget.ImageView;
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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import Model.ListItem;
import Recycler.RecyclerAdapter;
import Ui.SpannableGridLayoutManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.android.volley.toolbox.Volley.newRequestQueue;

//import com.example.qhs.deydigital.com.example.qhs.wallpapershopping.R;


public class Fragment_search extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private List<JSONArray> imageList;
    private RequestQueue queue;
    //public  View view;
    public JSONArray image_series_json;
    public String txt;
    private ProgressBar pgsBar;
    private EditText editText;
    public HurlStack hurlStack;

    private Toolbar toolbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        //Toolbar

        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
        title.setText("جستجو");


        hurlStack = new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(java.net.URL url)
                    throws IOException {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super
                        .createConnection(url);
                try {
                    httpsURLConnection
                            .setSSLSocketFactory(getSSLSocketFactory(getContext()));
                    //httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                    //Log.d("HostnameVerifier****", getHostnameVerifier().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return httpsURLConnection;
            }
        };

        queue = newRequestQueue(getContext(), hurlStack);
        pgsBar = (ProgressBar) view.findViewById(R.id.SearchpBar);

        recyclerView = (RecyclerView) view.findViewById(R.id.searchRecycler);
        firstLoad();

        //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        //NukeSSLCerts.nuke();


        //hide keyboard when activity start
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //**************************************************
       // editText=(EditText) view.findViewById(R.id.EtSearch);

        //  category_id=editText.getText().toString();


        imageList = new ArrayList<JSONArray>();
        image_series_json = new JSONArray();

        ///Recycler
        recyclerView=(RecyclerView) view.findViewById(R.id.searchRecycler);
        //recyclerView.setHasFixedSize(true);
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

        //*******************
        recyclerView.setLayoutManager(gridLayoutManager);
        // SnapHelper snapHelper = new PagerSnapHelper();
        //   snapHelper.attachToRecyclerView(recyclerView);
        listItems=new ArrayList<>();

        // queue = Volley.newRequestQueue(this);


        //
        //adapter=new RecyclerAdapter( this,listItems, image_series_json);
        adapter=new RecyclerAdapter( getContext(),listItems);
        recyclerView.setAdapter(adapter);
        //
        SearchView simpleSearchView = (SearchView) view.findViewById(R.id.search_view); // inititate a search view

        CharSequence query = simpleSearchView.getQuery(); // get the query string currently in the text field

        simpleSearchView.setOnQueryTextListener( this);


//        final Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "yekan/homa.ttf");
        //TextView txtView_title = (TextView) view.findViewById(R.id.txtTitle);
//        txtView_title.setTypeface(face);

//////////////////
//        Button button_search = (Button) view.findViewById(R.id.btn_search);
////        button_search.setTypeface(face);
//        button_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                InputMethodManager inputManager = (InputMethodManager)
//                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
//
//                search();
//            }
//        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("جستجو");
        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
        title.setText("جستجو");




        super.onActivityCreated(savedInstanceState);

    }


    public void search( String query){


        imageList = new ArrayList<JSONArray>();
        image_series_json = new JSONArray();

        ///Recycler
        //recyclerView=(RecyclerView) view.findViewById(R.id.searchRecycler);
        //recyclerView.setHasFixedSize(true);
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

        //*******************
        recyclerView.setLayoutManager(gridLayoutManager);
        // SnapHelper snapHelper = new PagerSnapHelper();
        //   snapHelper.attachToRecyclerView(recyclerView);
        listItems=new ArrayList<>();

        // queue = Volley.newRequestQueue(this);


        //
        //adapter=new RecyclerAdapter( this,listItems, image_series_json);
        adapter=new RecyclerAdapter( getContext(),listItems);
        recyclerView.setAdapter(adapter);
        queue = newRequestQueue(getContext(), hurlStack);
        String txt_search = txt=editText.getText().toString();
        // newMyResponse(URL_products);

        pgsBar.setVisibility(VISIBLE);

       /* final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                pgsBar.setVisibility(view.VISIBLE);
            }
        }, 1000);*/
        // pgsBar.setVisibility(view.GONE);

        //  TextView txtView_search /*= (TextView)findViewById(R.id.txtView_search)*/;
        String url;

        //Log.d("txt**",txt_search);
        String search_key_encode = Uri.encode(query) ;
        url = "https://deydigital.ir/wc-api/v3/products?filter[q]=" +
                // url = "https://deydigital.ir/wp-json/wc/v3/products?filter[q]=" +
                //url="https://deydigital.ir/wp-json/wc/v1/products?filter[q]=" +
                search_key_encode +
                //  "&per_page=100";
                "&page=";


        Log.d("unicode**",url + "***"+search_key_encode);

        for (int j = 0; j < 10 + 1; j++) {

            //final String error= "پاسخی از سایت دریافت نشد.";
            String new_url = url + String.valueOf(j+1);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    new_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        //JSONArray products = response.getJSONArray();

                        JSONArray products = response.getJSONArray("products");
                   /* if(products.length() == 0){
                        pgsBar.setVisibility(view.GONE);
                        Toast.makeText(getApplicationContext(),error ,
                                Toast.LENGTH_LONG).show();
                    }

                    else{*/
                        for (int i = 0; i < products.length(); i++) {

                            // response.getJSONArray()
                            image_series_json = products.getJSONObject(i).getJSONArray("images");
                            //viewDialog.hideDialog();

                            Log.d("hello", "hiiii");
                            //get image urls and save in arraylist
                            imageList.add(image_series_json);

                            ListItem item = new ListItem(
                                    products.getJSONObject(i).getJSONArray("images").
                                            getJSONObject(0).getString("src"),
                                    products.getJSONObject(i).getString("title"),
                                    /*image_series_json*/
                                    products.getJSONObject(i).getString("id"),
                                    products.getJSONObject(i).getString("description"),
                                    products.getJSONObject(i).getJSONArray("images"),
                                    new ArrayList()
                            );

                            pgsBar.setVisibility(GONE);
                            listItems.add(item);
                            adapter.notifyDataSetChanged();
                        }
                        // }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error:", error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", "ck_f3a763d40a0444805b94290bca9390353118c29f",
                            "cs_f0aeb4bdc3fe204e8591a12fa9270caabef2a1f9");
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                    auth.replace("\n", "");
                    params.put("Authorization", auth);
                    return params;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(1000000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //requests.add(jsonArrayRequest);

            queue.add(jsonObjectRequest);
        }
    }
    public void firstLoad(){


        pgsBar.setVisibility(VISIBLE);

       /* final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                pgsBar.setVisibility(view.VISIBLE);
            }
        }, 1000);*/
        // pgsBar.setVisibility(view.GONE);

        //  TextView txtView_search /*= (TextView)findViewById(R.id.txtView_search)*/;

        String url = "https://deydigital.ir/wc-api/v3/products";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    //JSONArray products = response.getJSONArray();

                    JSONArray products = response.getJSONArray("products");
                    for (int i=0; i<products.length();i++){

                        // response.getJSONArray()
                        image_series_json =products.getJSONObject(i).getJSONArray("images");
                        //viewDialog.hideDialog();

                        Log.d("hello","hiiii");
                        //get image urls and save in arraylist
                        imageList.add(image_series_json);

                        ListItem item=new ListItem(
                                products.getJSONObject(i).getJSONArray("images").
                                        getJSONObject(0).getString("src"),
                                products.getJSONObject(i).getString("title"),
                                /*image_series_json*/
                                products.getJSONObject(i).getString("id"),
                                products.getJSONObject(i).getString("description"),
                                products.getJSONObject(i).getJSONArray("images"),
                                new ArrayList()
                        );

                        pgsBar.setVisibility(GONE);
                        listItems.add(item);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error:", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "ck_f3a763d40a0444805b94290bca9390353118c29f",
                        "cs_f0aeb4bdc3fe204e8591a12fa9270caabef2a1f9");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                auth.replace("\n", "");
                params.put("Authorization", auth);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(1000000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //requests.add(jsonArrayRequest);

        queue.add(jsonObjectRequest);
    }


    public SSLSocketFactory getSSLSocketFactory(Context context)
            throws CertificateException, KeyStoreException, IOException,
            NoSuchAlgorithmException, KeyManagementException {

// the certificate file will be stored in \app\src\main\res\raw folder path
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = context.getResources().openRawResource(R.raw.server2);

        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore = KeyStore.getInstance("BKS");

        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();
    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {

        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];

        return new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return originalTrustManager.getAcceptedIssuers();
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                try {
                    if (certs != null && certs.length > 0) {
                        certs[0].checkValidity();
                    } else {
                        originalTrustManager
                                .checkClientTrusted(certs, authType);
                    }
                } catch (CertificateException e) {
                    Log.w("checkClientTrusted", e.toString());
                }
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                try {
                    if (certs != null && certs.length > 0) {
                        certs[0].checkValidity();
                    } else {
                        originalTrustManager
                                .checkServerTrusted(certs, authType);
                    }
                } catch (CertificateException e) {
                    Log.w("checkServerTrusted", e.toString());
                }
            }
        }};
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        search(s);
        return false;
    }
}
