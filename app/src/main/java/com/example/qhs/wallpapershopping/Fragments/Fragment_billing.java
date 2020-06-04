package com.example.qhs.wallpapershopping.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.qhs.wallpapershopping.R;

import java.util.ArrayList;
import java.util.List;

import Model.Billing;
import Model.Shipping;


public class Fragment_billing extends Fragment  implements AdapterView.OnItemSelectedListener {
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
       private String[][] cities ={{"بومهن","رودهن","تهران"},{"رامسر","بابل","ساری"}};
      // String[] City_Tehran={"بومهن","رودهن","تهران"};
       // String[] City_Mazandaran={"رامسر","بابل","ساری"};
       String[] State={"تهران","مازندران"};
       int index = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_billing, container, false);
        first_name = (EditText) view.findViewById(R.id.first_name);
        last_name = (EditText) view.findViewById(R.id.last_name);
        address = (EditText) view.findViewById(R.id.address);
        city_spinner = (Spinner) view.findViewById(R.id.city);
        state_spinner = (Spinner) view.findViewById(R.id.state);
        postcode = (EditText) view.findViewById(R.id.passcode);
        email = (EditText) view.findViewById(R.id.email);
        phone = (EditText) view.findViewById(R.id.phone);

    //    state.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, State);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_spinner.setAdapter(aa);
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int position, long l) {
                Toast.makeText(getContext(), State[position], Toast.LENGTH_LONG).show();
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
                city_spinner.setOnItemSelectedListener(this);
            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {
            }

        });
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
        Billing billing = new Billing(first_name.getText().toString().trim(),last_name.getText().toString().trim(),address.getText().toString().trim(),city, state,
                postcode.getText().toString().trim(), "ایران", email.getText().toString().trim(), phone.getText().toString().trim());

        Shipping shipping = new Shipping(first_name.getText().toString().trim(),last_name.getText().toString().trim(),address.getText().toString().trim(),city, state,
                postcode.getText().toString().trim(), "ایران");

        //TODO: send customer info. to server, then create order



    }
}
