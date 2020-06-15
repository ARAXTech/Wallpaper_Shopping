package com.example.qhs.wallpapershopping.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.qhs.wallpapershopping.R;



public class Dialog extends Fragment {
    private Button btnCancel;
    private Button btnRegister;
    BottomNavigationView navigation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_dialog, container, false);
        navigation = getActivity().findViewById(R.id.navigation);
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
               // getFragmentManager().popBackStackImmediate();

                FragmentManager fm = getFragmentManager();
              //  if (!getFragmentManager().popBackStackImmediate()) {
                    if (fm.getBackStackEntryCount() > 0) {


                        String tag = "";
                        tag = fm.getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 2).getName();
                        Log.d("TAG ", tag);
                        int index = 0;
                        switch (tag) {
                            case "fragment_home":
                                index = 0;
                                break;
                            case "fragment_shopping":
                                index = 1;
                                break;
                            case "fragment_search":
                                index = 2;
                                break;
                            case "fragment_favorite":
                                index = 3;
                                break;
                            case "fragment_call":
                                index = 4;
                                break;


                        }
                        navigation.getMenu().getItem(index).setChecked(true);
                        fm.popBackStackImmediate();
                    }
                }
            //}
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}
