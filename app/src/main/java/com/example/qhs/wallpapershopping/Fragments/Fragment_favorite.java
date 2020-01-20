package com.example.qhs.wallpapershopping.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.qhs.wallpapershopping.AuthHelper;
import com.example.qhs.wallpapershopping.FavoriteAdapter;
import com.example.qhs.wallpapershopping.MainActivity;
import com.example.qhs.wallpapershopping.R;

import java.util.ArrayList;
import java.util.List;

import Data.DatabaseHandler;
import Model.ListItem;

//import com.example.qhs.deydigital.com.example.qhs.wallpapershopping.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_favorite.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_favorite#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_favorite extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private List<ListItem> listItems;
    private AuthHelper mAuthHelper;
    private DatabaseHandler db;
    private Menu mOptionsMenu;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_favorite() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_favorite.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_favorite newInstance(String param1, String param2) {
        Fragment_favorite fragment = new Fragment_favorite();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        db = new DatabaseHandler(getContext());

        mAuthHelper = AuthHelper.getInstance(getContext());
        //Button profileBtn=(Button) view.findViewById(R.id.ProfileBtn);
        //profileBtn.setVisibility(View.GONE);

        recyclerView = (RecyclerView) view.findViewById(R.id.FavoriteRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItems = new ArrayList<>();


        // db.deleteAll();
        listItems = db.getAllListItem();

        int num = db.getListItemCount();
        // ListItem[] myArray=new ListItem[num];
        ListItem[] myArray = listItems.toArray(new ListItem[listItems.size()]);
        Log.d("sizee", String.valueOf(num));
        for (int i = 0; i < num; i++) {
            //  if(db.Exists(i)==true){

            Log.d(" contact:", " " + myArray[i].getId());

            // listItems.add(item);
            //  }
        }
        adapter = new FavoriteAdapter(getActivity(), listItems);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // you can add listener of elements here
          /*Button mButton = (Button) view.findViewById(R.id.button);
            mButton.setOnClickListener(this); */

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_signout){
            mAuthHelper.clear();
            startActivity(new Intent(getActivity(), MainActivity.class));
            // profileBtn.setVisibility(View.VISIBLE );
            //finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.signout_menu, menu);
        mOptionsMenu = menu;
        super.onCreateOptionsMenu(mOptionsMenu, inflater);
    }

    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.action_signout);
        //register.setVisible(false);
        if(mAuthHelper.isLoggedIn())
        {
            register.setVisible(true);
        }
        else
        {
            register.setVisible(false);
        }
        //invalidateOptionsMenu();
        //return true;
    }
    private void updateOptionsMenu() {
        if (mOptionsMenu != null) {
            onPrepareOptionsMenu(mOptionsMenu);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        updateOptionsMenu();
        super.onConfigurationChanged(newConfig);
    }
}
