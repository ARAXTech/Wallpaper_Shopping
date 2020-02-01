package com.example.qhs.wallpapershopping;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import java.util.ArrayList;
import java.util.List;

import Data.DatabaseHandler;
import Model.ListItem;

public class ShoppingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ShoppingAdapter adapter;
    private List<ListItem> listItems;
    private TextView totalPrice;
    private int sum = 0;
    private AuthHelper mAuthHelper;
    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final DatabaseHandler db1 = new DatabaseHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UIElement cls = new UIElement(ShoppingActivity.this, this);
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
        UIElement cls1 = new UIElement(ShoppingActivity.this, this);
  //      cls1.NavigationMethod();
        cls1.curvedNavigationMethod();
//
        mAuthHelper = AuthHelper.getInstance(this);
//        Button profileBtn=(Button)findViewById(R.id.ProfileBtn);
//        profileBtn.setVisibility(View.GONE);
        //

        recyclerView = (RecyclerView) findViewById(R.id.ShoppingRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listItems = new ArrayList<>();
        /// Spinner


        // db.deleteAll();
        listItems = db1.getAllListItem();

        int num = db1.getListItemCount();
        // ListItem[] myArray=new ListItem[num];
        ListItem[] myArray = listItems.toArray(new ListItem[listItems.size()]);
        Log.d("sizee", String.valueOf(num));
        adapter = new ShoppingAdapter(this,listItems);
    //    adapter.notifyDataSetChanged();


        for (int i=0; i <num; i++) {

           sum=sum+ listItems.get(i).getPrice()*listItems.get(i).getCount();

        }

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        totalPrice=(TextView)findViewById(R.id.totalPrice);
        totalPrice.setText(String.valueOf(sum)+"تومان");


        // zarinpal Payment
        Uri data = getIntent().getData();
        if (getIntent().getData() != null) {

            ZarinPal.getPurchase(this).verificationPayment(data, new OnCallbackVerificationPaymentListener() {
                @Override
                public void onCallbackResultVerificationPayment(boolean isPaymentSuccess, String refID, PaymentRequest paymentRequest) {
                    Log.i("TAG", "onCallbackResultVerificationPayment: " + refID);
                }
            });
        }

        Button btnPay = (Button) findViewById(R.id.totelPriceBtn);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myPayment();

            }
        });

    }

    private void myPayment(){

        ZarinPal purchase = ZarinPal.getPurchase(this);

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
                    Toast.makeText(getApplicationContext(),"خطا در ایجاد درخواست پرداخت", Toast.LENGTH_LONG).show();
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
