package com.darkweb.genesisvpn.application.homeManager;

import android.animation.ObjectAnimator;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import com.darkweb.genesisvpn.application.constants.enums;
import com.darkweb.genesisvpn.application.constants.strings;
import com.darkweb.genesisvpn.application.helperManager.helperMethods;
import com.darkweb.genesisvpn.application.proxyManager.proxy_controller;
import com.darkweb.genesisvpn.application.status.status;
import com.jwang123.flagkit.FlagKit;

import java.util.Locale;

class home_view_controller {

    /*LOCAL VIEW DECLARATION*/

    private Button connect_base;
    private Button connect_animator;
    private ImageView connect_loading;
    private TextView location_info;
    private ImageButton flag;

    /*LOCAL VARIABLE DECLARATION*/

    private ObjectAnimator scale_connect_animator = null;
    private ObjectAnimator loading_connect_animator = null;

    /*INITIALIZATIONS*/

    home_view_controller(Button connect_base, Button connect_animator, ImageView connect_loading,ImageButton flag,TextView location_info)
    {
        this.connect_base = connect_base;
        this.connect_animator = connect_animator;
        this.connect_loading = connect_loading;
        this.location_info = location_info;
        this.flag = flag;

        initializeConnectStyles();
    }

    /*HELPER METHODS*/

    private void initializeConnectStyles(){

        /*FONTS*/
        home_model.getInstance().getHomeInstance().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        int size = helperMethods.screenWidth()*60/100;

        Typeface custom_font = Typeface.createFromAsset(home_model.getInstance().getHomeInstance().getAssets(),  "fonts/gotham_med.ttf");
        connect_base.setTypeface(custom_font);

        /*WIDTH WITH SCREEN*/

        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)connect_base.getLayoutParams();
        lp.width = size;
        lp.height = size;

        connect_base.setLayoutParams(lp);
        connect_animator.setLayoutParams(lp);
        connect_loading.setLayoutParams(lp);

        Handler handler = new Handler();
        handler.post(() -> {
            home_animation.getInstance().beatAnimation(connect_animator);
            home_animation.getInstance().rotateAnimation(connect_loading);
        });

        connect_base.setText(strings.goText);
        status.connection_status = enums.connection_status.unconnected;
        ViewCompat.setTranslationZ(connect_loading, 15);
        connect_base.setTextSize(TypedValue.COMPLEX_UNIT_PX, helperMethods.screenWidth()*0.2f);
        connect_loading.setAlpha(0f);
        flag.setAlpha(0f);

        ConstraintLayout.LayoutParams lp1 = (ConstraintLayout.LayoutParams)flag.getLayoutParams();
        lp1.width = helperMethods.screenWidth()*19/100;;
        lp1.height = helperMethods.screenWidth()*14/100;;
        flag.setLayoutParams(lp1);
        location_info.setTextSize(TypedValue.COMPLEX_UNIT_PX, helperMethods.screenWidth()*0.035f);

    }

    /*EVENT TO VIEW HANDLERS*/

    void onStartView()
    {
        if(status.connection_status == enums.connection_status.connected)
        {
            connect_base.setText(strings.goText);
            connect_base.setTextSize(TypedValue.COMPLEX_UNIT_PX, helperMethods.screenWidth()*0.2f);
            connect_loading.animate().alpha(0);
            status.connection_status = enums.connection_status.unconnected;
            proxy_controller.getInstance().disConnect();
        }
        else if(status.connection_status == enums.connection_status.connecting)
        {
            connect_base.setText(strings.stopingText);
            connect_base.setTextSize(TypedValue.COMPLEX_UNIT_PX, helperMethods.screenWidth()*0.053f);
            connect_loading.animate().alpha(1);
            status.connection_status = enums.connection_status.stoping;
            proxy_controller.getInstance().disConnect();
        }
        else if(status.connection_status == enums.connection_status.stoping)
        {
            connect_base.setText(strings.connectingText);
            connect_base.setTextSize(TypedValue.COMPLEX_UNIT_PX, helperMethods.screenWidth()*0.065f);
            connect_loading.animate().alpha(1);
            status.connection_status = enums.connection_status.connecting;
        }
        else if(status.connection_status == enums.connection_status.unconnected)
        {
            connect_base.setText(strings.connectingText);
            connect_base.setTextSize(TypedValue.COMPLEX_UNIT_PX, helperMethods.screenWidth()*0.065f);
            connect_loading.animate().alpha(1);
            status.connection_status = enums.connection_status.connecting;
            proxy_controller.getInstance().connect();
        }
    }

    void onConnected()
    {
        connect_base.setTextSize(TypedValue.COMPLEX_UNIT_PX, helperMethods.screenWidth()*0.07f);
        connect_base.setText(strings.connectedText);
        connect_loading.animate().alpha(0);
        status.connection_status = enums.connection_status.connected;
    }

    void onDisConnected()
    {
        if(status.connection_status == enums.connection_status.unconnected)
        {
            connect_base.setText(strings.goText);
            connect_base.setTextSize(TypedValue.COMPLEX_UNIT_PX, helperMethods.screenWidth()*0.2f);
            connect_loading.animate().alpha(0);
            status.connection_status = enums.connection_status.unconnected;
            proxy_controller.getInstance().disConnect();
        }
    }

    void onSetFlag(String location)
    {
        if(!location.equals(strings.emptySTR))
        {
            if(flag.getAlpha()==0f)
            {
                flag.animate().alpha(1);
            }

            Locale obj = new Locale("", location);
            flag.setBackground(FlagKit.drawableWithFlag(home_model.getInstance().getHomeInstance(), location));
            location_info.setText("Powered By | " + obj.getDisplayCountry());
        }
    }

    void onHideFlag()
    {
        flag.animate().alpha(0);
        location_info.setText("Unconnected");
    }

    void onConnecting()
    {
        connect_base.setText(strings.connectingText);
        connect_base.setTextSize(TypedValue.COMPLEX_UNIT_PX,helperMethods.screenWidth()*0.065f);
        connect_loading.animate().alpha(1);
        status.connection_status = enums.connection_status.connecting;
        proxy_controller.getInstance().connect();
    }
    /*ANIMATION VIEW REDIRECTIONS*/


}
