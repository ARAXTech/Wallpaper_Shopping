package com.example.qhs.wallpapershopping.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qhs.wallpapershopping.AuthHelper;
import com.example.qhs.wallpapershopping.R;
import com.example.qhs.wallpapershopping.network.NetworkRequest;
import com.google.gson.Gson;
import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Model.Billing;
import Model.Customer;
import Model.Order;
import Model.Shipping;


public class Fragment_billing extends Fragment implements AdapterView.OnItemSelectedListener{
       private EditText first_name;
       private EditText last_name;
       private EditText address;
       private Spinner city_spinner;
       private Spinner state_spinner;
       private String city;
       private String state;
       private EditText postcode;
       private EditText email;
       private EditText phone;
       private Button payment;
       private String[][] cities ={{"بومهن","رودهن","تهران"},{"رامسر","بابل","ساری"}};
       String[] State={"تهران","مازندران"};
       private int index = -1;
       private AuthHelper mAuthHelper;
       private NetworkRequest request;
       private Billing billing;
       private Shipping shipping;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_billing, container, false);
        //Toolbar
        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
        title.setText("جزییات حساب");

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity)getActivity()).onBackPressed();
            }
        });
        mAuthHelper = AuthHelper.getInstance(getContext());
        request = new NetworkRequest();

        first_name = (EditText) view.findViewById(R.id.first_name);
        last_name = (EditText) view.findViewById(R.id.last_name);
        address = (EditText) view.findViewById(R.id.address);
        city_spinner = (Spinner) view.findViewById(R.id.city);
        state_spinner = (Spinner) view.findViewById(R.id.state);
        postcode = (EditText) view.findViewById(R.id.passcode);
        email = (EditText) view.findViewById(R.id.email);
        phone = (EditText) view.findViewById(R.id.phone);
        payment = (Button) view.findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInfo();
            }
        });

//        state_spinner.setFocusableInTouchMode(true);
//        state_spinner.setFocusable(true);

        ArrayAdapter aa = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, State);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_spinner.setAdapter(aa);


        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int position, long l) {
                aa.notifyDataSetChanged();
                String data= state_spinner.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), data, Toast.LENGTH_LONG).show();
                index = position;
                switch(position){
                    case 0:
                        state = "تهران";
                        ArrayAdapter aa1 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, cities[position]);
                        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        city_spinner.setAdapter(aa1);
                        break;
                    case 1:
                        state = "مازندران";
                        ArrayAdapter aa2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, cities[position]);
                        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        city_spinner.setAdapter(aa2);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {
            }
        });
        city_spinner.setOnItemSelectedListener(this);


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

        return view;
    }

    public String getFirst_name() {
        return first_name.getText().toString().trim();
    }

    public String getLast_name() {
        return last_name.getText().toString().trim();
    }

    public String getAddress() {
        return address.getText().toString().trim();
    }

    public String getPostcode() {
        return postcode.getText().toString().trim();
    }

    public String getEmail() {
        return email.getText().toString().trim();
    }

    public String getPhone() {
        return phone.getText().toString().trim();
    }
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        //Toolbar
//        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
//        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
//        title.setText("جزئیات خرید");
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


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView <?> arg0, View arg1, int position, long id) {
       // state_spinner.getpositio
        city = cities[index][position];
        Toast.makeText(getContext(), city, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

    }

    public void addInfo (){

        if (getFirst_name().matches("") || getLast_name().matches("") || getAddress().matches("") || getPostcode().matches("")
                || getEmail().matches("") || getPhone().matches("")) {
            Toast.makeText(getContext(), R.string.toast_no_empty_field, Toast.LENGTH_SHORT).show();
            return;
        }

        billing = new Billing(getFirst_name(), getLast_name(), getAddress(), city, state, getPostcode(), "ایران", getEmail(), getPhone());

        shipping = new Shipping(getFirst_name(), getLast_name(), getAddress(), city, state, getPostcode(), "ایران");

        Customer customer = new Customer(getEmail(), getFirst_name(), getLast_name(), billing, shipping);

        try {

            request.customerPostRequest("http://mobifytech.ir/wp-json/wc/v3/customers/" + mAuthHelper.getIdUser(), customer, mCustomerCallback);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private NetworkRequest.Callback<Customer> mCustomerCallback = new NetworkRequest.Callback<Customer>() {
        @Override
        public void onResponse(@NonNull Customer response) {

            NetworkRequest request2 = new NetworkRequest();
            try {
                Order order = new Order("pending", Integer.parseInt(mAuthHelper.getIdUser()), billing, shipping, Fragment_Shopping.getInstance().getLineItems());
                request2.orderPostRequest("http://mobifytech.ir/wp-json/wc/v3/orders", order, mOrderCallback);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String error) {

        }

        @Override
        public Class<Customer> type() {
            return Customer.class;
        }
    };


    private NetworkRequest.Callback<Order> mOrderCallback = new NetworkRequest.Callback<Order>() {
        @Override
        public void onResponse(@NonNull Order response) {
            Toast.makeText(getContext(), response.getId()+" ", Toast.LENGTH_LONG ).show();
            myPayment(Integer.parseInt(response.getTotal()));


        }

        @Override
        public void onError(String error) {

        }

        @Override
        public Class<Order> type() {
            return Order.class;
        }
    };

    private void myPayment(int totalPrice){

        ZarinPal purchase = ZarinPal.getPurchase(getContext());

        PaymentRequest payment = ZarinPal.getPaymentRequest();


        payment.setMerchantID("71c705f8-bd37-11e6-aa0c-000c295eb8fc");
        payment.setAmount(totalPrice);
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

}
