package com.example.qhs.wallpapershopping;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import About.AboutUs;
import Recycler.Search;

public class UIElement extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private Activity activity;
    private AuthHelper mAuthHelper;
    public BottomNavigationView bottomNavigation;
    //burved bottom navigation
    public CurvedBottomNavigationView curvedBottomNavigationView ;
//    =
//            (CurvedBottomNavigationView) activity.findViewById(R.id.curved_bottom_navigation);
    public VectorMasterView fab_home ,
                            fab_search ,
                            fab_favorite ,
                            fab_call ,
                            fab_shopping_cart ;
    //public ;

//        fab_call = (VectorMasterView) activity.findViewById(R.id.fab_call);
//        fab_favorite = (VectorMasterView) activity.findViewById(R.id.fab_favorite);
//        fab_search = (VectorMasterView) activity.findViewById(R.id.fab_search);
//        fab_shopping_cart = (VectorMasterView) activity.findViewById(R.id.fab_shopping_cart);

    RelativeLayout lin_id;
    PathModel  outline;

    public static Bitmap fastblur;
    public MenuItem menuItem;

    public UIElement(Context c, Activity a) {
        context = c;
        activity = a;
    }

    public void FontMethod() {
        //  toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        // ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        // setSupportActionBar(toolbar);
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "fonts/homa.ttf");
        TextView txtView_title = (TextView) activity.findViewById(R.id.txtTitle);
        txtView_title.setTypeface(face);
    }

//    public void NavigationMethod() {
//
//        bottomNavigation = (BottomNavigationView) activity.findViewById(R.id.bottom_navigation);
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);
//
//        for (int i = 0; i < menuView.getChildCount(); i++) {
//            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
//            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
//            final DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
//            if ((activity.getResources().getConfiguration().screenLayout &
//                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
//                    Configuration.SCREENLAYOUT_SIZE_NORMAL) {
//                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics);
//                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics);
//            }
//            if ((activity.getResources().getConfiguration().screenLayout &
//                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
//                    Configuration.SCREENLAYOUT_SIZE_LARGE) {
//                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
//                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
//            }
//            if ((activity.getResources().getConfiguration().screenLayout &
//                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
//                    Configuration.SCREENLAYOUT_SIZE_XLARGE) {
//                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
//                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
//            }
//            if ((activity.getResources().getConfiguration().screenLayout &
//                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
//                    Configuration.SCREENLAYOUT_SIZE_SMALL) {
//                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
//                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
//            }
//            iconView.setLayoutParams(layoutParams);
//            bottomNavigation.getMenu().getItem(0).setIcon(R.drawable.home);
//            bottomNavigation.getMenu().getItem(1).setIcon(R.drawable.shopping);
//            bottomNavigation.getMenu().getItem(2).setIcon(R.drawable.phone);
//            bottomNavigation.getMenu().getItem(3).setIcon(R.drawable.search);
//            bottomNavigation.getMenu().getItem(4).setIcon(R.drawable.favorite_no);
//        }
//        ;
//        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                handleBottomNavigationItemSelected(item);
//                return true;
//            }
//        });
//    }
//
//    private void handleBottomNavigationItemSelected(MenuItem item) {
//
//        AnimationDrawable bottomNavigationAnimation;
//        //log in
//        mAuthHelper = AuthHelper.getInstance(this);
//        switch (item.getItemId()) {
//            case R.id.Home:
//                // start Intent
//                //  activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//                //item.setIcon(R.drawable.animation_navigation_home);
//                bottomNavigationAnimation = (AnimationDrawable) item.getIcon();
//                bottomNavigationAnimation.start();
//                context.startActivity(new Intent(context, MainActivity.class));
//                break;
//            case R.id.AboutUs:
//                context.startActivity(new Intent(context, AboutUs.class)); // start Intent
//                item.setIcon(R.drawable.phone_fill);
//
//                break;
//            case R.id.Search:
//                Intent intent2 = new Intent(context, Search.class);
//                item.setIcon(R.drawable.animation_navigation_search);
//                bottomNavigationAnimation = (AnimationDrawable) item.getIcon();
//                bottomNavigationAnimation.start();
//                context.startActivity(intent2); // start Intent
//
//                break;
//            case R.id.Shop:
//                if (mAuthHelper.isLoggedIn()) {
//                    Intent intent3= new Intent(context, ShoppingActivity.class);
//                    context.startActivity(intent3); // start Intent
//                    item.setIcon(R.drawable.shopping_fill);
//                } else {
//                    Bitmap map = takeScreenShot(activity);
//                    Bitmap fast = fastblur(map, 10);
//                    this.fastblur = fast;
//                    context.startActivity(new Intent(context, RegisterDialogActivity.class));
//                }
//
//                break;
//            case R.id.Favorite:
//                if (mAuthHelper.isLoggedIn()) {
//                    Intent intent4 = new Intent(context, FavoriteActivity.class);
//                    context.startActivity(intent4); // start Intent
//                    item.setIcon(R.drawable.favorite_fill);
//                } else {
//                    Bitmap map = takeScreenShot(activity);
//                    Bitmap fast = fastblur(map, 10);
//                    this.fastblur = fast;
//                    context.startActivity(new Intent(context, RegisterDialogActivity.class));
//                }
//        }
//    }

    public void defineVariable(){

           }
    public void curvedNavigationMethod() {
        curvedBottomNavigationView = (CurvedBottomNavigationView) activity.findViewById(R.id.curved_bottom_navigation);
        //BottomNavigationMenuView menuView = (BottomNavigationMenuView) curvedBottomNavigationView.getChildAt(0);

        fab_home = (VectorMasterView) activity.findViewById(R.id.fab_home);
        fab_call = (VectorMasterView) activity.findViewById(R.id.fab_call);
        fab_favorite = (VectorMasterView) activity.findViewById(R.id.fab_favorite);
        fab_search = (VectorMasterView) activity.findViewById(R.id.fab_search);
        fab_shopping_cart = (VectorMasterView) activity.findViewById(R.id.fab_shopping_cart);


        lin_id = (RelativeLayout) activity.findViewById(R.id.lin_id);

        //curvedBottomNavigationView.inflateMenu(R.menu.curved_bottom_nav_item);
        //test code for getting icons manually not in menu file in order to avoid double show icons in nav
//        iconView.setLayoutParams(layoutParams);
//
//        curvedBottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_shopping_cart_black_24dp);
//        curvedBottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_search_black_24dp);
//        curvedBottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_home_black_24dp);
//        curvedBottomNavigationView.getMenu().getItem(3).setIcon(R.drawable.ic_favorite_black_24dp);
//        curvedBottomNavigationView.getMenu().getItem(4).setIcon(R.drawable.ic_call_black_24dp);

//        menuItem = (MenuItem)findViewById(R.id.Home);
//        menuItem.setVisible(false);
//        curvedBottomNavigationView.getMenu().getItem(0).getIcon().setVisible(true,true);
//        curvedBottomNavigationView.getMenu().getItem(1).getIcon().setVisible(true,true);
//        curvedBottomNavigationView.getMenu().getItem(2).getIcon().setVisible(true,true);
//        curvedBottomNavigationView.getMenu().getItem(3).getIcon().setVisible(true,true);
//        curvedBottomNavigationView.getMenu().getItem(4).getIcon().setVisible(true,true);

        //set event for botton navigation
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(this);
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

        mAuthHelper = AuthHelper.getInstance(this);

        switch (menuItem.getItemId()){
            case R.id.menu_shopping_cart:
                draw(6);
                //find the correct path using name
                lin_id.setX(curvedBottomNavigationView.mFirstCurveControlPoint1.x-22);
                fab_shopping_cart.setVisibility(View.VISIBLE);
                fab_home.setVisibility(View.GONE);
                fab_search.setVisibility(View.GONE);
                fab_favorite.setVisibility(View.GONE);
                fab_call.setVisibility(View.GONE);
                drawAnimation(fab_shopping_cart);
                if (mAuthHelper.isLoggedIn()) {
                    Intent intent3= new Intent(context, ShoppingActivity.class);
                    context.startActivity(intent3); // start Intent
                    //item.setIcon(R.drawable.shopping_fill);
                } else {
                    Bitmap map = takeScreenShot(activity);
                    Bitmap fast = fastblur(map, 10);
                    this.fastblur = fast;
                    context.startActivity(new Intent(context, RegisterDialogActivity.class));
                }
                break;
            case R.id.menu_search:
                //Animation
                draw(3);
                //find the correct path using name
                lin_id.setX(curvedBottomNavigationView.mFirstCurveControlPoint1.x-22);
                fab_home.setVisibility(View.GONE);
                fab_shopping_cart.setVisibility(View.GONE);
                fab_search.setVisibility(View.VISIBLE);
                fab_favorite.setVisibility(View.GONE);
                fab_call.setVisibility(View.GONE);
                drawAnimation(fab_search);
                Intent intent2 = new Intent(context, Search.class);
                context.startActivity(intent2); // start Intent
                break;

            case R.id.menu_home:
                //Animation
                draw(2);
                //find the correct path using name
                lin_id.setX(curvedBottomNavigationView.mFirstCurveControlPoint1.x-22);
                fab_home.setVisibility(View.VISIBLE);
                fab_shopping_cart.setVisibility(View.GONE);
                fab_search.setVisibility(View.GONE);
                fab_favorite.setVisibility(View.GONE);
                fab_call.setVisibility(View.GONE);
                drawAnimation(fab_home);
                context.startActivity(new Intent(context, MainActivity.class));
                break;

            case R.id.menu_favorite:
                //Animation
                draw2();
                //find the correct path using name
                lin_id.setX(curvedBottomNavigationView.mFirstCurveControlPoint1.x-22);
                fab_home.setVisibility(View.GONE);
                fab_shopping_cart.setVisibility(View.GONE);
                fab_search.setVisibility(View.GONE);
                fab_favorite.setVisibility(View.VISIBLE);
                fab_call.setVisibility(View.GONE);
                drawAnimation(fab_favorite);

                if (mAuthHelper.isLoggedIn()) {
                    Intent intent4 = new Intent(context, FavoriteActivity.class);
                    context.startActivity(intent4); // start Intent
                    // item.setIcon(R.drawable.favorite_fill);
                } else {
//                    Bitmap map = takeScreenShot(activity);
//                    Bitmap fast = fastblur(map, 10);
//                    this.fastblur = fast;
                    context.startActivity(new Intent(context, RegisterDialogActivity.class));
                }
                break;

            case R.id.menu_call:
                //Animation
                draw();
                //find the correct path using name
                lin_id.setX(curvedBottomNavigationView.mFirstCurveControlPoint1.x-22);
                fab_call.setVisibility(View.VISIBLE);
                fab_home.setVisibility(View.GONE);
                fab_shopping_cart.setVisibility(View.GONE);
                fab_search.setVisibility(View.GONE);
                fab_favorite.setVisibility(View.GONE);
                drawAnimation(fab_call);
                context.startActivity(new Intent(context, AboutUs.class)); // start Intent
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
}