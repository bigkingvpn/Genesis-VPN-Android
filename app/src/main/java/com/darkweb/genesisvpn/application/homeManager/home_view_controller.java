package com.darkweb.genesisvpn.application.homeManager;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

import com.darkweb.genesisvpn.application.constants.enums;
import com.darkweb.genesisvpn.application.helperManager.helperMethods;
import com.darkweb.genesisvpn.application.status.status;

class home_view_controller {

    /*LOCAL VIEW DECLARATION*/

    private Button connect_base;
    private Button connect_animator;
    private ImageView connect_loading;
    private Thread animationThread = null;

    /*LOCAL VARIABLE DECLARATION*/

    private ObjectAnimator scale_connect_animator = null;
    private ObjectAnimator loading_connect_animator = null;

    /*INITIALIZATIONS*/

    home_view_controller(Button connect_base, Button connect_animator, ImageView connect_loading)
    {
        this.connect_base = connect_base;
        this.connect_animator = connect_animator;
        this.connect_loading = connect_loading;

        initializeConnectAnimation();
        initializeConnectStyles();
    }

    /*HELPER METHODS*/

    private void initializeConnectAnimation(){
        animationThread = new Thread()
        {
            @SuppressWarnings("InfiniteLoopStatement")
            public void run()
            {
                while (true)
                {
                    try
                    {
                            home_model.getInstance().getHomeInstance().runOnUiThread(new Runnable() {
                            public void run() {

                                if(scale_connect_animator==null)
                                {
                                    scale_connect_animator = home_animations.circleGrow(connect_animator);
                                    connect_loading.animate().alpha(0f);
                                }
                                else if(status.connection_status == enums.connection_status.unconnected)
                                {
                                    connect_base.setText("GO");
                                    connect_base.setTextSize(TypedValue.COMPLEX_UNIT_SP, 105);
                                    if(connect_loading.getAlpha()!=0)
                                    {
                                        connect_loading.animate().alpha(0f);
                                    }
                                }
                                else if(status.connection_status == enums.connection_status.connecting)
                                {
                                    connect_loading.animate().alpha(1f);
                                    connect_loading.bringToFront();
                                    ViewCompat.setTranslationZ(connect_loading, 11);
                                    connect_base.setText("Connecting");
                                    connect_base.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
                                }
                                else if(status.connection_status == enums.connection_status.connected)
                                {
                                    connect_loading.animate().alpha(0f);
                                    connect_base.setText("Connected");
                                    connect_base.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
                                }

                                if(scale_connect_animator!=null && !scale_connect_animator.isRunning())
                                {
                                    scale_connect_animator.setDuration(1500);
                                    scale_connect_animator.start();
                                }

                            }
                        });
                        sleep(3000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        animationThread.start();
    }

    private void initializeConnectStyles(){

        /*FONTS*/

        int size = helperMethods.screenWidth()*60/100;

        Typeface custom_font = Typeface.createFromAsset(home_model.getInstance().getHomeInstance().getAssets(),  "fonts/gotham_med.ttf");
        connect_base.setTypeface(custom_font);
        helperMethods.screenToFont(connect_base,size);

        /*WIDTH WITH SCREEN*/

        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)connect_base.getLayoutParams();
        lp.width = size;
        lp.height = size;

        connect_base.setLayoutParams(lp);
        connect_animator.setLayoutParams(lp);

        lp.width = size;
        lp.height = size;

        connect_loading.setLayoutParams(lp);

        /*LOADING ICON*/

        connect_loading.setAlpha(0f);
        ViewCompat.setTranslationZ(connect_loading, 11);

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3000);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        connect_loading.startAnimation(rotate);

        connect_base.setTextSize(TypedValue.COMPLEX_UNIT_SP, 105);
    }

    /*EVENT TO VIEW HANDLERS*/

    void onStartView()
    {
        if(animationThread!=null)
        {
            animationThread.interrupt();
        }

        if(status.connection_status == enums.connection_status.connecting || status.connection_status == enums.connection_status.connected)
        {
            status.connection_status = enums.connection_status.unconnected;
        }
        else if(status.connection_status == enums.connection_status.unconnected)
        {
            status.connection_status = enums.connection_status.connecting;
        }
    }

}
