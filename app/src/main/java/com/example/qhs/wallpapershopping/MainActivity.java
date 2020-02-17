package com.example.qhs.wallpapershopping;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.qhs.wallpapershopping.Fragments.Dialog;
import com.example.qhs.wallpapershopping.Fragments.Fragment_Shopping;
import com.example.qhs.wallpapershopping.Fragments.Fragment_about;
import com.example.qhs.wallpapershopping.Fragments.Fragment_favorite;
import com.example.qhs.wallpapershopping.Fragments.Fragment_home;
import com.example.qhs.wallpapershopping.Fragments.Fragment_login;
import com.example.qhs.wallpapershopping.Fragments.Fragment_search;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import android.os.Bundle;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    //Decleration
    private AuthHelper mAuthHelper;
    private Menu mOptionsMenu;
    private ImageButton profileBtn;
    private TextView txtView_login;
    private TextView txtView_signUp;
    private Toolbar toolbar;
    private ImageView bgApp, clover;
    private Animation bgAnim, cloverAnim;
    private LinearLayout textSplash, textHome;
    private Animation frombottom;


    private Context context;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    public BottomNavigationView bottomNavigation;
    //curved bottom navigation
    public CurvedBottomNavigationView curvedBottomNavigationView ;
    public VectorMasterView fab_home ,
            fab_search ,
            fab_favorite ,
            fab_call ,
            fab_shopping_cart ;


    RelativeLayout lin_id;
    PathModel outline;

    public static Bitmap fastblur;
    public MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context= this;

//        //Splash Screen
        if (Splashscreen.Splash == 0){
            Intent intent = new Intent(this,Splashscreen.class);
            startActivity(intent);
            super.onCreate(savedInstanceState);
            finish();
            return;
        }
//        //End Splash

        super.onCreate(savedInstanceState);
        //animation
        //setAnimation();
        setContentView(R.layout.activity_main);
        //frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);

//        textSplash = findViewById(R.id.textsplash);
//        textHome = findViewById(R.id.texthome);
//
//        bgApp = findViewById(R.id.bgapp);
//        clover = findViewById(R.id.clover);

        fragment = new Fragment_home();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame, fragment).commit();
//        //Toolbar

         toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         getSupportActionBar().setTitle("");


        //Profile
        mAuthHelper = AuthHelper.getInstance(this);

        profileBtn = (ImageButton) findViewById(R.id.ProfileBtn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new Fragment_login();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment).commit();;

            }
        });
//


//        txtView_login = findViewById(R.id.nameuser);
//        txtView_signUp= findViewById(R.id.walletuser);
//
//        //updateOptionsMenu();
        if (mAuthHelper.isLoggedIn()) {
            Log.d("USERNAME: ", "isloggedin");
            profileBtn.setVisibility(View.GONE);

        }


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);


    }

    public void curvedNavigationMethod() {
//        curvedBottomNavigationView = (CurvedBottomNavigationView) findViewById(R.id.curved_bottom_navigation);
//        //BottomNavigationMenuView menuView = (BottomNavigationMenuView) curvedBottomNavigationView.getChildAt(0);
//
//        fab_home = (VectorMasterView) findViewById(R.id.fab_home);
//        fab_call = (VectorMasterView) findViewById(R.id.fab_call);
//        fab_favorite = (VectorMasterView) findViewById(R.id.fab_favorite);
//        fab_search = (VectorMasterView) findViewById(R.id.fab_search);
//        fab_shopping_cart = (VectorMasterView) findViewById(R.id.fab_shopping_cart);
//
//
//        lin_id = (RelativeLayout) findViewById(R.id.lin_id);
//
//        //set event for botton navigation
//        curvedBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    public static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }

        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);


        return (bitmap);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        //  mAuthHelper = AuthHelper.getInstance(this);

        switch (menuItem.getItemId()){
            case R.id.menu_shopping_cart:
//                draw(6);
//                //find the correct path using name
//                lin_id.setX(curvedBottomNavigationView.mFirstCurveControlPoint1.x-22);
//                fab_shopping_cart.setVisibility(View.VISIBLE);
//                fab_home.setVisibility(View.GONE);
//                fab_search.setVisibility(View.GONE);
//                fab_favorite.setVisibility(View.GONE);
//                fab_call.setVisibility(View.GONE);
//                drawAnimation(fab_shopping_cart);

                if (mAuthHelper.isLoggedIn()) {
                    fragment = new Fragment_Shopping();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame, fragment).commit();;
                } else {
                    Bitmap map = takeScreenShot(this);
                    Bitmap fast = fastblur(map, 10);
                    this.fastblur = fast;
                    fragment = new Dialog();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.frame, fragment).commit();
                }
                break;
            case R.id.menu_search:
                //Animation
//                draw(3);
//                //find the correct path using name
//                lin_id.setX(curvedBottomNavigationView.mFirstCurveControlPoint1.x-22);
//                fab_home.setVisibility(View.GONE);
//                fab_shopping_cart.setVisibility(View.GONE);
//                fab_search.setVisibility(View.VISIBLE);
//                fab_favorite.setVisibility(View.GONE);
//                fab_call.setVisibility(View.GONE);
//                drawAnimation(fab_search);
                fragment = new Fragment_search();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment).commit();
                break;

            case R.id.menu_home:
                //Animation
//                draw(2);
//                //find the correct path using name
//                lin_id.setX(curvedBottomNavigationView.mFirstCurveControlPoint1.x-22);
//                fab_home.setVisibility(View.VISIBLE);
//                fab_shopping_cart.setVisibility(View.GONE);
//                fab_search.setVisibility(View.GONE);
//                fab_favorite.setVisibility(View.GONE);
//                fab_call.setVisibility(View.GONE);
//                drawAnimation(fab_home);
                //   context.startActivity(new Intent(context, MainActivity.class));

                fragment = new Fragment_home();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame, fragment).commit();
                break;

            case R.id.menu_favorite:
                //Animation
//                draw2();
//                //find the correct path using name
//                lin_id.setX(curvedBottomNavigationView.mFirstCurveControlPoint1.x-22);
//                fab_home.setVisibility(View.GONE);
//                fab_shopping_cart.setVisibility(View.GONE);
//                fab_search.setVisibility(View.GONE);
//                fab_favorite.setVisibility(View.VISIBLE);
//                fab_call.setVisibility(View.GONE);
//                drawAnimation(fab_favorite);

                if (mAuthHelper.isLoggedIn()) {
                    fragment = new Fragment_favorite();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame, fragment).commit();
                } else {
                   Bitmap map = takeScreenShot(this);
                    Bitmap fast = fastblur(map, 10);
                    this.fastblur = fast;
                    fragment = new Dialog();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.frame, fragment).commit();
                }
                break;

            case R.id.menu_call:
                //Animation
//                draw();
//                //find the correct path using name
//                lin_id.setX(curvedBottomNavigationView.mFirstCurveControlPoint1.x-22);
//                fab_call.setVisibility(View.VISIBLE);
//                fab_home.setVisibility(View.GONE);
//                fab_shopping_cart.setVisibility(View.GONE);
//                fab_search.setVisibility(View.GONE);
//                fab_favorite.setVisibility(View.GONE);
//                drawAnimation(fab_call);
                // context.startActivity(new Intent(context, AboutUs.class)); // start Intent
                fragment = new Fragment_about();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment).commit();
                break;

            // 1/6           2/6               3/6          4/6           5/6

        }
        return true;
    }

    private void draw2() {
        curvedBottomNavigationView.mFirstCurveStartPoint.set((curvedBottomNavigationView.mNavigationBarWidth*2/3)
                -(curvedBottomNavigationView.CURVE_CIRCLE_RADIUS*2)- (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);

        curvedBottomNavigationView.mFirstCurveEndPoint.set(curvedBottomNavigationView.mNavigationBarWidth*2/3,
                curvedBottomNavigationView.CURVE_CIRCLE_RADIUS +(curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/4));

        curvedBottomNavigationView.mSecondCurveStartPoint = curvedBottomNavigationView.mFirstCurveEndPoint;

        curvedBottomNavigationView.mSecondCurveEndPoint.set((curvedBottomNavigationView.mNavigationBarWidth*2/3)
                +(curvedBottomNavigationView.CURVE_CIRCLE_RADIUS*2) + (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);

        curvedBottomNavigationView.mFirstCurveControlPoint1.set(curvedBottomNavigationView.mFirstCurveStartPoint.x
                        +curvedBottomNavigationView.CURVE_CIRCLE_RADIUS + (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/4),
                curvedBottomNavigationView.mFirstCurveStartPoint.y);

        curvedBottomNavigationView.mFirstCurveControlPoint2.set(curvedBottomNavigationView.mFirstCurveEndPoint.x -
                        (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS*2) + curvedBottomNavigationView.CURVE_CIRCLE_RADIUS,
                curvedBottomNavigationView.mFirstCurveEndPoint.y);

        //same with the second

        curvedBottomNavigationView.mSecondCurveControlPoint1.set(curvedBottomNavigationView.mSecondCurveStartPoint.x
                        + (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS*2) - (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS),
                curvedBottomNavigationView.mSecondCurveStartPoint.y);

        curvedBottomNavigationView.mSecondCurveControlPoint2.set(curvedBottomNavigationView.mSecondCurveEndPoint.x -
                        (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS + (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/4)),
                curvedBottomNavigationView.mSecondCurveEndPoint.y);

    }

    private void draw() {

        curvedBottomNavigationView.mFirstCurveStartPoint.set((curvedBottomNavigationView.mNavigationBarWidth*5/6)
                -(curvedBottomNavigationView.CURVE_CIRCLE_RADIUS*2)- (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);

        curvedBottomNavigationView.mFirstCurveEndPoint.set(curvedBottomNavigationView.mNavigationBarWidth*5/6,
                curvedBottomNavigationView.CURVE_CIRCLE_RADIUS +(curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/4));

        curvedBottomNavigationView.mSecondCurveStartPoint = curvedBottomNavigationView.mFirstCurveEndPoint;

        curvedBottomNavigationView.mSecondCurveEndPoint.set((curvedBottomNavigationView.mNavigationBarWidth*5/6)
                +(curvedBottomNavigationView.CURVE_CIRCLE_RADIUS*2) + (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);

        curvedBottomNavigationView.mFirstCurveControlPoint1.set(curvedBottomNavigationView.mFirstCurveStartPoint.x
                        +curvedBottomNavigationView.CURVE_CIRCLE_RADIUS + (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/4),
                curvedBottomNavigationView.mFirstCurveStartPoint.y);

        curvedBottomNavigationView.mFirstCurveControlPoint2.set(curvedBottomNavigationView.mFirstCurveEndPoint.x -
                        (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS*2) + curvedBottomNavigationView.CURVE_CIRCLE_RADIUS,
                curvedBottomNavigationView.mFirstCurveEndPoint.y);

        //same with the second

        curvedBottomNavigationView.mSecondCurveControlPoint1.set(curvedBottomNavigationView.mSecondCurveStartPoint.x
                        + (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS*2) - (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS),
                curvedBottomNavigationView.mSecondCurveStartPoint.y);

        curvedBottomNavigationView.mSecondCurveControlPoint2.set(curvedBottomNavigationView.mSecondCurveEndPoint.x -
                        (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS + (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/4)),
                curvedBottomNavigationView.mSecondCurveEndPoint.y);

    }

    private void draw(int i) {

        curvedBottomNavigationView.mFirstCurveStartPoint.set((curvedBottomNavigationView.mNavigationBarWidth/i)
                -(curvedBottomNavigationView.CURVE_CIRCLE_RADIUS*2)- (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);

        curvedBottomNavigationView.mFirstCurveEndPoint.set(curvedBottomNavigationView.mNavigationBarWidth/i,
                curvedBottomNavigationView.CURVE_CIRCLE_RADIUS +(curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/4));

        curvedBottomNavigationView.mSecondCurveStartPoint = curvedBottomNavigationView.mFirstCurveEndPoint;

        curvedBottomNavigationView.mSecondCurveEndPoint.set((curvedBottomNavigationView.mNavigationBarWidth/i)
                +(curvedBottomNavigationView.CURVE_CIRCLE_RADIUS*2) + (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);

        curvedBottomNavigationView.mFirstCurveControlPoint1.set(curvedBottomNavigationView.mFirstCurveStartPoint.x
                        +curvedBottomNavigationView.CURVE_CIRCLE_RADIUS + (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/4),
                curvedBottomNavigationView.mFirstCurveStartPoint.y);

        curvedBottomNavigationView.mFirstCurveControlPoint2.set(curvedBottomNavigationView.mFirstCurveEndPoint.x -
                        (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS*2) + curvedBottomNavigationView.CURVE_CIRCLE_RADIUS,
                curvedBottomNavigationView.mFirstCurveEndPoint.y);

        //same with the second

        curvedBottomNavigationView.mSecondCurveControlPoint1.set(curvedBottomNavigationView.mSecondCurveStartPoint.x
                        + (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS*2) - (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS),
                curvedBottomNavigationView.mSecondCurveStartPoint.y);

        curvedBottomNavigationView.mSecondCurveControlPoint2.set(curvedBottomNavigationView.mSecondCurveEndPoint.x -
                        (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS + (curvedBottomNavigationView.CURVE_CIRCLE_RADIUS/4)),
                curvedBottomNavigationView.mSecondCurveEndPoint.y);

    }

    private void drawAnimation(VectorMasterView fab) {

        outline = fab.getPathModelByName("outline");
        outline.setStrokeColor(Color.WHITE);
        outline.setTrimPathEnd(0.0f);

        //init value animator
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f,1.0f);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                outline.setTrimPathEnd((Float)valueAnimator.getAnimatedValue());
                fab.update();
            }
        });
        valueAnimator.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_signout){
            mAuthHelper.clear();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            profileBtn.setVisibility(View.VISIBLE );
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