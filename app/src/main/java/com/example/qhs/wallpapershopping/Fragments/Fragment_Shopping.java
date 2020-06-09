package com.example.qhs.wallpapershopping.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
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
import com.example.qhs.wallpapershopping.RecyclerItemClickListener;
import com.example.qhs.wallpapershopping.ShoppingAdapter;
import com.example.qhs.wallpapershopping.network.Admin;
import com.example.qhs.wallpapershopping.network.NetRequest;
import com.example.qhs.wallpapershopping.network.NetworkRequest;
import com.example.qhs.wallpapershopping.network.Token;
import com.google.gson.Gson;
import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import org.json.JSONArray;
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
import Model.Billing;
import Model.CartItem;
import Model.ListItem;
import Model.Order;
import Model.Shipping;


public class Fragment_Shopping extends Fragment implements ShoppingAdapter.ItemCallback, View.OnClickListener {

    private static Fragment_Shopping instance = null;
    private RecyclerView recyclerView;
    private ShoppingAdapter adapter;
    private List<ListItem> listItems;
    public  TextView totalPrice;
    private int sum=0;
    private AuthHelper mAuthHelper;
    private Admin admin;
    private DatabaseHandler db;
    private Integer[][] shoppingProductId;
    private int num;
    private CardView cardN;
    private List<CartItem> line_items;
    private NetRequest request;
    private int orderId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    public static Fragment_Shopping getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        //Toolbar

        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
        title.setText("سبد خرید");

        db = new DatabaseHandler(getContext());
        num = db.getShoppingItemCount();

        mAuthHelper = AuthHelper.getInstance(getContext());
        admin = Admin.getInstance(getContext());
//        Button profileBtn=(Button)findViewById(R.id.ProfileBtn);
//        profileBtn.setVisibility(View.GONE);
        request = new NetRequest(getContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.ShoppingRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listItems = new ArrayList<>();
        ///cardview
        cardN=(CardView) view.findViewById(R.id.cardN);
        /// Spinner



        //db.deleteAll();
        listItems = db.getAllShoppingItem();
        adapter = new ShoppingAdapter(getContext(),listItems,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        totalPrice=(TextView)view.findViewById(R.id.totalPrice);

        line_items = new ArrayList<CartItem>();
        if(num==0){
            cardN.setVisibility(View.GONE);
        }
        if(num>0) {
            cardN.setVisibility(View.VISIBLE);
            for (int i = 0; i < num; i++) {

                sum = sum + listItems.get(i).getPrice() * listItems.get(i).getCount_shop();

                CartItem cartItem = new CartItem();
                cartItem.setProductId(Integer.parseInt(listItems.get(i).getId()));
                cartItem.setName(listItems.get(i).getName());
                cartItem.setQuantity(listItems.get(i).getCount_shop());
                cartItem.setTotal(String.valueOf(listItems.get(i).getPrice()*listItems.get(i).getCount_shop()));
                line_items.add(cartItem);
            }
            totalPrice.setText(sum + " تومان");
            Log.d("line_itemss", line_items.toString());
        }



        // zarinpal Payment
        Uri data = getActivity().getIntent().getData();
        if (data != null) {

            ZarinPal.getPurchase(getContext()).verificationPayment(data, new OnCallbackVerificationPaymentListener() {
                @Override
                public void onCallbackResultVerificationPayment(boolean isPaymentSuccess, String refID, PaymentRequest paymentRequest) {
                    if (isPaymentSuccess) {
                        NetRequest netRequest = new NetRequest(getContext());
                        netRequest.JsonObjectNetRequest("PUT", "wc/v3/orders/"+orderId+"?status=completed", null, admin.getAdminAuth());
                    }
                    Log.i("TAG", "onCallbackResultVerificationPayment: " + refID);
                }
            });
        }

        Button btnPay = (Button) view.findViewById(R.id.totelPriceBtn);
        btnPay.setOnClickListener(this);

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
        payment.setAmount(sum);
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
    }

    public void addProduct(final int productId, final int quantity){

        Log.d("ProductID Quantity ", String.valueOf(productId)+"   "+quantity);


        request.JsonObjectNetRequest("POST", "cocart/v1/add-item?product_id=" + productId + "&quantity=" + quantity , mAddProductCallback, null);

    }


    private NetRequest.Callback<JSONObject> mAddProductCallback = new NetRequest.Callback<JSONObject>() {
        @Override
        public void onResponse(@NonNull JSONObject response) {
            try {

                Log.d("KEYYY ", response.getString("key"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String error) {

            //   Gson g = new Gson();
            // ErrorHandler errorHandler = g.fromJson(error, ErrorHandler.class);

            //Log.d("errorHandler ", errorHandler.getCode());
        }
    };
    @Override
     public void TotalPrice(){
                    sum=0;
                    if(listItems.size()==0){
                        cardN.setVisibility(View.GONE);
                    }
                    if(listItems.size()>0) {
                        cardN.setVisibility(View.VISIBLE);
                        for (int i = 0; i < listItems.size(); i++) {

                            sum = sum + listItems.get(i).getPrice() * listItems.get(i).getCount_shop();

                        }
                        totalPrice.setText(String.valueOf(sum) + " تومان");
                    }
     }


    @Override
    public void onClick(View view) {
        NetRequest request = new NetRequest(getContext());
        request.JsonObjectNetRequest("GET", "wc/v3/customers/"+mAuthHelper.getIdUser(), mCustomerCallback, admin.getAdminAuth());


    }

    private final NetRequest.Callback<JSONObject> mCustomerCallback = new NetRequest.Callback<JSONObject>() {

        @Override
        public void onResponse(@NonNull JSONObject response) {
            Gson gson = new Gson();
            try {
                Billing billing = gson.fromJson(response.getJSONObject("billing").toString(), Billing.class);
                Shipping shipping = gson.fromJson(response.getJSONObject("shipping").toString(), Shipping.class);

                Log.d("BILLING ", response.getJSONObject("billing").toString());

                if (!billing.getFirstName().equals("") & !billing.getLastName().equals("") & !billing.getAddress1().equals("") & !billing.getCity().equals("")
                        & !billing.getEmail().equals("") & !billing.getPhone().equals("") & !billing.getPostcode().equals("") & !billing.getState().equals("")) {// اگر از طرف سایت اطالعات وارد شده بود، برو به پرداخت

                    Order order = new Order("pending", Integer.parseInt(mAuthHelper.getIdUser()), billing, shipping, line_items);

                    Log.d("ORDER ", order.getLineItems().toString());
                    NetworkRequest request = new NetworkRequest();
                    request.orderPostRequest("http://mobifytech.ir/wp-json/wc/v3/orders", order, mOrderCallback);


                    myPayment();

                } else {// وگرنه صفحه وارد کردن اطلاعات رو براش بیار
                    Fragment fragment = new Fragment_billing();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commit();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String error) {

        }
    };

    private NetworkRequest.Callback<Order> mOrderCallback = new NetworkRequest.Callback<Order>() {
        @Override
        public void onResponse(@NonNull Order response) {
            orderId = response.getId();
            Toast.makeText(getContext(), response.getId()+" ", Toast.LENGTH_LONG ).show();

            myPayment();
        }

        @Override
        public void onError(String error) {

        }

        @Override
        public Class<Order> type() {
            return Order.class;
        }
    };

    public List<CartItem> getLineItems(){
        return line_items;
    }
}