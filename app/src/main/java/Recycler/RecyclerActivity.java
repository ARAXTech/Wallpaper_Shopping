package Recycler;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.qhs.deydigital.MainActivity;
import com.example.qhs.deydigital.R;
import Ui.SpannableGridLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
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
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import About.AboutUs;
import Model.ListItem;
import Model.RecyclerHorizentalItem;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private List<JSONArray> imageList;
    //Json parametr
    private final static String URL_products = "https://deydigital.ir/wc-api/v3/products?filter[limit] =-1";
    private final static String URL_category = "https://deydigital.ir/wc-api/v3/products?filter[categories]=";
    private static String URL;
    private final static String URL_category_product = "https://deydigital.ir/wp-json/wc/v1/products?category=";
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
    public Context context;
    public String URL_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);


//Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Typeface face = Typeface.createFromAsset(getAssets(), "fonts/homa.ttf");
        TextView txtView_title = (TextView) findViewById(R.id.txtTitle);
        txtView_title.setTypeface(face);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        // NukeSSLCerts.nuke();

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
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
            }
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
            }
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_SMALL) {
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
            }
            iconView.setLayoutParams(layoutParams);
        }
        ;
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleBottomNavigationItemSelected(item);
                return true;
            }
        });
        //
        /*int logos[] = {R.drawable.logo1, R.drawable.logo2, R.drawable.logo3, R.drawable.logo4,
                R.drawable.logo5, R.drawable.logo6, R.drawable.logo7, R.drawable.logo8,
                R.drawable.logo9,R.drawable.logo10,R.drawable.logo11 };
        String txt[]={"پوستردیواری","اتاق","پشت Tv","گل تزئینی","فضاهای ایستاده","هنری","طبیعت",
                "اماکن و ساختمان ها","سه بعدی","پوسترسقفی","نمونه های اجرا شده"};*/
        int logos[] = {
                R.drawable.logo1, R.drawable.logo2, R.drawable.logo3, R.drawable.logo4,
                R.drawable.logo5, R.drawable.logo6, R.drawable.logo7, R.drawable.logo8,
                R.drawable.logo9, R.drawable.logo10, R.drawable.logo11, R.drawable.logo12,
                R.drawable.logo13};
        //String txt[]={"پوستردیواری","اتاق","پشت Tv","گل تزئینی","فضاهای ایستاده","هنری","طبیعت","اماکن و ساختمان ها",
        //    "سه بعدی","پوسترسقفی","نمونه های اجرا شده"};
        String txt[] = {
                "سالن پذیرایی",
                "اتاق",
                "پشت Tv",
                "گل تزئینی",
                "فضاهای ایستاده",
                "هنری",
                "طبیعت",
                "اماکن",
                "سه بعدی",
                "پوسترسقفی",
                "نمونه های اجرا شده",
                "اتاق کودک",
                "وسیله نقلیه"};

        recyclerViewHorizental = (RecyclerView) findViewById(R.id.reciclerViewHorizental);

        // add a divider after each item for more clarity
        //recyclerViewHorizental.addItemDecoration(new DividerItemDecoration(
        //      RecyclerActivity.this, LinearLayoutManager.HORIZONTAL));

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(
                RecyclerActivity.this, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewHorizental.setLayoutManager(horizontalLayoutManager);

        for (int i = 0; i < logos.length; i++) {

            RecyclerHorizentalItem HItem = new RecyclerHorizentalItem(logos[i], txt[i]);

            HorizentalItems.add(HItem);
            //  adapterHorizental.notifyDataSetChanged();

        }
        adapterHorizental = new RecyclerViewHorizontalListAdapter(HorizentalItems, getApplicationContext());
        recyclerViewHorizental.setAdapter(adapterHorizental);


        //Retrieve Bundle value
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            category_id = null;
            Log.d("key:", category_id);
        } else {
            category_id = extras.getString("key");
        }

        URL = URL_category_product + category_id + per_page;
        Log.d("URL:", URL);

        imageList = new ArrayList<JSONArray>();
        image_series_json = new JSONArray();

        ///Recycler
        recyclerView = (RecyclerView) findViewById(R.id.reciclerViewID);
        //recyclerView.setHasFixedSize(true);
        //********************
        SpannableGridLayoutManager gridLayoutManager = new
                SpannableGridLayoutManager(new SpannableGridLayoutManager.GridSpanLookup() {
            @Override
            public SpannableGridLayoutManager.SpanInfo getSpanInfo(int position) {
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
        listItems = new ArrayList<>();

        final Context context = this;

        //
        //adapter=new RecyclerAdapter( this,listItems, image_series_json);
        adapter = new RecyclerAdapter(this, listItems);
        recyclerView.setAdapter(adapter);
        //
        category_Response_edited(URL);
        // newMyResponse(URL_products);
        pgsBar = (ProgressBar) findViewById(R.id.pBar);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                pgsBar.setVisibility(view.VISIBLE);
            }
        }, 1000);
        // pgsBar.setVisibility(view.GONE);

    }

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

    public void category_Response_edited(String url) {

        Bundle extras = getIntent().getExtras();
        String count_string = extras.getString("count");
        int count_int = Integer.valueOf(count_string);


        JsonArrayRequest jsonArrayRequest;
        String offset_number_e = offset_number;
        String per_page_number_e = per_page_number;

        int page_number = 1;
        //count_int = 100;

        for (int j = 0; j < (count_int / 100) + 1; j++) {

          /*  if( j == count_int / 100 ){
                if( response_number == 100)
                    j = j-1;
            }*/
            //String URL_complete = url + per_page_number_e + offset + offset_number_e;
            URL_complete = url + per_page_number_e + "&page=" + String.valueOf(page_number++);

            offset_number_e = per_page_number_e + offset_number_e;
            per_page_number_e = "100";

            // Log.d("j",String.valueOf(j));
            // iteration_number=j;
            //URL myURL = new URL(URL_complete);


            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                    URL_complete, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {

                        //JSONArray products = response.getJSONArray();
                        for (int i = 0; i < response.length(); i++) {

                            response_number = response.length();


                            image_series_json = response.getJSONObject(i).getJSONArray("images");
                            //viewDialog.hideDialog();

                            //Log.d("j**response number",String.valueOf(iteration_number) + "***"+String.valueOf(response_number) );
                            //get image urls and save in arraylist
                            imageList.add(image_series_json);

                            Log.d("response*****", response.getJSONObject(i).getString("name"));
                            ListItem item = new ListItem(
                                    response.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("src"),
                                    response.getJSONObject(i).getString("name"),
                                    /*image_series_json*/
                                    response.getJSONObject(i).getString("id"),
                                    response.getJSONObject(i).getString("short_description"),
                                    response.getJSONObject(i).getJSONArray("images"),
                                    new ArrayList()
                            );

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
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error:", error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", "ck_f3a763d40a0444805b94290bca9390353118c29f", "cs_f0aeb4bdc3fe204e8591a12fa9270caabef2a1f9");
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                    auth.replace("\n", "");
                    params.put("Authorization", auth);
                    return params;
                }
            };


            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(1000000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //requests.add(jsonArrayRequest);
            queue.add(jsonArrayRequest);

        }


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
                Intent intent2 = new Intent(this, Search.class);
                startActivity(intent2); // start Intent
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

}
