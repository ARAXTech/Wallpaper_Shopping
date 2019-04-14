package Recycler;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
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
import com.example.qhs.deydigital.MainActivity;
import com.example.qhs.deydigital.R;
import Ui.SpannableGridLayoutManager;

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

import About.AboutUs;
import Model.ListItem;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class Search extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private List<JSONArray> imageList;
    private RequestQueue queue;
    public  View view;
    public  JSONArray image_series_json;
    public String txt;
    private ProgressBar pgsBar;
    private EditText editText;
    public HurlStack hurlStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        hurlStack = new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(java.net.URL url)
                    throws IOException {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super
                        .createConnection(url);
                try {
                    httpsURLConnection
                            .setSSLSocketFactory(getSSLSocketFactory(getApplicationContext()));
                    //httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                    //Log.d("HostnameVerifier****", getHostnameVerifier().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return httpsURLConnection;
            }
        };

        queue = newRequestQueue(this, hurlStack);
        firstLoad();

        //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Typeface face=Typeface.createFromAsset(getAssets(),"fonts/homa.ttf");
        TextView txtView_title = (TextView)findViewById(R.id.txtTitle);
        txtView_title.setTypeface(face);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        //NukeSSLCerts.nuke();


        //hide keyboard when activity start
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Navigation

        BottomNavigationView bottomNavigation =
                (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics);
            }
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_LARGE) {
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);}
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);}
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_SMALL) {
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
            }
            iconView.setLayoutParams(layoutParams);
        };
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleBottomNavigationItemSelected(item);
                return true;
            }
        });

        //**************************************************
        editText=(EditText)findViewById(R.id.EtSearch);

        //  category_id=editText.getText().toString();


        imageList = new ArrayList<JSONArray>();
        image_series_json = new JSONArray();

        ///Recycler
        recyclerView=(RecyclerView) findViewById(R.id.searchRecycler);
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
        adapter=new RecyclerAdapter( this,listItems);
        recyclerView.setAdapter(adapter);
        //
//////////////////
        Button button_search = (Button)findViewById(R.id.btn_search);
        button_search.setTypeface(face);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                search();
            }
        });
    }


    private void handleBottomNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent); // start Intent
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.AboutUs:
                Intent intent1 = new Intent(this, AboutUs.class);
                startActivity(intent1); // start Intent
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.Search:
                Intent intent2 = new Intent(this,Search.class);
                startActivity(intent2); // start Intent
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }


    public void search(){

        imageList = new ArrayList<JSONArray>();
        image_series_json = new JSONArray();

        ///Recycler
        recyclerView=(RecyclerView) findViewById(R.id.searchRecycler);
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
        adapter=new RecyclerAdapter( this,listItems);
        recyclerView.setAdapter(adapter);
        queue = newRequestQueue(this, hurlStack);
        String txt_search = txt=editText.getText().toString();
        // newMyResponse(URL_products);
        pgsBar = (ProgressBar) findViewById(R.id.SearchpBar);
        pgsBar.setVisibility(view.VISIBLE);

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

        Log.d("txt**",txt_search);
        String search_key_encode = Uri.encode(txt_search) ;
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

                            pgsBar.setVisibility(view.GONE);
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

        pgsBar = (ProgressBar) findViewById(R.id.SearchpBar);
        pgsBar.setVisibility(view.VISIBLE);

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

                            pgsBar.setVisibility(view.GONE);
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
}
