package About;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qhs.deydigital.MainActivity;
import com.example.qhs.deydigital.R;
import Recycler.Search;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
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


        //
        ImageView map = (ImageView)findViewById(R.id.imgView_map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("geo:0,0?q=34.61608232923757,50.860158371844136(Maninagar)");
                Intent intent4 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent4);
            }
        });
        TextView txtView_contact = (TextView)findViewById(R.id.txtView_contactUs);
        txtView_contact.setTypeface(face);

        Button contact=(Button)findViewById(R.id.contact);
        contact.setTypeface(face);

        Button email=(Button) findViewById(R.id.email);
        email.setTypeface(face);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+982532931515", null));
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"Deydigital@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }

        });

//


        final ListView simpleList1 = (ListView) findViewById(R.id.listA);

        final String[] myStrings = {
                "025-32931515",
                //"Deydigital@gmail.com",
                "09127592318",
                "dey_digital",
                "Dey_digital@",
                " قم/30 متری قائم/پلاک 150"};

        final int[] imgStrings = {
                R.mipmap.phone,
                R.mipmap.smartphone,
                R.mipmap.instagram ,
                R.mipmap.telegram,
              //  R.mipmap.mail,
                R.mipmap.address};

                /*R.drawable.phone,
                R.drawable.smartphone,
                R.drawable.instagram ,
                R.drawable.telegram,
                R.drawable.address};*/

        final AboutAdapter aboutAdapter = new AboutAdapter(getApplicationContext(), myStrings, imgStrings);
        simpleList1.setAdapter(aboutAdapter);


        simpleList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> adapterView, View view, int position, long l) {

                TextView textView = (TextView) view.findViewById(R.id.txtA);
                textView.setTypeface(face);

                switch (position) {
                    case 0:
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+982532931515", null));
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+989127592318", null));
                        startActivity(intent1);

                        break;
                    case 2:
                        //Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/ِDey_digital"));
                       /* Intent intent2 = new Intent(Intent.ACTION_VIEW);
                        intent2.setData(Uri.parse("http://instagram.com/_u/" + myStrings[2]));
                        intent2.setPackage("com.instagram.android");
                        startActivity(intent2);*/
                        String scheme = "http://instagram.com/_u/dey_digital";
                        String path = "https://instagram.com/dey_digital";
                        String nomPackageInfo ="com.instagram.android";
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));

                        try {
                            getApplicationContext().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                        } catch (Exception e) {
                            intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                        }
                        startActivity(intent2);


                        break;
                    case 3:

                        Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/" + myStrings[2] ));
                        startActivity(intent3);
                        break;

                    /*case 4:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"Deydigital@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }




                        break;*/

                    case 4:
                        Uri uri = Uri.parse("geo:0,0?q=34.61608232923757,50.860158371844136(چاپ دیجیتال دی)");
                        Intent intent4 = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent4);





                }

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
}
