package com.example.qhs.wallpapershopping.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.Toast;

import com.example.qhs.wallpapershopping.AuthHelper;
import com.example.qhs.wallpapershopping.R;
import com.example.qhs.wallpapershopping.network.NetworkRequest;
import com.google.gson.Gson;

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

        return view;
    }

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
        billing = new Billing(first_name.getText().toString().trim(),last_name.getText().toString().trim(),address.getText().toString().trim(),city, state,
                postcode.getText().toString().trim(), "ایران", email.getText().toString().trim(), phone.getText().toString().trim());


        shipping = new Shipping(first_name.getText().toString().trim(),last_name.getText().toString().trim(),address.getText().toString().trim(),city, state,
                postcode.getText().toString().trim(), "ایران");

        Customer customer = new Customer(email.getText().toString().trim(), first_name.getText().toString().trim(), last_name.getText().toString().trim(), billing, shipping);


//        JSONObject customer = new JSONObject();
        try {
//            customer.put("first_name", first_name.getText().toString().trim());
//            customer.put("last_name", last_name.getText().toString().trim());
//            customer.put("email", email.getText().toString().trim());
//            customer.
//            customer.put("billing", gson.toJson(billing));
//            customer.put("shipping", gson.toJson(shipping));

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

        }

        @Override
        public void onError(String error) {

        }

        @Override
        public Class<Order> type() {
            return Order.class;
        }
    };

}
