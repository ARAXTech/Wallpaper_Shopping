package com.example.qhs.wallpapershopping.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.qhs.wallpapershopping.R;



public class Dialog extends Fragment {
    private Button btnCancel;
    private Button btnRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_dialog, container, false);

        //register
        btnRegister=(Button)view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Fragment_login();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment).commit();

            }
        });

        //Cancel
        btnCancel=(Button)view.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}
