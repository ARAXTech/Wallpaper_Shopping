package Gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.example.qhs.wallpapershopping.AuthHelper;
import com.example.qhs.wallpapershopping.MainActivity;
import com.example.qhs.wallpapershopping.R;

import Data.DatabaseHandler;
import Model.ListItem;

import com.example.qhs.wallpapershopping.RegisterDialogActivity;
import com.example.qhs.wallpapershopping.UIElement;
import com.viewpagerindicator.CirclePageIndicator;


import org.json.JSONException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Model.ImageModel;

public class gallery extends AppCompatActivity {
    private  ViewPager mPager;
    private  int currentPage = 0;
    private  int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    private Timer swipeTimer;
    private AuthHelper mAuthHelper;
    private Menu mOptionsMenu;
    private Button profileBtn;
    Activity activity = (Activity) this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //DataBase Define
        final DatabaseHandler db=new DatabaseHandler(this);
        final DatabaseHandler db1=new DatabaseHandler(this);

        //log in
        mAuthHelper = AuthHelper.getInstance(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UIElement cls = new UIElement(gallery.this,this);
        cls.FontMethod();
        //add back button in toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        //Navigation
        UIElement cls1 = new UIElement(gallery.this,this);
        //cls1.NavigationMethod();
        cls1.curvedNavigationMethod();

        ///Face
        final Typeface face=Typeface.createFromAsset(getAssets(), "yekan/homa.ttf");
        TextView txtView_title = (TextView)findViewById(R.id.txtTitle);
        txtView_title.setTypeface(face);

//Profile
        //profileBtn=(Button) findViewById(R.id.ProfileBtn);
        mAuthHelper = AuthHelper.getInstance(this);

//        profileBtn = (Button) findViewById(R.id.ProfileBtn);
//        profileBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//
//            }
//        });

        //updateOptionsMenu();
        if (mAuthHelper.isLoggedIn()) {
            Log.d("USERNAME: ", "isloggedin");
            //profileBtn.setVisibility(View.GONE);
            // setupView();
        } else {

            //  finish();
        }

        imageModelArrayList = new ArrayList<>();
        try {
            imageModelArrayList = populateList();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String id =getIntent().getStringExtra("id") ;
        final String description=getIntent().getStringExtra("description");
        //Textview
        TextView txt_name=(TextView)findViewById(R.id.txt1);
        final TextView txt_description=(TextView)findViewById(R.id.txt2);
        txt_description.setText("\n" + Html.fromHtml(getIntent().getStringExtra("description")));
        txt_description.setTextColor(Color.parseColor("#000000"));
        //txt_description.setPaintFlags(txt_description.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        //txt_description.setMovementMethod(new ScrollingMovementMethod());
        txt_description.setTypeface(face);

     //   TextView txt_id=(TextView)findViewById(R.id.txt3);
        String product_code=" کد محصول: " ;
//        txt_id.setText(product_code+getIntent().getStringExtra("id"));
//        txt_id.setTextColor(Color.parseColor("#FF0000"));

        final String name = getIntent().getStringExtra("name");
        txt_name.setText( name +"\n"+
                product_code +getIntent().getStringExtra("id") );


        txt_name.setTypeface(face);

        init();
        final ArrayList<String> image_list = getIntent().getStringArrayListExtra("imageJsonObj");
       String s="";
        for (int i=1;i<image_list.size(); i=i+1)
        {
            s=s+image_list.get(i)+",";
            Log.d("listtt",s);
        }
      //favorite button
        final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
//     Log.d("EXIST", String.valueOf(db.Exists(id)));
        if (db.Exists(id)==true) {
                Log.d("vojod","vojod dare");
                Log.d("id", id);
                toggleButton.setChecked(true);
                toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favorite_yes));
            } else {
            Log.d("id1",String.valueOf(Integer.parseInt(id)));
                Log.d("id",id);
            toggleButton.setChecked(false);
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favorite_no));
            }

        final String finalS = s;
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ListItem item = new ListItem(id,name,description, finalS,"true",image_list.size(),1000,1);
                if (isChecked ){
                    db.addListItem(item);
                    Log.d("idddd",item.getId());
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.favorite_yes));
                    Log.d("additem",description);}
                else if (!isChecked ){
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favorite_no));
                   db.deleteListItem(item.getId());
                    Log.d("deleteitem",description);}
            }
        });
        
        ///Shopping Cart
        final Button shoppingBtn=(Button) findViewById(R.id.ShoppingBtn);
        shoppingBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                if (mAuthHelper.isLoggedIn()) {
                    ListItem item = new ListItem(id,name,description, finalS,"true",image_list.size(),1000,1);
                    db1.addListItem(item);
                    shoppingBtn.setEnabled(true);
                    shoppingBtn.setClickable(false);
                    shoppingBtn.setBackgroundColor(R.color.SecondaryLight);
                } else {
                    Bitmap map = UIElement.takeScreenShot(activity);
                    Bitmap fast = UIElement.fastblur(map, 10);
                    UIElement.fastblur = fast;
                    startActivity(new Intent(getApplicationContext(),RegisterDialogActivity.class));
                }


            }
        });


///SHARE
        ImageView img_share=(ImageView) findViewById(R.id.Share);
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                String ShareBody="your body here";
                String ShareSub="your subject here";
                sendIntent.putExtra(Intent.EXTRA_SUBJECT,ShareSub);
                sendIntent.putExtra(Intent.EXTRA_TEXT,ShareBody);
                startActivity(Intent.createChooser(sendIntent,"share using"));
            }
        });
    }

    private ArrayList<ImageModel> populateList() throws JSONException {



        ArrayList<String> image_list = getIntent().getStringArrayListExtra("imageJsonObj");

        // JsonParser parser = new JsonParser();
        //JSONObject scamDataJsonObject = parser.parse(scamDatas).getAsJsonObject();


        ArrayList<ImageModel> list = new ArrayList<>();
         //add image to list
        for(int i = 0; i < image_list.size(); i++){

            ImageModel imageModel = new ImageModel();
            imageModel.setImage(image_list.get(i));
            Log.d("INGALLERY",image_list.get(i));
            list.add(imageModel);
        }

        return list;
    }

    private void init() {
        //add image to ViewPager
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

         //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        View view = findViewById(android.R.id.content);
        Boolean toggle;
        // Auto start of viewpager
        //if(view.getId() == R.id.)
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
         swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }


    @Override
    protected void onDestroy() {

        swipeTimer.cancel();
        super.onDestroy();

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
