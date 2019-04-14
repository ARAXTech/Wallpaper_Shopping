package com.example.qhs.deydigital;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Splashscreen extends Activity {
    public static int Splash = 0;
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        StartAnimations();
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        // anim.reset();
        //   LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        //  l.clearAnimation();
        //   l.startAnimation(anim);

       /* anim = AnimationUtils.loadAnimation(this, R.anim.translate0);
        anim.reset();
        ImageView io = (ImageView) findViewById(R.id.splash0);
        io.clearAnimation();
        io.startAnimation(anim);*/

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView i = (ImageView) findViewById(R.id.splash1);
        i.clearAnimation();
        i.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate2);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash2);
        iv.clearAnimation();
        iv.startAnimation(anim);


        ////
        //   anim = AnimationUtils.loadAnimation(this, R.anim.shake);
        //   anim.reset();
        //  iv.clearAnimation();
        //iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        Splash++;
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(Splashscreen.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splashscreen.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Splashscreen.this.finish();
                }

            }
        };
        splashTread.start();

    }

}