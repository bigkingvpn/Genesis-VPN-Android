package com.darkweb.genesisvpn.application.homeManager;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Typeface;
import android.widget.Button;

import com.darkweb.genesisvpn.R;

class home_view_controller {

    /*LOCAL VARIABLE DECLARATION*/

    private Button connect_base;
    private Button connect_animator;

    /*INITIALIZATIONS*/

    home_view_controller(Button connect_base,Button connect_animator)
    {
        this.connect_base = connect_base;
        this.connect_animator = connect_animator;

        initializeConnectAnimation();
        initializeConnectStyles();
    }

    /*HELPER METHODS*/

    private void initializeConnectAnimation(){
        new Thread()
        {
            public void run()
            {
                final Button tx1 = (Button) home_model.getInstance().getHomeInstance().findViewById(R.id.connect_animator);

                final ObjectAnimator scaleup = ObjectAnimator.ofPropertyValuesHolder(
                        tx1,
                        PropertyValuesHolder.ofFloat("scaleX", 1.3f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.3f),
                        PropertyValuesHolder.ofFloat("alpha", 0f)
                );

                while (true)
                {
                    try
                    {
                        sleep(3000);

                        home_model.getInstance().getHomeInstance().runOnUiThread(new Runnable() {
                            public void run() {
                                if(!scaleup.isRunning())
                                {
                                    scaleup.setDuration(1500);
                                    scaleup.start();
                                }
                            }
                        });
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void initializeConnectStyles()
    {
        /*FONTS*/

        Typeface custom_font = Typeface.createFromAsset(home_model.getInstance().getHomeInstance().getAssets(),  "fonts/gotham_med.ttf");
        connect_base.setTypeface(custom_font);

    }

    /*EVENT TO VIEW HANDLERS*/

    void onStartView()
    {

    }

}
