package com.example.qhs.wallpapershopping.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qhs.wallpapershopping.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.qhs.wallpapershopping.AuthHelper;
import com.example.qhs.wallpapershopping.MainActivity;
import com.example.qhs.wallpapershopping.R;
import com.example.qhs.wallpapershopping.ShoppingAdapter;
import com.example.qhs.wallpapershopping.network.NetRequest;
import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import Data.DatabaseHandler;
import Model.ListItem;


public class Fragment_Shopping extends Fragment {

    private RecyclerView recyclerView;
    private ShoppingAdapter adapter;
    private List<ListItem> listItems;
    private TextView totalPrice;
    private int sum=0;
    private AuthHelper mAuthHelper;
    private Menu mOptionsMenu;
    private DatabaseHandler db;
    private Integer[][] shoppingProductId;
    private int num;
    private int quantity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        db = new DatabaseHandler(getContext());

        mAuthHelper = AuthHelper.getInstance(getContext());
//        Button profileBtn=(Button)findViewById(R.id.ProfileBtn);
//        profileBtn.setVisibility(View.GONE);


        recyclerView = (RecyclerView) view.findViewById(R.id.ShoppingRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listItems = new ArrayList<>();
        /// Spinner


        //db.deleteAll();
        listItems = db.getAllShoppingItem();
        num = db.getShoppingItemCount();

        ListItem[] myArray = listItems.toArray(new ListItem[listItems.size()]);

        shoppingProductId = new Integer[num][3];
        for (int i = 0; i < num; i++) {
            shoppingProductId[i][0] = Integer.parseInt(myArray[i].getId());
            shoppingProductId[i][1] = -1;
            shoppingProductId[i][2] = myArray[i].getCount();
        }

        //displayArray();


        // sort the array on item id(first column)
        Arrays.sort(shoppingProductId, new Comparator<Integer[]>() {
            @Override
            //arguments to this method represent the arrays to be sorted
            public int compare(Integer[] o1, Integer[] o2) {
                //get the item ids which are at index 0 of the array
                Integer itemIdOne = o1[0];
                Integer itemIdTwo = o2[0];
                // sort on item id
                return itemIdOne.compareTo(itemIdTwo);
            }
        });
        //displayArray();

        adapter = new ShoppingAdapter(getContext(),listItems);


        for (int i=0; i <num; i++) {

            sum=sum+ listItems.get(i).getPrice()*listItems.get(i).getCount();

        }

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        totalPrice = (TextView) view.findViewById(R.id.totalPrice);
        totalPrice.setText(String.valueOf(sum)+"تومان");


        //Shopping rest api
        doGetShopping();

        // zarinpal Payment
        Uri data = getActivity().getIntent().getData();
        if (data != null) {

            ZarinPal.getPurchase(getContext()).verificationPayment(data, new OnCallbackVerificationPaymentListener() {
                @Override
                public void onCallbackResultVerificationPayment(boolean isPaymentSuccess, String refID, PaymentRequest paymentRequest) {
                    Log.i("TAG", "onCallbackResultVerificationPayment: " + refID);
                }
            });
        }

        Button btnPay = (Button) view.findViewById(R.id.totelPriceBtn);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myPayment();

            }
        });

        return view;
    }

    private void displayArray() {
        System.out.println("-------------------------------------");
        System.out.println("Item id\t\tQuantity");
        for (int i = 0; i<shoppingProductId.length; i++) {
            Integer[] itemRecord = shoppingProductId[i];
            System.out.println(itemRecord[0] + "\t\t" + itemRecord[2]);
        }
        System.out.println("-------------------------------------");
    }

    private void myPayment(){

        ZarinPal purchase = ZarinPal.getPurchase(getContext());

        PaymentRequest payment = ZarinPal.getPaymentRequest();


        payment.setMerchantID("71c705f8-bd37-11e6-aa0c-000c295eb8fc");
        payment.setAmount(100);
        payment.setDescription("In App Purchase Test SDK");
        payment.setCallbackURL("app://zarinpalpayment"); // scheme://host in manifest
        payment.setMobile("09355106005");
        payment.setEmail("imannamix@gmail.com");

        purchase.startPayment(payment, new OnCallbackRequestPaymentListener() {
            @Override
            public void onCallbackResultPaymentRequest(int status, String authority, Uri paymentGatewayUri, Intent intent) {
                if (status == 100){
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(),"خطا در ایجاد درخواست پرداخت", Toast.LENGTH_LONG).show();
                }
            }
        });
//
//        ZarinPal.getPurchase(getApplicationContext()).startPayment(payment, new OnCallbackRequestPaymentListener() {
//            @Override
//            public void onCallbackResultPaymentRequest(int status, String authority, Uri paymentGatewayUri, Intent intent) {
//
//                startActivity(intent);
//            }
//        });

    }

    private void doGetShopping() {
        NetRequest request = new NetRequest(getContext());
        request.JsonObjectNetRequest("GET", "cocart/v1/get-cart", mShoppingProductCallback, null);

    }


    private NetRequest.Callback<JSONObject> mShoppingProductCallback = new NetRequest.Callback<JSONObject>(){

        @Override
        public void onResponse(@NonNull JSONObject response) {
            Iterator<String> keys = response.keys();

            //copy first column to a new array for binarySearch
            Integer[] arrayIdx = new Integer[num];
            for (int i=0; i<num; i++){
                arrayIdx[i] = shoppingProductId[i][0];
            }

            while(keys.hasNext()) {
                String key = keys.next();
                try {
                    if (response.get(key) instanceof JSONObject) {
                        Log.d("SHOPPING ", key);

                        int productId = response.getJSONObject(key).getInt("product_id");
                        quantity = response.getJSONObject(key).getInt("quantity");



                        int idx = Arrays.binarySearch(arrayIdx, productId);
                        if (idx < 0){
                            //get product from site
                            Log.d("getProduct_WishID ", String.valueOf(productId));
                            getProduct(productId);
                        }
                        else{
                            shoppingProductId[idx][1] = 1;
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < num; i++) {
                if (shoppingProductId[i][1] == -1) {
                    Log.d("addProduct_index ", String.valueOf(shoppingProductId[i][0]));
                    addProduct(shoppingProductId[i][0], shoppingProductId[i][2]);
                }
            }

        }

        @Override
        public void onError(String error) {
            Log.d("Server Error ", error);

        }
    };

    public void getProduct(int id){
        NetRequest request = new NetRequest(getContext());
        request.JsonObjectNetRequest("GET", "wc/v3/products/" + id, mProductCallback, null);

    }

    private NetRequest.Callback<JSONObject> mProductCallback = new NetRequest.Callback<JSONObject>() {
        @Override
        public void onResponse(@NonNull JSONObject response) {
            try {
                ListItem item = new ListItem(
                        response.getString("id"),
                        response.getString("name"),
                        response.getString("short_description"),
                        response.getJSONArray("images").getJSONObject(0).getString("src"),
                        "false",
                        response.getJSONArray("images").length(),
                        Integer.parseInt(response.getString("price")),
                        quantity
                );

                db.addListItem(item);
                //db.deleteListItem(response.getString("id"));
                listItems.add(item);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d("JSONException_get", e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String error) {
        }
    };

    public void addProduct(final int productId, final int quantity){

        Log.d("ProductID Quantity ", String.valueOf(productId)+quantity);

//        final JSONObject postparams = new JSONObject();
//
//        try {
//            postparams.put("product_id", productId);
//            postparams.put("quantity", quantity);
//
//        } catch (JSONException e) {
//            Log.d("JSONException_add", e.getMessage());
//            e.printStackTrace();
//        }
//        NetRequest request = new NetRequest(this);
//        request.JsonObjectNetRequest("POST", "cocart/v1/add-item" , mAddProductCallback, postparams);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://mobifytech.ir/wp-json/cocart/v1/add-item";


        JsonObjectRequest strRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new com.android.volley.Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + mAuthHelper.getIdToken());
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("product_id", String.valueOf(productId));
                params.put("quantity", String.valueOf(quantity));
                return params;
            }
        };
        final int socketTimeout = 100000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strRequest.setRetryPolicy(policy);

        queue.add(strRequest);

    }

    private NetRequest.Callback<JSONObject> mAddProductCallback = new NetRequest.Callback<JSONObject>() {
        @Override
        public void onResponse(@NonNull JSONObject response) {
        }

        @Override
        public void onError(String error) {
        }
    };



}
