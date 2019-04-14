package Gallery;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;


import android.view.ViewGroup;
import android.widget.TextView;


import About.AboutUs;
import com.example.qhs.deydigital.MainActivity;
import com.example.qhs.deydigital.R;
import Recycler.Search;
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

    /*private int[] myImageList = new int[]{R.drawable.logo1, R.drawable.logo2,
            R.drawable.logo3,R.drawable.logo4};*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
//Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Typeface face=Typeface.createFromAsset(getAssets(),"fonts/homa.ttf");
        TextView txtView_title = (TextView)findViewById(R.id.txtTitle);
        txtView_title.setTypeface(face);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        //Navigation

        BottomNavigationView bottomNavigation =
                (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics);
            }
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_LARGE) {
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
            }
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
            }
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_SMALL) {
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
            }
            iconView.setLayoutParams(layoutParams);
        }
        ;
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleBottomNavigationItemSelected(item);
                return true;
            }
        });
        imageModelArrayList = new ArrayList<>();
        try {
            imageModelArrayList = populateList();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String id = getIntent().getStringExtra("id");

        //Textview
        TextView txt_name=(TextView)findViewById(R.id.txt1);




        TextView txt_description=(TextView)findViewById(R.id.txt2);
        txt_description.setText("\n" + Html.fromHtml(getIntent().getStringExtra("description")));
        txt_description.setTextColor(Color.parseColor("#000000"));
        //txt_description.setPaintFlags(txt_description.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        //txt_description.setMovementMethod(new ScrollingMovementMethod());
        txt_description.setTypeface(face);

     //   TextView txt_id=(TextView)findViewById(R.id.txt3);
        String product_code=" کد محصول: " ;
//        txt_id.setText(product_code+getIntent().getStringExtra("id"));
//        txt_id.setTextColor(Color.parseColor("#FF0000"));

        String name = getIntent().getStringExtra("name");
        txt_name.setText( name +"\n"+
                product_code +getIntent().getStringExtra("id") );


        txt_name.setTypeface(face);

        init();
    }
    private ArrayList<ImageModel> populateList() throws JSONException {



        ArrayList<String> image_list = getIntent().getStringArrayListExtra("imageJsonObj");

        // JsonParser parser = new JsonParser();
        //JSONObject scamDataJsonObject = parser.parse(scamDatas).getAsJsonObject();


        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < image_list.size(); i++){

            ImageModel imageModel = new ImageModel();
            imageModel.setImage(image_list.get(i));
            Log.d("INGALLERY",image_list.get(i));
            list.add(imageModel);
        }

        return list;
    }

    private void init() {

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
    private void handleBottomNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent); // start Intent
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.AboutUs:
                Intent intent1 = new Intent(this, AboutUs.class);
                startActivity(intent1); // start Intent
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.Search:
                Intent intent2 = new Intent(this,Search.class);
                startActivity(intent2); // start Intent
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    @Override
    protected void onDestroy() {

        swipeTimer.cancel();
        super.onDestroy();

    }
}
