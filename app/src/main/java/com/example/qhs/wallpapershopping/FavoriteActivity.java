package com.example.qhs.wallpapershopping;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import Data.DatabaseHandler;
import Model.ListItem;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private List <ListItem> listItems;
    private AuthHelper mAuthHelper;
    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final DatabaseHandler db = new DatabaseHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UIElement cls = new UIElement(FavoriteActivity.this, this);
        cls.FontMethod();
        //add back button in toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
               // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


        //Navigation
        UIElement cls1 = new UIElement(FavoriteActivity.this, this);
        cls1.defineVariable();
        cls1.curvedNavigationMethod();


      //
        mAuthHelper = AuthHelper.getInstance(this);
        //Button profileBtn=(Button)findViewById(R.id.ProfileBtn);
        //profileBtn.setVisibility(View.GONE);
        //
        recyclerView = (RecyclerView) findViewById(R.id.FavoriteRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listItems = new ArrayList <>();


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
        adapter = new FavoriteAdapter(this, listItems);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_signout){
            mAuthHelper.clear();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            // profileBtn.setVisibility(View.VISIBLE );
            //finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.signout_menu, menu);
        mOptionsMenu = menu;
        return super.onCreateOptionsMenu(mOptionsMenu);
    }

    public boolean onPrepareOptionsMenu(Menu menu)
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
        return true;
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
