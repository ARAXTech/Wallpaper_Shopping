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


public class Fragment_billing extends Fragment  implements AdapterView.OnItemSelectedListener {
       private EditText first_name;
       private EditText last_name;
       private EditText address;
       private Spinner city;
       private Spinner state;
       private EditText passcode;
       private EditText email;
       private EditText phone;
       String[] City_Tehran={"بومهن","رودهن","تهران"};
        String[] City_Mazandaran={"رامسر","بابل","ساری"};
       String[] State={"مازندران","تهران"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_billing, container, false);
        first_name = (EditText) view.findViewById(R.id.first_name);
        last_name = (EditText) view.findViewById(R.id.last_name);
        address = (EditText) view.findViewById(R.id.address);
        city = (Spinner) view.findViewById(R.id.city);
        state = (Spinner) view.findViewById(R.id.state);
        passcode = (EditText) view.findViewById(R.id.passcode);
        email = (EditText) view.findViewById(R.id.email);
        phone = (EditText) view.findViewById(R.id.phone);

    //    state.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, State);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(aa);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int position, long l) {
                Toast.makeText(getContext(), State[position], Toast.LENGTH_LONG).show();
                switch(position){
                    case 0:
                        ArrayAdapter aa1 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, City_Tehran);
                        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        city.setAdapter(aa1);
                        break;
                    case 1:
                        ArrayAdapter aa2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, City_Mazandaran);
                        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        city.setAdapter(aa2);
                        break;
                }
                city.setOnItemSelectedListener(this);
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
      //  Toast.makeText(getContext(), State[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

    }
}
